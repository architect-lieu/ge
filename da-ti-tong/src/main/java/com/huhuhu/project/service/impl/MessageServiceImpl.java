package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Message;
import com.huhuhu.project.entity.MessageClient;
import com.huhuhu.project.mapper.MessageClientMapper;
import com.huhuhu.project.mapper.MessageMapper;
import com.huhuhu.project.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-07-01 09:07:37
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final MessageClientMapper messageClientMapper;

    @Override
    public Boolean add(Message message) {
        return this.save(message);
    }

    @Override
    public Boolean modify(Message message) {
        return this.updateById(message);
    }

    @Override
    public Page<Message> pageList(int page, int size) {
        Page<Message> messagePage = this.page(Page.of(page, size));
        List<Message> records = messagePage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return messagePage;
        }
        // 查询阅读记录
        if (TokenUtil.currentLoginUserType().equals(SystemConstant.CUSTOMER)) {
            List<Long> msgIds = records.stream().map(Message::getMessageId).collect(Collectors.toList());
            Map<Long, MessageClient> messageClients = messageClientMapper.selectList(Wrappers.lambdaQuery(MessageClient.class).eq(MessageClient::getClientId, TokenUtil.currentLoginUserId())
                    .in(MessageClient::getMessageId, msgIds)).stream().collect(Collectors.toMap(MessageClient::getMessageId, Function.identity()));
            for (Message record : records) {
                record.setReadFlag(messageClients.get(record.getMessageId()) != null);
            }
        }
        return messagePage;
    }

    @Override
    public Message detail(Long id) {
        Message message = this.getById(id);
        if (message == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        String type = TokenUtil.currentLoginUserType();
        if (SystemConstant.CUSTOMER.equals(type)) {
            // 查询记录
            MessageClient messageClient = messageClientMapper.selectOne(Wrappers.lambdaQuery(MessageClient.class).eq(MessageClient::getClientId, TokenUtil.currentLoginUserId())
                    .eq(MessageClient::getMessageId, id));
            if (messageClient != null) {
                message.setReadFlag(Boolean.TRUE);
                return message;
            }
            // 插入阅读记录
            messageClient = new MessageClient();
            messageClient.setMessageId(id);
            messageClient.setClientId(TokenUtil.currentLoginUserId());
            messageClientMapper.insert(messageClient);
        }
        return message;
    }
}
