package com.hari.application.login.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hari.application.login.data.GeneratePublicKeyPayload;
import com.hari.application.login.service.GeneratePublicKeyService;

@RestController
public class GeneratePublicKeyController {

    private GeneratePublicKeyService generatePublicKeyService;

    @Autowired
    public GeneratePublicKeyController( GeneratePublicKeyService generatePublicKeyService){
        this.generatePublicKeyService = generatePublicKeyService;
    }

    @RequestMapping(value = "/api/generate-public-key",method = {RequestMethod.POST})
    public String generatePublicKey(@RequestBody GeneratePublicKeyPayload generatePublicKeyPayload){
        String publicKey =  this.generatePublicKeyService.generatePublicKey(generatePublicKeyPayload);
        return null;
    }

    
}
