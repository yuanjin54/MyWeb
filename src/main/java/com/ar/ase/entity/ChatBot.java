package com.ar.ase.entity;

import lombok.*;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * ChatBot
 *
 * @author yuanjin
 * @date 2019/12/30
 */
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatBot implements Serializable{
    private static final long serialVersionUID = -7681377079999756120L;
    private Integer id;
    private String ipAddress;
    private String question;
    private String answer;
    private Date createTime;
}
