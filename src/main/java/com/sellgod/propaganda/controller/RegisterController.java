package com.sellgod.propaganda.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sellgod.propaganda.dto.PageDto;
import com.sellgod.propaganda.dto.RegisterDto;
import com.sellgod.propaganda.entity.BanaerDto;
import com.sellgod.propaganda.entity.Imgs;
import com.sellgod.propaganda.entity.UserEntity;
import com.sellgod.propaganda.service.UserOperationService;
import com.sellgod.propaganda.service.UserService;
import com.sellgod.propaganda.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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
           EntityWrapper  entityWrapper  = new EntityWrapper();
           entityWrapper.eq("phone",registerDto.getPhone());
           UserEntity  userEntity = userService.selectOne(entityWrapper);

           if(userEntity!=null){
               return R.error("手机号码已注册");
           }
           userEntity  = new UserEntity();
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
           return  r;
       }

       @GetMapping(value = "/banaer")
       public R getBananer(){
           List<Imgs>   mlist =  new ArrayList<>();
           Imgs  imgs1 = new Imgs();
           imgs1.setImgUrl("http://pn5mg6279.bkt.clouddn.com/1551668884147.jpg");
           Imgs  imgs2 = new Imgs();
           imgs2.setImgUrl("http://pn5mg6279.bkt.clouddn.com/1551844224157.jpg");

           Imgs  imgs3 = new Imgs();
           imgs3.setImgUrl("http://pn5mg6279.bkt.clouddn.com/1551844860321.jpg");
           Imgs  imgs4 = new Imgs();
           imgs4.setImgUrl("http://pn5mg6279.bkt.clouddn.com/1551844860321.jpg");
           Imgs  imgs5 = new Imgs();
           imgs5.setImgUrl("http://pn5mg6279.bkt.clouddn.com/1551844860321.jpg");

           Imgs  imgs6 = new Imgs();
           imgs6.setImgUrl("http://pn5mg6279.bkt.clouddn.com/1551699503459.jpg");
           Imgs  imgs7 = new Imgs();
           imgs7.setImgUrl("http://pn5mg6279.bkt.clouddn.com/1551844860321.jpg");
           Imgs  imgs8 = new Imgs();
           imgs8.setImgUrl("http://pn5mg6279.bkt.clouddn.com/1551699503459.jpg");
           mlist.add(imgs1);
           mlist.add(imgs2);
           mlist.add(imgs3);
           mlist.add(imgs4);
/*           mlist.add(imgs5);
           mlist.add(imgs6);
           mlist.add(imgs7);
           mlist.add(imgs8);*/
           BanaerDto  banaerDto  = new BanaerDto();
           banaerDto.setImgs(mlist);
           return R.withD(mlist);
       }

       @PostMapping(value = "/users")
       private R getUserList(@RequestBody PageDto page){
           return userOperationService.getUsers(page);
       }



}
