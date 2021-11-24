package com.lij.myqqrobotserver.filter.base;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lij.myqqrobotserver.api.GoCqHttpApi;
import com.lij.myqqrobotserver.common.config.GoCqHttpConfig;
import com.lij.myqqrobotserver.dto.MessageDto;

/**
 * @author Celphis
 */
@Component
public abstract class BaseFilter implements Comparable<BaseFilter>{
	protected String commendStr;
	protected Integer order=0;
	
	protected static Logger logger = LogManager.getLogger();

	private static GoCqHttpConfig goCqHttpConfig;

	@Autowired
	public void setGoCqHttpConfig(GoCqHttpConfig config) {
		goCqHttpConfig = config;
	}

	@PostConstruct
	protected abstract void init() ;
	
	/**
	 * 根据消息体内容执行操作并返回处理消息
	 *
	 * @param inDto 得到的消息体
	 * @return 返回的消息体
	 */
	protected abstract MessageDto checkMessage(MessageDto inDto) throws Exception;

	public boolean filt(MessageDto inDto) {
		MessageDto outDto = null;
		try {
			outDto = this.checkMessage(inDto);
		} catch (Exception e) {
			logger.error("", e);
		}
		if (outDto != null && !StringUtils.isBlank(outDto.sendMessage)) {
			Long messgaeId = sendMessage(outDto);
			if (outDto.deleteMSgFlag) {
				new Runnable() {
					@Override
					public void run() {
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								deleteMessage(messgaeId);
							}
						}, 110000);
					}
				}.run();;
			}
			return true;
		}
		return false;
	}

	public Long sendMessage(MessageDto outDto) {
		JSONObject response = null;
		Long msgId = null;
		if (GoCqHttpConfig.MSG_TYPE_PRIVATE.equals(outDto.messageType)) {
			response = GoCqHttpApi.sendMsg(outDto.toQQ, null, outDto.sendMessage);

		} else if (GoCqHttpConfig.MSG_TYPE_GROUP.equals(outDto.messageType)) {
			if (goCqHttpConfig.getReplyType() == 2 || GoCqHttpConfig.MSG_TYPE_PRIVATE.equals(outDto.sendType)) {
				GoCqHttpApi.sendPrivateMsg(outDto.toQQ, outDto.fromGroup, outDto.sendMessage);
			} else if (goCqHttpConfig.getReplyType() == 1) {
				if (outDto.atFlag) {
					response = GoCqHttpApi.sendGroupMsg(outDto.fromGroup,
							"[CQ:at,qq=" + outDto.toQQ + "]\n" + outDto.sendMessage);
				} else {
					response = GoCqHttpApi.sendGroupMsg(outDto.fromGroup, outDto.sendMessage);
				}
				if (response.getInteger("retcode") != 0) {
					response = GoCqHttpApi.sendPrivateMsg(outDto.toQQ, outDto.fromGroup, outDto.sendMessage);
				}
			}
		}
		try {
			msgId = response.getJSONObject("data").getLong("message_id");
		} catch (Exception ignore) {

		}
		return msgId;
	}

	public void deleteMessage(Long messageId) {
		GoCqHttpApi.deleteMsg(messageId);
	}

	public static void prepareMessage(MessageDto messageDto) {
		if (messageDto.rawMessage.matches("^.*\\[CQ:at,qq=[0-9]+\\].*$")) {
			String rawMessage = messageDto.rawMessage.replaceAll("\\[CQ:at,qq=[0-9]+\\]\\s*", "");
			String toQQ = messageDto.rawMessage.replaceAll(".*\\[CQ:at,qq=", "").replaceAll("\\].*", "");
			messageDto.rawMessage = rawMessage;
			if (!goCqHttpConfig.getSelfQQ().equals(toQQ)) {
				messageDto.toQQ = toQQ;
			} else {
				messageDto.toQQ = messageDto.fromQQ;
				messageDto.toRobotFlag = true;
			}
		} else {
			messageDto.toQQ = messageDto.fromQQ;
		}
	}

	protected MessageDto parseMessage(MessageDto inDto, String sendMessage) {
		return new MessageDto(inDto, sendMessage);
	}
	
	public String getCommendStr() {
		return commendStr;
	}
	public Integer getOrder() {
		return order;
	}

	@Override
	public int compareTo(BaseFilter filter) {
		int i = this.getOrder()-filter.getOrder();
		return -i;
	}
	
}
