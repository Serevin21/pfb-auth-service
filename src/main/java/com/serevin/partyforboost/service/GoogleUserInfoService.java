package com.serevin.partyforboost.service;

import com.serevin.partyforboost.dto.google.userinfo.UserInfo;

public interface GoogleUserInfoService {

    UserInfo getUserInfoByAccessToken(String accessToken);

}
