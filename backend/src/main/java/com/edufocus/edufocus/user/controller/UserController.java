package com.edufocus.edufocus.user.controller;

import com.edufocus.edufocus.user.model.entity.User;
import com.edufocus.edufocus.user.model.service.UserService;
import com.edufocus.edufocus.user.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody User user) throws Exception {
        userService.join(user);
        return ResponseEntity.ok("User registered successfully");
    }
    @PostMapping("/findpassword/{user_id}")
    public ResponseEntity<String> findpassword(@PathVariable("user_id") Long user_id) throws Exception {

        userService.userCheck(user_id);
        return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");

    }
//
//    @PostMapping("/login")
//    public ResponseEntity<User> login(@RequestBody User user) {
//        try {
//            User loggedInUser = userService.login(user);
//            return ResponseEntity.ok(loggedInUser);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
 //   }

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리.")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody @Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) User user, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");

        if(jwtUtil.checkToken(token)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            User loginUser = userService.login(user);
            if (loginUser != null) {
                String accessToken = jwtUtil.createAccessToken(String.valueOf(loginUser.getId()));
                String refreshToken = jwtUtil.createRefreshToken(String.valueOf(loginUser.getId()));

                // 발급받은 refresh token 을 DB에 저장.
                userService.saveRefreshToken(loginUser.getId(), refreshToken);

                // JSON 으로 token 전달.
                System.out.println(accessToken);
                resultMap.put("access-token", accessToken);
              //  resultMap.put("refresh-token", refreshToken);

                // 쿠키 저장
                Cookie refreshCookie = new Cookie("refresh-token", refreshToken);
                refreshCookie.setPath("/");
                refreshCookie.setHttpOnly(true);
                refreshCookie.setSecure(true); // HTTPS에서만 전송되도록 설정
                // refreshCookie.setSameSite(Cookie.SameSite.NONE); // Cross-Origin 요청에 대해 모두 전송

                response.addCookie(refreshCookie);

                // 쿠키저장
                status = HttpStatus.CREATED;
            } else {
                resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @Operation(summary = "회원인증", description = "회원 정보를 담은 Token 을 반환한다.")
    @GetMapping("/auth/{userId}")
    public ResponseEntity<Map<String, Object>> getInfo(
            @PathVariable("userId") @Parameter(description = "인증할 회원의 아이디.", required = true) Long userId,
            HttpServletRequest request) {
		//logger.debug("userId : {} ", userId);
        String id = String.valueOf(userId);

        System.out.println("!>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(id);
        System.out.println(id.getClass().getName());
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        if (jwtUtil.checkToken(request.getHeader("Authorization"))) {
            log.info("사용 가능한 토큰!!!");
            try {
//				로그인 사용자 정보.
                User member = userService.userInfo(userId);
                resultMap.put("userInfo", member);
                status = HttpStatus.OK;
            } catch (Exception e) {
                log.error("정보조회 실패 : {}", e);
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            System.out.println(jwtUtil.checkToken(request.getHeader("Authorization")));
            log.error("사용 불가능 토큰!!!");
            resultMap.put("message", "Unauthorized token");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @GetMapping("/logout/{userId}")

    public ResponseEntity<?> removeToken(@PathVariable ("userId") @Parameter(description = "로그아웃 할 회원의 아이디.", required = true) Long userId) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
            userService.deleteRefreshToken(userId);
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("로그아웃 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "Access Token 재발급", description = "만료된 access token 을 재발급 받는다.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,HttpServletResponse response)
            throws Exception {

      //  System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
      //  System.out.println(user.getUserId());
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

       // String token = request.getHeader("refreshToken");


        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh-token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        Long userId = Long.parseLong(jwtUtil.getUserId(token));
    ///    System.out.println(userId);
      ///  System.out.println("리프레쉬 토큰 (쿠키에서 받은거 "+token);
      //  log.debug("token : {}, memberDto : {}", token, user);
       // System.out.println(jwtUtil.checkToken(token));
        if (jwtUtil.checkToken(token)) {

           // System.out.println(token);
           // System.out.println(userService.getRefreshToken(userId));
            //System.out.println("쿠키 토큰 , 디비 토큰 비교"+token.equals(userService.getRefreshToken(userId)));
            if (token.equals(userService.getRefreshToken(userId))) {
                System.out.println("!!");
                String accessToken = jwtUtil.createAccessToken(String.valueOf(userId));
                String refreshToken = jwtUtil.createRefreshToken(String.valueOf(userId));

                log.debug("token : {}", accessToken);
                log.debug("정상적으로 access token 재발급!!!");
                resultMap.put("access-token", accessToken);


                // 바뀐 리프레시db저장
                userService.saveRefreshToken(userId,refreshToken);
                // 쿠키 저장
                Cookie refreshCookie = new Cookie("refresh-token", refreshToken);
                refreshCookie.setPath("/");
                refreshCookie.setHttpOnly(true);
                refreshCookie.setSecure(true); // HTTPS에서만 전송되도록 설정
                // refreshCookie.setSameSite(Cookie.SameSite.NONE); // Cross-Origin 요청에 대해 모두 전송

                response.addCookie(refreshCookie);
                System.out.println("바뀐 리프레쉬랑 지금꺼 비교 "+ refreshToken.equals(token));
                resultMap.put("access-token", accessToken);
                status = HttpStatus.CREATED;

            }
        } else {
            log.debug("refresh token 도 사용 불가!!!!!!!");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

    @Operation(summary = "회원 정보 조회", description = "토큰을 이용하여 회원 정보를 조회한다.")
    @GetMapping("/userinfo")
    public ResponseEntity<Map<String, Object>> getMember(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        String token = request.getHeader("Authorization");

        if (jwtUtil.checkToken(token)) {
            String userId = jwtUtil.getUserId(token);
            log.info("사용 가능한 토큰!!! userId: {}", userId);
            try {
                User user = userService.userInfo(Long.parseLong(userId));
                resultMap.put("userInfo", user);
                status = HttpStatus.OK;

            } catch (Exception e) {
                log.error("정보조회 실패 : {}", e);
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            log.error("사용 불가능 토큰!!!");
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }






}
