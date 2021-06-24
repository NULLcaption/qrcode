package com.cxg.qrcode.server.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cxg.qrcode.server.dao.UserInfoDao;
import com.cxg.qrcode.server.mapper.UserInfoMapper;
import com.cxg.qrcode.server.pojo.UserTokenDo;
import com.cxg.qrcode.server.service.UserInfoServiceImpl;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class UserInfoService implements UserInfoServiceImpl {

    static Log log= LogFactory.get(UserInfoService.class);

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserInfoDao userInfoDao;

    /**
     * 初始化用户
     * @param userTokenDo
     * @return
     */
    @Override
    public UserTokenDo createUserToken(UserTokenDo userTokenDo) {
        try {
            userInfoDao.save(userTokenDo);
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    /**
     * 用户绑定token验证扫码登录
     * @param userId
     * @param token
     * @param projId
     * @return
     * @throws Exception
     */
    @Override
    public String bindUserIdAndToken(String userId, String token, Integer projId) throws Exception {
        //判断用户是否存在
        UserTokenDo userTokenDo = new UserTokenDo();
        userTokenDo.setUuid(token);
        userTokenDo = userInfoMapper.selectUserInfoById(userTokenDo);
        if(null == userTokenDo){
            throw  new Exception("错误的请求！");
        }
        //判断是否超时
        Date createDate = new Date(userTokenDo.getCreateTime().getTime() + (1000 * 60 * 1200));
        Date nowDate = new Date();
        if(nowDate.getTime() > createDate.getTime()){//当前时间大于校验时间
            Map map = new HashMap();
            map.put("code",500);
            map.put("msg","二维码失效！");
            WebSocketServer.sendInfo(JSONUtil.parse(map).toString() ,token);
            throw new Exception("二维码失效！");
        }
        //正常登陆
        userTokenDo.setState("1");
        userTokenDo.setLoginTime(DateUtil.format(new Date(), "yyyy-MM-dd hh:mm:ss"));
        userTokenDo.setUserId(userId);
        int i = userInfoMapper.updateUserInfoById(userTokenDo);
        Map map = new HashMap();
        map.put("code",200);
        map.put("msg","ok");
        map.put("userId",userId);
        if(StrUtil.isNotEmpty(String.valueOf(projId))){
            map.put("projId",projId);
        }
        WebSocketServer.sendInfo(JSONUtil.parse(map).toString(), token);
        if(i > 0 ){
            return null;
        }else{
            throw new Exception("服务器异常！");
        }
    }

    @Override
    public UserTokenDo selectUserInfoByUserId(String userId) {
        try {
            return userInfoMapper.selectUserInfoByUserId(userId);
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
}
