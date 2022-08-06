package com.shu.apipassenger.service;

import com.shu.apipassenger.remote.ServicePassengerUserClient;
import com.shu.apipassenger.remote.ServiceVerificationcodeClient;
import com.shu.internalcommon.constant.CommonStatusEnum;
import com.shu.internalcommon.constant.IdentityConstant;
import com.shu.internalcommon.dto.ResponseResult;
import com.shu.internalcommon.request.VerificationCodeDTO;
import com.shu.internalcommon.response.NumberCodeResponse;
import com.shu.internalcommon.response.TokenResponse;
import com.shu.internalcommon.util.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    //乘客验证码前缀
    private String verificationCodePrefix = "passenger-verification-code-";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据手机号获取key
     * @param passengerPhone
     * @return
     */
    private String generatorKeyByPhone(String passengerPhone){
        return verificationCodePrefix + passengerPhone;
    }

    @Autowired
    private ServicePassengerUserClient servicePassengerUserClient;
    /**
     *生成验证码
     * @param passengerPhone 手机号
     * @return
     */
    public ResponseResult generatorCode(String passengerPhone){
        //调用验证码服务，获取验证码
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numbercode = numberCodeResponse.getData().getNumberCode();

        //key,value,过期时间
        String key = generatorKeyByPhone(passengerPhone);
        //存入Redis
        stringRedisTemplate.opsForValue().set(key, String.valueOf(numbercode),120,TimeUnit.MINUTES);


        //返回值
        return ResponseResult.success("");
    }

    /**
     * 校验验证码
     * @param passengerPhone 手机号
     * @param verificationCode 验证码
     * @return
     */
    public ResponseResult checkCode(String passengerPhone, String verificationCode){
        //根据手机号，去Redis读取验证码
        System.out.println("根据手机号，去Redis读取验证码");
        //生成key
        String key = generatorKeyByPhone(passengerPhone);
        //根据key获取value
        String codeRedis = stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis中的value："+codeRedis);

        //校验验证码
        if (StringUtils.isBlank(codeRedis)){
            return ResponseResult.fail(CommonStatusEnum.verification_code_error.getCode(),CommonStatusEnum.verification_code_error.getValue());
        }
        if (!verificationCode.trim().equals(codeRedis.trim())){
            return ResponseResult.fail(CommonStatusEnum.verification_code_error.getCode(),CommonStatusEnum.verification_code_error.getValue());
        }

        //判断原来是否有用户，并进行相应的处理
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setPassengerPhone(passengerPhone);
        servicePassengerUserClient.loginOrRegister(verificationCodeDTO);

        //颁发令牌
        String token = JwtUtils.generatorToken(passengerPhone,IdentityConstant.PASSENGER_IDENTITY);

        //响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);
        return ResponseResult.success(tokenResponse);
    }
}
