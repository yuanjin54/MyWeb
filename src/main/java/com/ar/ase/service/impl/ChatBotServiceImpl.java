package com.ar.ase.service.impl;

import com.ar.ase.entity.ChatBot;
import com.ar.ase.mapper.ChatBotMapper;
import com.ar.ase.service.ChatBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户信息表ServiceImpl
 *
 * @author yuanjin
 */
@Service
public class ChatBotServiceImpl implements ChatBotService {
    private static final Logger logger = LoggerFactory.getLogger(ChatBotServiceImpl.class);

    @Resource
    private ChatBotMapper chatBotMapper;

    @Override
    public void insert(ChatBot chatBot) {
        chatBotMapper.insert(chatBot);
    }
}
