package com.jinsong.core.qq.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

import com.jinsong.core.qq.api.QQ;
import com.jinsong.core.qq.api.QQUserInfo;

/*
 * ApiAdapter的泛型填要适应的API接口
 */
public class QQAdapter implements ApiAdapter<QQ> {

	@Override
	public boolean test(QQ api) {
		// TODO Auto-generated method stub
		//认为QQ永远是OK的，也就是QQ官方提供的API永远是可以用的
		return true;
	}

	@Override
	public void setConnectionValues(QQ api, ConnectionValues values) {
		// TODO Auto-generated method stub
		QQUserInfo userInfo =api.getQQUserInfo();
		
		values.setDisplayName(userInfo.getNickname());//昵称
		values.setImageUrl(userInfo.getFigureurl_qq_1());//头像
		values.setProfileUrl(null);//主页链接，QQ是没有这个的，微博有
		values.setProviderUserId(userInfo.getOpenId());//服务商ID
	}

	@Override
	public UserProfile fetchUserProfile(QQ api) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus(QQ api, String message) {
		// TODO Auto-generated method stub
		
	}

}
