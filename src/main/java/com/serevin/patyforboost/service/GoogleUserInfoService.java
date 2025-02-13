package com.serevin.patyforboost.service;

import com.serevin.patyforboost.dto.google.userinfo.UserInfo;

public interface GoogleUserInfoService {

    UserInfo getUserInfoByAccessToken(String accessToken);

}
