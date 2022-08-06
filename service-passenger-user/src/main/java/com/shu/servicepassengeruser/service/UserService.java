package com.shu.servicepassengeruser.service;

import com.shu.internalcommon.dto.ResponseResult;
import com.shu.servicepassengeruser.dto.PassengerUser;
import com.shu.servicepassengeruser.mapper.PassengerUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private PassengerUserMapper passengerUserMapper;

    public ResponseResult loginOrRegister(String passengerPhone){

        System.out.println("userservice被调用，手机号："+passengerPhone);
        //根据手机号查询用户信息
        Map<String,Object> map = new HashMap<>();
        map.put("passenger_phone",passengerPhone);
        List<PassengerUser> passengerUsers = passengerUserMapper.selectByMap(map);
        //判断用户信息是否存在
        if(passengerUsers.size()==0){
            //如果不存在，插入用户信息
            PassengerUser passengerUser = new PassengerUser();
            passengerUser.setPassengerName("张三");
            passengerUser.setPassengerGender((byte)0);
            passengerUser.setPassengerPhone(passengerPhone);
            passengerUser.setState((byte)0);

            LocalDateTime now = LocalDateTime.now();
            passengerUser.setGmtCreate(now);
            passengerUser.setGmtModified(now);

            passengerUserMapper.insert(passengerUser);
        }

        return ResponseResult.success();
    }
}


