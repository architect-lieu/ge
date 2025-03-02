package com.huhuhu.project.service;

import com.huhuhu.project.entity.DocDownloadSearchRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 02:56:45
 */
public interface DocDownloadRecordService extends IService<DocDownloadSearchRecord> {

    /**
     * 查找和下载记录
     * @return
     */
    List<DocDownloadSearchRecord> searchDownloadRecord(Integer type);
}
