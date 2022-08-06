package com.shu.servicepassengeruser.contronller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TsetController {

    @GetMapping()
    public String test(){
        return "service-passenger-user";
    }

}
