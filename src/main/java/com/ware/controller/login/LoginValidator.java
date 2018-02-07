package com.ware.controller.login;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class LoginValidator extends Validator {
    @Override
    protected void validate(Controller c) {

        setShortCircuit(true);

        validateRequired("userName","userNameMsg","请输入用户名");
        validateRequired("password","userPwdMsg","请输入密码");
    }

    @Override
    protected void handleError(Controller c) {
        c.renderJson();
    }
}
