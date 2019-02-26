package com.sellgod.propaganda.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.sellgod.propaganda.entity.FileEntity;
import com.sellgod.propaganda.service.FileService;
import com.sellgod.propaganda.service.UserOperationService;
import com.sellgod.propaganda.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
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
                Long id = fileEntity.getId();
                return R.withD(id);
            }
            return R.error("图片添加失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.ok();
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
