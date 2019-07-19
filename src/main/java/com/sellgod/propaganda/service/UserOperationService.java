package com.sellgod.propaganda.service;
import com.sellgod.propaganda.dto.PageDto;
import com.sellgod.propaganda.utils.R;
import org.springframework.web.multipart.MultipartFile;


public interface UserOperationService {

     R saveFile( MultipartFile file);

     R getUsers(PageDto page);

     String getOpenId(String code) throws Exception;


}
