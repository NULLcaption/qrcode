package com.cxg.qrcode.server.mapper;

import com.cxg.qrcode.server.pojo.UserTokenDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserInfoMapper {

    int updateUserInfoById(UserTokenDo userTokenDo);

    UserTokenDo selectUserInfoById(UserTokenDo userTokenDo);

    UserTokenDo selectUserInfoByUserId(@Param("userId") String userId);
}
