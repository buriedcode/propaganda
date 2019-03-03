package com.sellgod.propaganda.controller;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sellgod.propaganda.dto.RegisterDto;
import com.sellgod.propaganda.entity.UserEntity;
import com.sellgod.propaganda.service.UserOperationService;
import com.sellgod.propaganda.service.UserService;
import com.sellgod.propaganda.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/user")
@Slf4j
public class RegisterController {



    @Autowired
    private UserOperationService  userOperationService;
    @Autowired
    private UserService   userService;
       @PostMapping(value = "/register")
       public R register(@RequestBody RegisterDto  registerDto){
           System.out.println("111111");
           UserEntity  userEntity  = new UserEntity();
           BeanUtils.copyProperties(registerDto,userEntity);
           if(userService.insert(userEntity)) {
               return R.ok();
           }else{
               return R.error();
           }
       }
       @PostMapping(value = "/fileUpload")
       public R upload(@RequestParam(value = "file") MultipartFile file) {
           R r = userOperationService.saveFile(file);
           System.out.println(""+r.toString());
           return  r;
       }



}
