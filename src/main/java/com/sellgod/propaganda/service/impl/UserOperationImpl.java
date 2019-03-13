package com.sellgod.propaganda.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sellgod.propaganda.dto.PageDto;
import com.sellgod.propaganda.dto.UserListDto;
import com.sellgod.propaganda.entity.FileEntity;
import com.sellgod.propaganda.entity.UserEntity;
import com.sellgod.propaganda.service.FileService;
import com.sellgod.propaganda.service.UserOperationService;
import com.sellgod.propaganda.service.UserService;
import com.sellgod.propaganda.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserOperationImpl implements UserOperationService {

    @Autowired
    private FileService   fileService;

    @Value("${qiniu.AK}")
    private String ACCESS_KEY;
    @Value("${qiniu.SK}")
    private String SECRET_KEY;

    @Value("${qiniu.bucketname}")
    private String bucketname;

    @Value("${qiniu.URL}")
    private String url;

    @Autowired
    private UserService userService;

    @Override
    public R saveFile(MultipartFile file) {
        try {
            FileInputStream inputStream = (FileInputStream) file.getInputStream();
            String fileName = file.getOriginalFilename();
            String fileNameExtension = fileName.substring(fileName.lastIndexOf("."), fileName.length());
            String realName = String.valueOf(System.currentTimeMillis())   + fileNameExtension;
            String picUrl = uploadQNImg(inputStream,realName);

            FileEntity  fileEntity  = new FileEntity();
            fileEntity.setUrl(picUrl);
            if(fileService.insert(fileEntity)){
                return R.withD(fileEntity);
            }
            return R.error("图片添加失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    @Override
    public R getUsers(PageDto pageDto) {
        List<UserListDto>  userListDtos  = new ArrayList<>();
        /*List<UserEntity> userEntityList  = userService.selectList(new EntityWrapper<>());*/
        EntityWrapper  ew = new EntityWrapper();
        Page<UserEntity> page = userService.selectPage(new Page<UserEntity>(pageDto.getCurrentPage(), pageDto.getPageSize()),ew);
        List<UserEntity> userEntityList = page.getRecords();
        for(int i=0;i<userEntityList.size();i++){
            UserListDto  userListDto  = new UserListDto();
            FileEntity  fileEntity  = fileService.selectById(userEntityList.get(i).getPicId());
            userListDto.setImg(fileEntity.getUrl());
            userListDto.setInfo(userEntityList.get(i).getInfo());
            userListDto.setName(userEntityList.get(i).getUserName());
            userListDtos.add(userListDto);
        }
        return R.withD(userListDtos);
    }


    private String uploadQNImg(FileInputStream file, String key) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传

        try {
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(bucketname);
            try {
                Response response = uploadManager.put(file, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                String returnPath = url + "/" + putRet.key;
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
