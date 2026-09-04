package com.ma.aiagent.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName messages
 */
@TableName(value ="conversation")
@Data
@Builder
public class Conversation implements Serializable {
    /**
     * 编号，主键
     */
    @TableId
    private Long id;

    /**
     * 会话Id
     */
    private String conversationId;

    /**
     * 消息类型

     */
    private String messageType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息存储时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}