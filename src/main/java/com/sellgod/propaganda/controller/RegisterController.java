package com.sellgod.propaganda.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sellgod.propaganda.dto.PageDto;
import com.sellgod.propaganda.dto.RegisterDto;
import com.sellgod.propaganda.entity.BanaerDto;
import com.sellgod.propaganda.entity.FileEntity;
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
           return userOperationService.saveFile(file);
       }

    @GetMapping(value = "/wxLogin")
    public R wxLogin(@RequestParam String code) throws Exception{
        return R.withD(userOperationService.getOpenId(code));
    }

       @GetMapping(value = "/banaer")
       public R getBananer(){
           List<Imgs>   mlist =  new ArrayList<>();
           Imgs  imgs1 = new Imgs();
           imgs1.setImgUrl("http://pic37.nipic.com/20140113/8800276_184927469000_2.png");
           Imgs  imgs2 = new Imgs();
           imgs2.setImgUrl("http://pic1.win4000.com/wallpaper/c/53cdd1f7c1f21.jpg");

           Imgs  imgs3 = new Imgs();
           imgs3.setImgUrl("http://pic36.nipic.com/20131129/8821914_111419739001_2.jpg");
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
           //mlist.add(imgs4);
/*           mlist.add(imgs5);
           mlist.add(imgs6);
           mlist.add(imgs7);
           mlist.add(imgs8);*/
           BanaerDto  banaerDto  = new BanaerDto();
           banaerDto.setImgs(mlist);
           return R.withD(mlist);
       }

       @GetMapping(value = "/list")
       public R getDataList(){
           List<FileEntity>   fileEntityList = new ArrayList<>();
           FileEntity   fileEntity  = new FileEntity();
           fileEntity.setUrl("https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=ab436e58aeec08fa390015a769ef3d4d/b17eca8065380cd7cd63680aaf44ad34588281dc.jpg");
           fileEntity.setWidth(450);
           fileEntity.setHeight(300);


           FileEntity   fileEntity1  = new FileEntity();
           fileEntity1.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563432007161&di=c642da2e39f6d1b469717ccd71e4319d&imgtype=0&src=http%3A%2F%2Fimg5.makepolo.net%2Fimages%2Fformals%2Fimg%2Fproduct%2F11%2F660%2F2eeb1a04f49e662c0ff6b956424f0de8.jpg");
           fileEntity1.setWidth(1000);
           fileEntity1.setHeight(901);


           FileEntity   fileEntity2  = new FileEntity();
           fileEntity2.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563432103778&di=9601aa41b546319ba4df6cdf93fc9071&imgtype=jpg&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20190122%2Ff24ab1f776974b41bb83cbd86353f702.jpeg");
           fileEntity2.setWidth(640);
           fileEntity2.setHeight(360);


           FileEntity   fileEntity3  = new FileEntity();
           fileEntity3.setUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563432167791&di=bd6ec1b0a30cf0b20847e55f9606d0b7&imgtype=0&src=http%3A%2F%2Fphotocdn.sohu.com%2F20131029%2FImg389137955.jpg");
           fileEntity3.setWidth(330);
           fileEntity3.setHeight(498);

           fileEntityList.add(fileEntity);
           fileEntityList.add(fileEntity1);
           fileEntityList.add(fileEntity2);
           fileEntityList.add(fileEntity3);
           return R.withD(fileEntityList);
       }

       @PostMapping(value = "/users")
       public R getList(@RequestBody PageDto page){
           return userOperationService.getUsers(page);
       }



}
