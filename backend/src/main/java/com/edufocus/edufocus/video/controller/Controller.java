package com.edufocus.edufocus.video.controller;

import java.util.Map;
import java.util.Random;

import com.edufocus.edufocus.lecture.entity.Lecture;
import com.edufocus.edufocus.lecture.entity.LectureDetailResponse;
import com.edufocus.edufocus.lecture.repository.LectureRepository;
import com.edufocus.edufocus.lecture.service.LectureService;
import com.edufocus.edufocus.registration.entity.RegistrationStatus;
import com.edufocus.edufocus.registration.service.RegistrationService;
import com.edufocus.edufocus.user.model.entity.User;
import com.edufocus.edufocus.user.model.entity.UserRole;
import com.edufocus.edufocus.user.model.repository.UserRepository;
import com.edufocus.edufocus.user.model.service.UserService;
import com.edufocus.edufocus.user.util.JWTUtil;
import com.edufocus.edufocus.video.service.VideoSertvice;
import io.livekit.server.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import livekit.LivekitWebhook.WebhookEvent;


@RestController
@RequestMapping("/video")
@Slf4j
@RequiredArgsConstructor

public class Controller {
	private final JWTUtil jwtUtil;
	private final UserService userService;
	private final LectureService lectureService;
	private final VideoSertvice videoSertvice;
	private final RegistrationService registrationService;
	private final UserRepository userRepository;
	private final LectureRepository lectureRepository;
	@Value("${livekit.api.key}")
	private String LIVEKIT_API_KEY;

	@Value("${livekit.api.secret}")
	private String LIVEKIT_API_SECRET;

	/**
	 * @param params JSON object with roomName and participantName
	 * @return JSON object with the JWT token
	 */
//	@PostMapping(value = "/token")
//	public ResponseEntity<Map<String, String>> createToken(@RequestBody Map<String, String> params) {
//		String roomName = params.get("roomName");
//		String participantName = params.get("participantName");
//
//		if (roomName == null || participantName == null) {
//			return ResponseEntity.badRequest().body(Map.of("errorMessage", "roomName and participantName are required"));
//		}
//
//		AccessToken token = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
//		token.setName(participantName);
//		token.setIdentity(participantName);
//		token.addGrants(new RoomJoin(true), new RoomName(roomName));
//
//
//
//		return ResponseEntity.ok(Map.of("token", token.toJwt()));
//	}

	@PostMapping(value = "/makeroom/{lecture_id}")
	public ResponseEntity<String> startLecture(@PathVariable("lecture_id") Long id, HttpServletRequest request) throws Exception {


		String userToken = request.getHeader("Authorization");
		Long userId = Long.parseLong(jwtUtil.getUserId(userToken));


		videoSertvice.startOnline(userId, id);

		return new ResponseEntity<>("방만들기 성공",HttpStatus.OK);

	}


	@PostMapping(value = "/joinroom/{lecture_id}")
	public ResponseEntity<Map<String, String>> joinRoom(@PathVariable("lecture_id") Long id, HttpServletRequest request) throws Exception
	{

		String userToken = request.getHeader("Authorization");


		Long userId = Long.parseLong(jwtUtil.getUserId(userToken));
		User findUser= userRepository.findById(userId).orElse(null);
		Lecture lecture= lectureRepository.findById(id).orElse(null);


		Random random = new Random();


		int randomNumber = 100 + random.nextInt(9000);

		String randStr = String.valueOf(randomNumber);


		if(findUser.getRole()==UserRole.ADMIN ){//&& lecture.isOnline() ) {



			String roomName = lecture.getTitle();
			String participantName = userService.getUserName(userId);


			AccessToken token = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
			token.setName(participantName+randStr);
			token.setIdentity(participantName+randStr);
			token.addGrants(new RoomJoin(true), new RoomName(roomName), new RoomCreate(true));

			videoSertvice.startOnline(userId, id);


			return ResponseEntity.ok(Map.of("token", token.toJwt()));

		}
		else if(findUser.getRole()==UserRole.STUDENT )// && lecture.isOnline() )
		{


			String roomName = lecture.getTitle();
			String participantName = userService.getUserName(userId);


			AccessToken token = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
			token.setName(participantName+randStr);
			token.setIdentity(participantName+randStr);
			token.addGrants(new RoomJoin(true), new RoomName(roomName));


			videoSertvice.startOnline(userId, id);


			return ResponseEntity.ok(Map.of("token", token.toJwt()));
		}


		return ResponseEntity.ok(Map.of("token", null));

//		String userToken = request.getHeader("Authorization");
//
//		Long userId = Long.parseLong(jwtUtil.getUserId(userToken));
//		LectureDetailResponse lecture= lectureService.findLectureById(userId,id);
//
//
//		//RegistrationStatus registrationStatus = registrationService.isOnline(userId,id);
//
//		if(registrationStatus==RegistrationStatus.ACCEPTED)
//		{
//			String roomName = lecture.getTitle();
//			String participantName = userService.getUserName(userId);
//
//
//			AccessToken token = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
//			token.setName(participantName);
//			token.setIdentity(participantName);
//			token.addGrants(new RoomJoin(true), new RoomName(roomName));
//
//			//videoSertvice.startOnline(userId,id);
//
//
//
//			return ResponseEntity.ok(Map.of("token", token.toJwt()));
//		}
//		else{
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("errorMessage", "Not accepted"));
//
//		}



	}

	@PostMapping(value = "/livekit/webhook", consumes = "application/webhook+json")
	public ResponseEntity<String> receiveWebhook(@RequestHeader("Authorization") String authHeader, @RequestBody String body) {
		WebhookReceiver webhookReceiver = new WebhookReceiver(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
		try {
			WebhookEvent event = webhookReceiver.receive(body, authHeader);
			System.out.println("LiveKit Webhook: " + event.toString());
		} catch (Exception e) {
			System.err.println("Error validating webhook event: " + e.getMessage());
		}
		return ResponseEntity.ok("ok");
	}

	

}
