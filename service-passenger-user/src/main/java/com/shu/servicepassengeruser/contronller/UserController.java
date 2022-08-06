package com.shu.servicepassengeruser.contronller;

import com.shu.internalcommon.dto.ResponseResult;
import com.shu.internalcommon.request.VerificationCodeDTO;
import com.shu.servicepassengeruser.service.UserService;
import com.sun.xml.internal.ws.client.ResponseContextReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseResult loginOrRegister(@RequestBody VerificationCodeDTO verificationCodeDTO){

        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("手机号："+passengerPhone);
        return userService.loginOrRegister(passengerPhone);
    }
}
