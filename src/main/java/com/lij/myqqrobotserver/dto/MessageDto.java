package com.lij.myqqrobotserver.dto;

/**
 * @author Celphis
 */
public class MessageDto implements Cloneable {
    public String messageType;
    public String rawMessage;
    public String message;
    public String fromQQ;
    public String fromQQNickName;
    public String fromQQGroupCard;
    public String fromGroup;
    public String toQQ;
    public String sendMessage;
    public String sendType;
    public boolean toRobotFlag = false;
    public boolean deleteMSgFlag = false;
    public boolean atFlag = true;
    public String postType;
    public String targetId;
    public MessageDto(){
    	
    }
    
    public MessageDto(String messageType, String rawMessage, String message, String fromQQ, String fromGroup) {
        this.messageType = messageType;
        this.rawMessage = rawMessage;
        this.message = message;
        this.fromQQ = fromQQ;
        this.fromGroup = fromGroup;
    }

    public MessageDto(String messageType, String rawMessage, String fromQQ, String fromQQNickName, String fromQQGroupCard, String fromGroup) {
        this.messageType = messageType;
        this.rawMessage = rawMessage;
        this.fromQQ = fromQQ;
        this.fromQQNickName = fromQQNickName;
        this.fromQQGroupCard = fromQQGroupCard;
        this.fromGroup = fromGroup;
    }

    public MessageDto(MessageDto messageDto, String sendMessage) {
        this.messageType = messageDto.messageType;
        this.rawMessage = messageDto.rawMessage;
        this.fromQQ = messageDto.fromQQ;
        this.fromQQNickName = messageDto.fromQQNickName;
        this.fromQQGroupCard = messageDto.fromQQGroupCard;
        this.fromGroup = messageDto.fromGroup;
        this.toQQ = messageDto.toQQ;
        this.sendMessage = sendMessage;
        this.message = messageDto.message;
        this.sendType = messageDto.sendType;
        this.toRobotFlag = messageDto.toRobotFlag;
        this.deleteMSgFlag = messageDto.deleteMSgFlag;
        this.atFlag=messageDto.atFlag;
    }

    @Override
    public MessageDto clone() {
        try {
            return (MessageDto) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "messageType='" + messageType + '\'' +
                ", rawMessage='" + rawMessage + '\'' +
                ", message='" + message + '\'' +
                ", fromQQ='" + fromQQ + '\'' +
                ", fromQQNickName='" + fromQQNickName + '\'' +
                ", fromQQGroupCard='" + fromQQGroupCard + '\'' +
                ", fromGroup='" + fromGroup + '\'' +
                ", toQQ='" + toQQ + '\'' +
                '}';
    }
}
