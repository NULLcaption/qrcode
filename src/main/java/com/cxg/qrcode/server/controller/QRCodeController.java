package com.cxg.qrcode.server.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.cxg.qrcode.core.utils.ResponseUtil;
import com.cxg.qrcode.server.pojo.UserTokenDo;
import com.cxg.qrcode.server.service.UserInfoServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 获取二维码
 */
@RequestMapping("/index")
@Controller
public class QRCodeController {

    static Log log= LogFactory.get(QRCodeController.class);

    @Autowired
    UserInfoServiceImpl userInfoService;

    /**
     * 二维码登录页面
     * @return
     */
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    /**
     * 获取登录的二维码
     * @param request
     * @param response
     */
    @GetMapping("/getLoginQr")
    public void createCodeImg(HttpServletRequest request, HttpServletResponse response){
        //设置response header参数
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //创建创建登录用户，并将生成的UUID绑定在response header中
        UserTokenDo userTokenDo = new UserTokenDo();
        userTokenDo.setUserId(IdUtil.randomUUID());
        userTokenDo.setCreateTime(new Date());
        userTokenDo.setLoginTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        userTokenDo.setState("0");
        UserTokenDo user =  userInfoService.createUserToken(userTokenDo);
        try {
            response.setHeader("uuid", userTokenDo.getUuid());
            QrCodeUtil.generate(userTokenDo.getUuid(), 300, 300, "jpg",response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定token和用户
     * @param token
     * @param userId
     * @param projId
     * @return
     */
    @GetMapping("/bindUserIdAndToken")
    @ResponseBody
    public Object bindUserIdAndToken(@RequestParam("token") String token ,
                                     @RequestParam("userId") String userId,
                                     @RequestParam(required = false,value = "projId") Integer projId){

        try {
            return ResponseUtil.success(userInfoService.bindUserIdAndToken(userId, token, projId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error(500, e.getMessage());
        }
    }

    /**
     * 用户登录成功页面
     * @param model
     * @param userId
     * @return
     */
    @GetMapping("/indexPage/{userId}")
    public String indexPage(@NotNull Model model, @PathVariable String userId){
        UserTokenDo userTokenDo = userInfoService.selectUserInfoByUserId(userId);
        model.addAttribute("userId",userTokenDo.getUserId());
        model.addAttribute("loginTime",userTokenDo.getLoginTime());
        model.addAttribute("createTime",userTokenDo.getCreateTime());
        return "indexPage";
    }
}
