package com.hari.application.useradministration.service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;
import javax.security.auth.DestroyFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hari.application.useradministration.data.CreateUserPayload;
import com.hari.application.useradministration.domain.AppUser;
import com.hari.application.useradministration.repository.AppUserRepository;

@Component
public class UserAdministrationWritePlatformService {
    private RedisCacheManager redisCacheManager;
    private AppUserRepository appUserRepository;

    @Autowired
    public UserAdministrationWritePlatformService(RedisCacheManager redisCacheManager,
            AppUserRepository appUserRepository) {
        this.redisCacheManager = redisCacheManager;
        this.appUserRepository = appUserRepository;
    }

    public String createuser(CreateUserPayload createUserPayload) {
        //move this to validator class
        validate(createUserPayload);
        String password = createUserPayload.getPassword();
        byte[] privateKeyAssoiatedWithUser = this.redisCacheManager.getCache("CryptoGraphy")
                .get(createUserPayload.getUsername(), byte[].class);
        String unecodedPassword = null;
        try {
            unecodedPassword = decryptEncryptedTextUsingRSAPrivateKey(password, privateKeyAssoiatedWithUser);
        } catch (DestroyFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // make this singleton
        ApplicationPasswordEncoder applicationPasswordEncoder = new ApplicationPasswordEncoder();
        String encodedPassword = applicationPasswordEncoder.encode(unecodedPassword);

        AppUser appUser = AppUser.builder().username(createUserPayload.getUsername()).password(encodedPassword)
                .email(createUserPayload.getEmail()).firstname(createUserPayload.getFirstName())
                .lastname(createUserPayload.getLastName()).mobileNumber(createUserPayload.getMobileNumber())
                .accountNonExpired(true).accountNonLocked(true).build();
        appUserRepository.save(appUser);
        return appUser.getId().toString();
    }

    private void validate(CreateUserPayload createUserPayload) {
        //validate username
    }

    // move to diff service
    private String decryptEncryptedTextUsingRSAPrivateKey(String encryptedText, final byte[] privateKeyData)
            throws DestroyFailedException {
        String decryptedText = null;
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyData));
            final Cipher decryptKey = Cipher.getInstance("RSA/None/OAEPWithSHA-256AndMGF1Padding");
            decryptKey.init(Cipher.DECRYPT_MODE, privateKey);
            final byte[] decryptByte = decryptKey.doFinal(encryptedText.getBytes());
            decryptedText = new String(decryptByte);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return decryptedText;
    }

}
