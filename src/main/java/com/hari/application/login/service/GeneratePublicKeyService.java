package com.hari.application.login.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import com.hari.application.login.data.GeneratePublicKeyPayload;

@Component
public class GeneratePublicKeyService{

    @Autowired
    RedisCacheManager redisCacheManager;


    public String generatePublicKey(GeneratePublicKeyPayload generatePublicKeyPayload) {
        //to do check if user is already present and return public key
        // move to diff  service
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            final KeyPair keyPair = keyPairGenerator.generateKeyPair();
            final PublicKey publicKey = keyPair.getPublic();
            final PrivateKey privateKey = keyPair.getPrivate();
            final byte[] publicKeyAsBytes = publicKey.getEncoded();
            final byte[] privateKeyAsBytes = privateKey.getEncoded();
            redisCacheManager.getCache("CryptoGrpahy").put(generatePublicKeyPayload.getUserName()+"public", publicKeyAsBytes);
            redisCacheManager.getCache("CryptoGrpahy").put(generatePublicKeyPayload.getUserName()+"private", privateKeyAsBytes);
            return publicKey.toString();

            
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
}
