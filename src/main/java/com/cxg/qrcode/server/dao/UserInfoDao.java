package com.cxg.qrcode.server.dao;

import com.cxg.qrcode.server.pojo.UserTokenDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao extends JpaRepository<UserTokenDo, Long> {
}
