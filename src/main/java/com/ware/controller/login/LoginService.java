package com.ware.controller.login;

import com.jfinal.kit.Ret;
import com.ware.model.UserInfo;

public class LoginService {

    public static final LoginService me = new LoginService();
    public static final String sessionIdName = "jfinalId";

    private final UserInfo userDao = new UserInfo().dao();


    public Ret login(String userName,String password){

        UserInfo userInfo = userDao.findFirst("select * from system_user where user_name=? limit 1",userName.trim());

        if(userInfo == null){
            return Ret.fail("msg","用户不存在");
        }

        if(password.trim().equals(userInfo.getPassword()) == false){

            return Ret.fail("msg","用户名或者密码不正确");
        }

        return Ret.ok();
    }
}
