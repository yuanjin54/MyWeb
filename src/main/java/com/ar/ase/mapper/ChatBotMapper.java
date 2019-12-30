package com.ar.ase.mapper;

import com.ar.ase.entity.ChatBot;
import org.apache.ibatis.annotations.Mapper;

/**
 * ChatBotMapper
 *
 * @author yuanjin
 * @date 2019/12/30
 */
@Mapper
public interface ChatBotMapper {
    void insert(ChatBot chatBot);
}
