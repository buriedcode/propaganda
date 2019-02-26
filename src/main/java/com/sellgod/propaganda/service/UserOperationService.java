package com.sellgod.propaganda.service;
import com.sellgod.propaganda.utils.R;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface UserOperationService {

     R saveFile( MultipartFile file);


}
