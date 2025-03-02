package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.cache.JvmCache;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.common.oss.OssUtil;
import com.huhuhu.project.entity.DocDownloadSearchRecord;
import com.huhuhu.project.entity.Document;
import com.huhuhu.project.entity.VipConfig;
import com.huhuhu.project.enums.DownloadSearchRecordEnum;
import com.huhuhu.project.enums.UserEnum;
import com.huhuhu.project.form.params.DocumentPageParams;
import com.huhuhu.project.mapper.DocDownloadRecordMapper;
import com.huhuhu.project.mapper.DocumentMapper;
import com.huhuhu.project.service.DocumentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.DocToImageUtil;
import com.huhuhu.project.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-12 06:15:19
 */
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {

    private final OssUtil ossUtil;

    private final DocDownloadRecordMapper docDownloadRecordMapper;

    private final DocumentMapper documentMapper;


    @Override
    public Boolean add(Document document) {
        document.setDownloadFlag(UUID.randomUUID().toString().replace("-", ""));
        return this.save(document);
    }

    @Override
    public Boolean modify(Document document) {
        return this.updateById(document);
    }

    @Override
    public Document detail(Integer id) {
        Document document = this.getById(id);
        if (document == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return document;
    }

    @Override
    public Page<Document> pageList(DocumentPageParams documentPageParams) {
        String docName = documentPageParams.getDocName();
        Long categoryId = documentPageParams.getCategoryId();
        LambdaQueryWrapper<Document> queryWrapper = Wrappers.lambdaQuery(Document.class)
                .select(i -> TokenUtil.loginCheck() && SystemConstant.ADMIN.equals(TokenUtil.currentLoginUserType()) || !i.getColumn().equals("doc_download_url"))
                .eq(categoryId != null, Document::getCategoryId, categoryId)
                .like(StrUtil.isNotBlank(docName), Document::getDocName, docName)
                .orderByDesc(Document::getCreateTime);
        Page<Document> documentPage = this.page(Page.of(documentPageParams.getPage(), documentPageParams.getSize()), queryWrapper);
        List<Document> records = documentPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<Long> docIds = records.stream().map(Document::getDocId).collect(Collectors.toSet());
            LambdaQueryWrapper<DocDownloadSearchRecord> recordLambdaQueryWrapper = Wrappers.query(new DocDownloadSearchRecord())
                    .select("doc_id,count(doc_id) as downloadNum")
                    .lambda()
                    .in(DocDownloadSearchRecord::getDocId, docIds)
                    .eq(DocDownloadSearchRecord::getType, DownloadSearchRecordEnum.DOWNLOAD.getDesc())
                    .groupBy(DocDownloadSearchRecord::getDocId);
            List<Map<String, Object>> mapList = docDownloadRecordMapper.selectMaps(recordLambdaQueryWrapper);
            Map<Long, Object> numMap = mapList.stream().collect(Collectors.toMap(i -> Long.parseLong(i.get("doc_id").toString()), i -> i.get("downloadNum")));
            for (Document record : records) {
                Object num = numMap.get(record.getDocId());
                if (num == null) {
                    record.setDownloadNum(0);
                    continue;
                }
                record.setDownloadNum(Integer.parseInt(num.toString()));
            }
        }
        return documentPage;
    }

    /**
     * 手动关流
     *
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> uploadDoc(MultipartFile file) {
        BufferedInputStream bufferedInputStream = null;
        InputStream inputStream = null;
        try {
            Map<String, Object> result = new HashMap<>();
            if (StrUtil.isBlank(file.getOriginalFilename())) {
                throw new BusinessException(3001, "请指定文件名称！");
            }
            String type = OssUtil.getFileType(file.getOriginalFilename());
            result.put("type", type);
            inputStream = file.getInputStream();
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedInputStream.mark(inputStream.available() + 1);
            List<InputStream> bufferedImages = DocToImageUtil.docStreamToImages(bufferedInputStream, file.getOriginalFilename());
            bufferedInputStream.reset();
            if (CollUtil.isNotEmpty(bufferedImages)) {
                List<String> images = new ArrayList<>();
                for (int i = 0; i < bufferedImages.size(); i++) {
                    InputStream stream = bufferedImages.get(i);
                    Map<String, Object> data = ossUtil.uploadFile2(stream, i + "-" + UUID.randomUUID().toString().replace("-", "") + ".png");
                    images.add((String) data.get("urlData"));
                }
                result.put("previewImages", images);
                result.put("coverImage", images.get(0));
            }
            Map<String, Object> uploadFile = ossUtil.uploadFile2(bufferedInputStream, file.getOriginalFilename());
            result.putAll(uploadFile);
            return result;
        } catch (Exception e) {
            if (e instanceof BusinessException)
                throw (BusinessException) e;
            throw new RuntimeException("文件上传失败！");
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    //
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    @Override
    @Transactional
    public void downloadDoc(HttpServletResponse response, String downloadFlag) {
        // 查询文档信息
        Document document = documentMapper.selectOne(Wrappers.lambdaQuery(Document.class)
                .eq(Document::getDownloadFlag, downloadFlag));
        if (document == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        // 不是免费文档需要判断用户本月的下载次数
        if (document.getFreeFlag() == null || !document.getFreeFlag()) {
            checkUserDownloadNum();
        }
        // 下载
        OSSObject ossObject = null;
        try {
            ossObject = ossUtil.download(document.getDownloadUrlKey());
            String key = ossObject.getKey();
            response.setHeader("Content-Disposition", "attachment;filename=" + key.substring(key.lastIndexOf("/") + 1));
            response.setHeader("Content-Type", OssUtil.getContentType(key.substring(key.lastIndexOf("."))));
            IoUtil.copy(ossObject.getObjectContent(), response.getOutputStream());
            // 记录下载
            // 查询是否下载过
            Long count = docDownloadRecordMapper.selectCount(Wrappers.lambdaQuery(DocDownloadSearchRecord.class)
                    .eq(DocDownloadSearchRecord::getUserId, TokenUtil.currentLoginUserId())
                    .eq(DocDownloadSearchRecord::getType, DownloadSearchRecordEnum.DOWNLOAD.getDesc())
                    .eq(DocDownloadSearchRecord::getDocId, document.getDocId()));
            if (count > 0)
                return;
            Long userId = TokenUtil.currentLoginUserId();
            DocDownloadSearchRecord docDownloadSearchRecord = new DocDownloadSearchRecord();
            docDownloadSearchRecord.setUserId(userId);
            docDownloadSearchRecord.setDocId(document.getDocId());
            docDownloadSearchRecord.setType(DownloadSearchRecordEnum.DOWNLOAD.getDesc());
            docDownloadRecordMapper.insert(docDownloadSearchRecord);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
    }

    @Override
    public Map<String, Object> coverImage(MultipartFile file) {
        if (StrUtil.isBlank(file.getOriginalFilename())) {
            throw new BusinessException(3001, "请指定文件名称！");
        }
        try {
            return ossUtil.uploadFile2(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkUserDownloadNum() {
        Map<String, Object> claims = TokenUtil.getClaims();
        Object vipFlag = claims.get("vipFlag");
        if (vipFlag == null) vipFlag = UserEnum.IDENTITY_USER.getCode();
        Integer flag = (Integer) vipFlag;
        // 计算月时间
        DateTime currentTime = new DateTime();
        currentTime.setMutable(false);
        DateTime lastTime = currentTime.offset(DateField.DAY_OF_MONTH, -DateTime.now().dayOfMonth() + 1);
        // 用户下载的总次数
        Long downloadCount = docDownloadRecordMapper.selectCount(Wrappers.lambdaQuery(DocDownloadSearchRecord.class)
                .eq(DocDownloadSearchRecord::getUserId, TokenUtil.currentLoginUserId())
                .eq(DocDownloadSearchRecord::getType, DownloadSearchRecordEnum.DOWNLOAD.getDesc())
                .between(DocDownloadSearchRecord::getCreateTime, lastTime, currentTime));

        UserEnum userEnum = UserEnum.getByCode(flag);
        VipConfig vipConfig = JvmCache.getVipConfig(userEnum.getDesc());
        if (vipConfig != null) {
            Integer monthDownloadDocNum = vipConfig.getMonthDownloadDocNum();
            // 是否到达上限
            if (monthDownloadDocNum != -1 && downloadCount >= monthDownloadDocNum) {
                throw new BusinessException(ResultCode.MONTH_MAX_DOWNLOAD_ERROR);
            }
        }
    }
}
