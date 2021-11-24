package com.lij.myqqrobotserver.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Celphis
 */
@Configuration
public class GoCqHttpConfig {
    private String selfQQ;
    private Integer replyType;
    public static final String MSG_TYPE_GROUP = "group";
    public static final String MSG_TYPE_PRIVATE = "private";
    public static final String POST_TYPE_MESSAGE= "message";
    public static final String POST_TYPE_NOTICE="notice";
    
    public String getSelfQQ() {
        return selfQQ;
    }

    @Value("${go_cqhttp.selfQQ}")
    public void setSelfQQ(String selfQQ) {
        this.selfQQ = selfQQ;
    }

    public Integer getReplyType() {
        if (replyType == null || replyType != 2) {
            replyType = 1;
        }
        return replyType;
    }

    @Value("${go_cqhttp.reply.type}")
    public void setReplyType(Integer replyType) {
        this.replyType = replyType;
    }
}
