package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-07-01 09:07:37
 */
public interface MessageService extends IService<Message> {

    Boolean add(Message message);

    Boolean modify(Message message);

    Page<Message> pageList(int page, int size);

    Message detail(Long id);
}
