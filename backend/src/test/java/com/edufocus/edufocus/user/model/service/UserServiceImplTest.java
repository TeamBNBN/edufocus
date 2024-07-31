package com.edufocus.edufocus.user.model.service;

import com.edufocus.edufocus.user.model.entity.PasswordDto;
import com.edufocus.edufocus.user.model.entity.User;
import com.edufocus.edufocus.user.model.entity.UserRole;
import com.edufocus.edufocus.user.model.exception.UserException;
import com.edufocus.edufocus.user.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testJoin() {
        User user = new User();
        user.setUserId("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole(UserRole.STUDENT);
        user.setId(1L);


        userService.join(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testLogin_Success() throws Exception {
        User user = new User();
        user.setUserId("testUser");
        user.setPassword("password");

        when(userRepository.findByUserId("testUser")).thenReturn(Optional.of(user));

        User loggedInUser = userService.login(user);

        assertNotNull(loggedInUser);
        assertEquals("testUser", loggedInUser.getUserId());
    }

    @Test
    public void testLogin_UserNotFound() {
        User user = new User();
        user.setUserId("testUser");
        user.setPassword("password");

        when(userRepository.findByUserId("testUser")).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> {
            userService.login(user);
        });
    }

    @Test
    public void testLogin_InvalidPassword() {
        User user = new User();
        user.setUserId("testUser");
        user.setPassword("password");

        User storedUser = new User();
        storedUser.setUserId("testUser");
        storedUser.setPassword("wrongPassword");

        when(userRepository.findByUserId("testUser")).thenReturn(Optional.of(storedUser));

        assertThrows(UserException.class, () -> {
            userService.login(user);
        });
    }

//    @Test
//    public void testUserInfo_Success() {
//        User user = new User();
//        user.setId(1L);
//        user.setName("testUser");
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        User userInfo = userService.userInfo(1L);
//
//        assertNotNull(userInfo);
//        assertEquals("testUser", userInfo.getName());
//    }
//
//    @Test
//    public void testUserInfo_UserNotFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(UserException.class, () -> {
//            userService.userInfo(1L);
//        });
//    }
//
//    @Test
//    public void testChangePassword_Success() throws Exception {
//        User user = new User();
//        user.setId(1L);
//        user.setPassword("currentPassword");
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        PasswordDto passwordDto = new PasswordDto();
//        passwordDto.setCurrentPassword("currentPassword");
//        passwordDto.setNewPassword("newPassword");
//        passwordDto.setNewPasswordCheck("newPassword");
//
//        userService.changePassword(passwordDto, 1L);
//
//        verify(userRepository, times(1)).save(user);
//        assertEquals("newPassword", user.getPassword());
//    }
//
//    @Test
//    public void testChangePassword_UserNotFound() {
//        when(userRepository.findById(1L)).thenReturn(Optional.empty());
//
//        PasswordDto passwordDto = new PasswordDto();
//        passwordDto.setCurrentPassword("currentPassword");
//        passwordDto.setNewPassword("newPassword");
//        passwordDto.setNewPasswordCheck("newPassword");
//
//        assertThrows(Exception.class, () -> {
//            userService.changePassword(passwordDto, 1L);
//        });
//    }
//
//    @Test
//    public void testChangePassword_InvalidCurrentPassword() {
//        User user = new User();
//        user.setId(1L);
//        user.setPassword("currentPassword");
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        PasswordDto passwordDto = new PasswordDto();
//        passwordDto.setCurrentPassword("wrongPassword");
//        passwordDto.setNewPassword("newPassword");
//        passwordDto.setNewPasswordCheck("newPassword");
//
//        assertThrows(Exception.class, () -> {
//            userService.changePassword(passwordDto, 1L);
//        });
//    }
//
//    @Test
//    public void testChangePassword_NewPasswordMismatch() {
//        User user = new User();
//        user.setId(1L);
//        user.setPassword("currentPassword");
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        PasswordDto passwordDto = new PasswordDto();
//        passwordDto.setCurrentPassword("currentPassword");
//        passwordDto.setNewPassword("newPassword");
//        passwordDto.setNewPasswordCheck("mismatchNewPassword");
//
//        assertThrows(Exception.class, () -> {
//            userService.changePassword(passwordDto, 1L);
//        });
//    }
}