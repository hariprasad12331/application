package com.hari.application.useradministration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hari.application.useradministration.data.CreateUserPayload;
import com.hari.application.useradministration.domain.AppUser;
import com.hari.application.useradministration.repository.AppUserRepository;
import com.hari.application.useradministration.service.UserAdministrationWritePlatformService;

@RestController
@RequestMapping(value="/api/users")
public class UserAdministrationController {

    @Autowired
    private UserAdministrationWritePlatformService userAdministrationWritePlatformService;

    @Autowired
    private AppUserRepository appUserRepository;

    @RequestMapping(method = {RequestMethod.POST})
    public String generatePublicKey(@RequestBody CreateUserPayload createUserPayload){
    
        return userAdministrationWritePlatformService.createuser(createUserPayload);
    }
    
    @RequestMapping(method = {RequestMethod.GET})
    //use @PreAuthorize("GET_ALL_USERS") for permissions
    public List<AppUser> getAllUsers(){
    
        return this.appUserRepository.findAll();
    }

    @RequestMapping(value="/{id}", method = {RequestMethod.GET})
    //use @PreAuthorize("GET_ONE_USERS") for permissions
    public AppUser getuser(@PathVariable Long id){
        //write a read service and return only some data
        return this.appUserRepository.findById(id).get();
    }
    
}
