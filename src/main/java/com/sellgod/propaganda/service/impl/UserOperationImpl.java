package com.sellgod.propaganda.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sellgod.propaganda.dto.PageDto;
import com.sellgod.propaganda.dto.UserInfoDto;
import com.sellgod.propaganda.dto.UserListDto;
import com.sellgod.propaganda.entity.FileEntity;
import com.sellgod.propaganda.entity.UserEntity;
import com.sellgod.propaganda.service.FileService;
import com.sellgod.propaganda.service.UserOperationService;
import com.sellgod.propaganda.service.UserService;
import com.sellgod.propaganda.utils.R;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.security.auth.message.AuthException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Value("${app.wx.appid}")
    private String appid;

    @Value("${app.wx.secret}")
    private String secret;

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

            int width = getImgWidth(file);
            int height = getImgHeight(file);
            FileEntity  fileEntity  = new FileEntity();
            fileEntity.setUrl(picUrl);
            fileEntity.setHeight(height);
            fileEntity.setWidth(width);
            if(fileService.insert(fileEntity)){
                return R.withD(fileEntity);
            }
            return R.error("图片添加失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok();
    }

    public  int getImgWidth(MultipartFile fileInputStream) {
        BufferedImage src = null;
        int ret = -1;
        try {
            //is = new FileInputStream(file);
            src = ImageIO.read(fileInputStream.getInputStream());
            ret = src.getWidth(null); // 得到源图宽
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 获取图片高度
     * @param file  图片文件
     * @return 高度
     */
    public  int getImgHeight(MultipartFile file) {
        BufferedImage src = null;
        int ret = -1;
        try {

            src = ImageIO.read(file.getInputStream());
            ret = src.getHeight(null); // 得到源图高
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public R getUsers(PageDto pageDto) {
        List<UserListDto>  userListDtos  = new ArrayList<>();
        /*List<UserEntity> userEntityList  = userService.selectList(new EntityWrapper<>());*/
        EntityWrapper  ew = new EntityWrapper();
        Page<UserEntity> page = userService.selectPage(new Page<UserEntity>(pageDto.getCurrentPage(), pageDto.getPageSize()),ew);
        List<UserEntity> userEntityList = page.getRecords();
        List<UserInfoDto>  userInfoDtoList  = new ArrayList<>();
        for(int i=0;i<userEntityList.size();i++) {
            UserInfoDto userInfoDto = new UserInfoDto();
            FileEntity fileEntity = fileService.selectById(userEntityList.get(i).getPicId());
            userInfoDto.setCount(userEntityList.get(i).getCount());
            userInfoDto.setHeight(fileEntity.getHeight());
            userInfoDto.setWidth(fileEntity.getWidth());
            userInfoDto.setName(userEntityList.get(i).getUserName());
            userInfoDto.setUrl(fileEntity.getUrl());
            userInfoDtoList.add(userInfoDto);
        }
        return R.withD(userInfoDtoList);
    }

    @Override
    public String getOpenId(String code) throws Exception {
        String wxUrl = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", appid, secret, code);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse = httpClient.execute(new HttpGet(wxUrl));
        int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
        HttpEntity closeableHttpResponseEntity = closeableHttpResponse.getEntity();
        String result = EntityUtils.toString(closeableHttpResponseEntity, StandardCharsets.UTF_8);
        closeableHttpResponse.close();
        String openId = "";
        if (statusCode == org.apache.http.HttpStatus.SC_OK) {
            ObjectMapper obj = new ObjectMapper();
            Map mapType = new HashMap<>();
            try {
                mapType = obj.readValue(result, Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mapType.containsKey("openid")) {
                openId = mapType.get("openid").toString();

            } else {
                throw new AuthException(mapType.get("errmsg").toString());
            }
        } else {
            throw new Exception(result);
        }
        return openId;
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
