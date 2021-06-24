package com.cxg.qrcode.server.service;

import com.cxg.qrcode.server.pojo.UserTokenDo;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoServiceImpl {
    UserTokenDo createUserToken(UserTokenDo userTokenDo);

    String bindUserIdAndToken(String userId, String token, Integer projId) throws Exception;

    UserTokenDo selectUserInfoByUserId(String userId);
}
