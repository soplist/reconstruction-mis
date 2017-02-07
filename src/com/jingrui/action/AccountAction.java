package com.jingrui.action;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.jingrui.domain.UserInformation;
import com.jingrui.domain.UserLoginInformation;
import com.jingrui.service.UserLoginInformationService;
import com.jingrui.util.BASE64;
import com.jingrui.util.LoadChineseCharactersListXml;
import com.jingrui.util.Message;
import com.opensymphony.xwork2.ActionSupport;

/**
 * <p>Title: AccountAction.java</p>
 * <p>Description: information management software platform</p>
 * <p>Copyright: Copyright (c) 2011-2012 JinRui Information Technology Co., Ltd.</p>
 * <p>Company: JinRui Information Technology Co., Ltd.</p>
 * @author wangkang
 * @version 1.0 creation time£º2017-1-12 ÏÂÎç3:09:36
 */

public class AccountAction extends ActionSupport{
	
	private HttpSession session;
	private String password;
	
	private static final String USER_INFORMATION_SESSION_KEY = "userInformation";
	private static final String UPDATE_PASSWORD_SUCCESS_KEY = "update_password_message_1";
	private static final String USER_NOT_EXIST_KEY = "update_password_message_2";
	private static final String MESSAGE_SESSION_KEY = "message";
	
	private UserLoginInformationService userLoginInformationService;
	
	private LoadChineseCharactersListXml loadChineseCharactersListXml;
	
	private Message message;
	private BASE64 base64;
	
	public AccountAction(){
		this.session=ServletActionContext.getRequest().getSession();
	}

	public String updatePassword(){
		UserInformation userInformation = (UserInformation) session.getAttribute(USER_INFORMATION_SESSION_KEY);
		if(userInformation != null){
			UserLoginInformation userLoginInformation = userLoginInformationService.getById(userInformation.getId());
			
			if(userLoginInformation == null){
			    
			    try {
			    	UserLoginInformation newUserLoginInformation = new UserLoginInformation();
				    newUserLoginInformation.setUserId(userInformation.getId());
					newUserLoginInformation.setPassword(new String(base64.encryptBASE64(password.getBytes())));
					userLoginInformationService.add(newUserLoginInformation);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
			}
			else{
				try {
				    userLoginInformation.setPassword(new String(base64.encryptBASE64(password.getBytes())));
				    userLoginInformationService.update(userLoginInformation);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			message.setContent(loadChineseCharactersListXml.getNodeListMap().get(UPDATE_PASSWORD_SUCCESS_KEY));
			session.setAttribute(MESSAGE_SESSION_KEY, message);
			
		    return "success";
		}else{
			message.setContent(loadChineseCharactersListXml.getNodeListMap().get(USER_NOT_EXIST_KEY));
			session.setAttribute(MESSAGE_SESSION_KEY, message);
			
			return "failure";
		}
		
	}
	
	public String beforeUpdatePassword(){
		return "updatePassword";
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public UserLoginInformationService getUserLoginInformationService() {
		return userLoginInformationService;
	}

	public void setUserLoginInformationService(
			UserLoginInformationService userLoginInformationService) {
		this.userLoginInformationService = userLoginInformationService;
	}
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	public LoadChineseCharactersListXml getLoadChineseCharactersListXml() {
		return loadChineseCharactersListXml;
	}

	public void setLoadChineseCharactersListXml(
			LoadChineseCharactersListXml loadChineseCharactersListXml) {
		this.loadChineseCharactersListXml = loadChineseCharactersListXml;
	}
	
	public BASE64 getBase64() {
		return base64;
	}

	public void setBase64(BASE64 base64) {
		this.base64 = base64;
	}

}
