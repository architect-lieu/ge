package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huhuhu.project.entity.DocDownloadSearchRecord;
import com.huhuhu.project.entity.Document;
import com.huhuhu.project.enums.DownloadSearchRecordEnum;
import com.huhuhu.project.mapper.DocDownloadRecordMapper;
import com.huhuhu.project.mapper.DocumentMapper;
import com.huhuhu.project.service.DocDownloadRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 02:56:45
 */
@Service
@RequiredArgsConstructor
public class DocDownloadRecordServiceImpl extends ServiceImpl<DocDownloadRecordMapper, DocDownloadSearchRecord> implements DocDownloadRecordService {

    private final DocumentMapper documentMapper;

    @Override
    public List<DocDownloadSearchRecord> searchDownloadRecord(Integer type) {
        String t = "";
        if (DownloadSearchRecordEnum.SEARCH.getCode().equals(type)) {
            t = DownloadSearchRecordEnum.SEARCH.getDesc();
        } else if (DownloadSearchRecordEnum.DOWNLOAD.getCode().equals(type)) {
            t = DownloadSearchRecordEnum.DOWNLOAD.getDesc();
        }
        List<DocDownloadSearchRecord> docDownloadSearchRecords = this.list(Wrappers.lambdaQuery(DocDownloadSearchRecord.class)
                .eq(DocDownloadSearchRecord::getUserId, TokenUtil.currentLoginUserId())
                .eq(DocDownloadSearchRecord::getType, t));
        if (CollUtil.isEmpty(docDownloadSearchRecords)) {
            return Collections.emptyList();
        }
        Map<Long, Document> documentMap = null;
        if (DownloadSearchRecordEnum.DOWNLOAD.getCode().equals(type)) {
            // 查询文档
            Set<Long> docIds = docDownloadSearchRecords.stream().map(DocDownloadSearchRecord::getDocId).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(docIds)) {
                documentMap = documentMapper.selectList(Wrappers.lambdaQuery(Document.class)
                        .select(item -> !item.getColumn().equals("doc_download_url"))
                        .in(Document::getDocId, docIds)).stream().collect(Collectors.toMap(Document::getDocId, Function.identity()));
            }
        }
        for (DocDownloadSearchRecord downloadSearchRecord : docDownloadSearchRecords) {
            if (documentMap != null) {
                downloadSearchRecord.setDocument(documentMap.get(downloadSearchRecord.getDocId()));
            }
        }
        return docDownloadSearchRecords;
    }
}
