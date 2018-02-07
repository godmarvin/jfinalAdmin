package com.ware.controller.login;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;

public class LoginController extends Controller {

    static final LoginService srv = LoginService.me;

    public void index(){

        render("index.html");
    }

    @Before(LoginValidator.class)
    public void doLogin(){


        Ret ret =srv.login(getPara("userName"),getPara("password"));
        if(ret.isOk()){

      //  render("homePage.html");

            ret.set("returnUrl", "/");    // 如果 returnUrl 存在则跳过去，否则跳去首页
        }

        renderJson(ret);

    }
}
