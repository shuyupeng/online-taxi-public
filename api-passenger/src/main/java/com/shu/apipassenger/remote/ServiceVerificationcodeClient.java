package com.shu.apipassenger.remote;

import com.shu.internalcommon.dto.ResponseResult;
import com.shu.internalcommon.response.NumberCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-verificationcode")
public interface ServiceVerificationcodeClient {

    @RequestMapping(method = RequestMethod.GET, value = "/numberCode/{size}")
    abstract ResponseResult<NumberCodeResponse> getNumberCode(@PathVariable("size") int size);
}
