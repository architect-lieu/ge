package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Document;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.DocumentPageParams;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-12 06:15:19
 */
public interface DocumentService extends IService<Document> {

    /**
     * 添加文档
     * @param document
     * @return
     */
    Boolean add(Document document);

    /**
     * 更新文档
     * @param document
     * @return
     */
    Boolean modify(Document document);

    /**
     * 文档详情
     * @param id
     * @return
     */
    Document detail(Integer id);

    /**
     * 分页查询文档
     * @param documentPageParams
     * @return
     */
    Page<Document> pageList(DocumentPageParams documentPageParams);

    /**
     * 上传文档
     * @param file
     * @return
     */
    Map<String,Object> uploadDoc(MultipartFile file);


    /**
     * 文档下载
     * @param response
     * @param downloadFlag
     */
    void downloadDoc(HttpServletResponse response, String downloadFlag);

    Map<String,Object> coverImage(MultipartFile file);
}
