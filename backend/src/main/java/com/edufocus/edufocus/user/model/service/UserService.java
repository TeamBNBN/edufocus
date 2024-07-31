package com.edufocus.edufocus.user.model.service;

import com.edufocus.edufocus.user.model.entity.InfoDto;
import com.edufocus.edufocus.user.model.entity.PasswordDto;
import com.edufocus.edufocus.user.model.entity.User;

public interface UserService {
     void join(User user) throws Exception;
     User login(User user) throws Exception;
      void saveRefreshToken(Long id, String refreshToken) throws Exception;
      String getRefreshToken(Long id) throws Exception;
      void deleteRefreshToken(Long id) throws Exception;
       User userInfo(Long id) throws Exception;
    void sendEamail(User user) throws Exception;
     void userCheck(Long id) throws  Exception;
     String getUserName(Long id) throws Exception;
    void changeuInfo(InfoDto infoDto,Long id) throws Exception;
    void changePassword(PasswordDto passwordDto,Long id) throws Exception;

}
