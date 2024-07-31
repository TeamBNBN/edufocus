package com.edufocus.edufocus.lecture.service;

import com.edufocus.edufocus.lecture.entity.*;
import com.edufocus.edufocus.lecture.repository.LectureRepository;
import com.edufocus.edufocus.registration.entity.Registration;
import com.edufocus.edufocus.registration.entity.RegistrationStatus;
import com.edufocus.edufocus.registration.repository.RegistrationRepository;
import com.edufocus.edufocus.user.model.entity.User;
import com.edufocus.edufocus.user.model.entity.UserRole;
import com.edufocus.edufocus.user.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Builder
@Service
@Transactional
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    private final UserRepository userRepository;

    private final RegistrationRepository registrationRepository;

    @Override
    public void createLecture(long userId, LectureCreateRequest lectureCreateRequest, MultipartFile image) throws IOException {
        User user = userRepository.findById(userId).get();

        Lecture lecture = new Lecture().builder()
                .user(user)
                .title(lectureCreateRequest.getTitle())
                .description(lectureCreateRequest.getDescription())
                .plan(lectureCreateRequest.getPlan())
                .startDate(lectureCreateRequest.getStartDate())
                .endDate(lectureCreateRequest.getEndDate())
                .time(lectureCreateRequest.getTime())
                .build();

        if (image != null && !image.isEmpty()) {
            String uid = UUID.randomUUID().toString();

            String currentPath = "backend/src/main/resources/images/";
            File checkPathFile = new File(currentPath);
            if (!checkPathFile.exists()) {
                checkPathFile.mkdirs();
            }

            File savingImage = new File(currentPath + uid + "_" + image.getOriginalFilename());
            image.transferTo(savingImage.toPath());
            String savePath = savingImage.toPath().toString();

            lecture.setImage(savePath);
        }

        lectureRepository.save(lecture);
    }

    @Override
    public boolean updateLecture(long userId, long lectureId, LectureCreateRequest lectureCreateRequest) {
        Lecture lecture = lectureRepository.findById(lectureId).get();

        if (lecture.getUser().getId() != userId) {
            return false;
        }

        if (lectureCreateRequest.getTitle() != null) {
            lecture.setTitle(lectureCreateRequest.getTitle());
        }
        if (lectureCreateRequest.getDescription() != null) {
            lecture.setDescription(lectureCreateRequest.getDescription());
        }
        if (lectureCreateRequest.getPlan() != null) {
            lecture.setPlan(lectureCreateRequest.getPlan());
        }
        if (lectureCreateRequest.getStartDate() != null) {
            lecture.setStartDate(lectureCreateRequest.getStartDate());
        }
        if (lectureCreateRequest.getEndDate() != null) {
            lecture.setEndDate(lectureCreateRequest.getEndDate());
        }
        if (lectureCreateRequest.getTime() != null) {
            lecture.setTime(lectureCreateRequest.getTime());
        }

        lectureRepository.save(lecture);
        return true;
    }

    @Override
    public boolean deleteLecture(long userId, long lectureId) {
        Optional<Lecture> lecture = lectureRepository.findById(lectureId);

        if (lecture.isEmpty()) {
            return false;
        }
        lecture = Optional.of(lecture.get());

        if (lecture.get().getUser().getId() != userId) {
            return false;
        }

        lectureRepository.deleteById(lectureId);
        return true;
    }

    @Override
    public List<LectureSearchResponse> findAllLecture() {
        List<Lecture> lectureList = lectureRepository.findAll();

        List<LectureSearchResponse> lectureSearchResponseList = new ArrayList<>();
        for (Lecture lecture : lectureList) {
            LectureSearchResponse lectureSearchResponse = LectureSearchResponse.builder()
                    .id(lecture.getId())
                    .title(lecture.getTitle())
                    .image(lecture.getImage()).build();

            lectureSearchResponseList.add(lectureSearchResponse);
        }

        return lectureSearchResponseList;
    }

    @Override
    public LectureDetailResponse findLectureById(Long userId, long lectureId) {
        Optional<Lecture> lecture = lectureRepository.findById(lectureId);
        if (lecture.isEmpty()) {
            return null;
        }

        String userStatus;
        if (userId == null) {
            userStatus = String.valueOf(UserStatus.NOT_ENROLLED);
        } else {
            User user = userRepository.findById(userId).get();

            if (user.getRole() == UserRole.ADMIN) {
                if (lecture.get().getUser().getId() == user.getId()) {
                    userStatus = String.valueOf(UserStatus.MANAGED_BY_ME);
                } else {
                    userStatus = String.valueOf(UserStatus.MANAGED_BY_OTHERS);
                }
            } else {
                Registration registration = registrationRepository.findByUserIdAndLectureId(userId, lectureId);

                if (registration == null) {
                    userStatus = String.valueOf(UserStatus.NOT_ENROLLED);
                } else if (registration.getStatus() == RegistrationStatus.ACCEPTED) {
                    userStatus = String.valueOf(UserStatus.ENROLLED);
                } else {
                    userStatus = String.valueOf(UserStatus.PENDING);
                }
            }
        }

        LectureDetailResponse lectureDetailResponse = new LectureDetailResponse().builder()
                .id(lecture.get().getId())
                .title(lecture.get().getTitle())
                .description(lecture.get().getDescription())
                .plan(lecture.get().getPlan())
                .image(lecture.get().getImage())
                .startDate(lecture.get().getStartDate())
                .endDate(lecture.get().getEndDate())
                .time(lecture.get().getTime())
                .online(lecture.get().isOnline())
                .teacherName(lecture.get().getUser().getName())
                .status(userStatus)
                .build();

        return lectureDetailResponse;
    }

    public List<LectureSearchResponse> findMyLecture(long userId) {
        User user = userRepository.findById(userId).get();

        List<LectureSearchResponse> myLectureList = new ArrayList<>();

        if (user.getRole() == UserRole.ADMIN) {
            List<Lecture> lectureList = lectureRepository.findLecturesByUserId(userId);
            for (Lecture lecture : lectureList) {
                LectureSearchResponse lectureSearchResponse = new LectureSearchResponse().builder()
                        .id(lecture.getId())
                        .title(lecture.getTitle())
                        .image(lecture.getImage()).build();

                myLectureList.add(lectureSearchResponse);
            }
        } else {
            List<Registration> registrationList = registrationRepository.findAllByUserId(userId);

            for (Registration registration : registrationList) {
                Lecture lecture = registration.getLecture();

                LectureSearchResponse lectureSearchResponse = new LectureSearchResponse().builder()
                        .id(lecture.getId())
                        .title(lecture.getTitle())
                        .image(lecture.getImage()).build();

                myLectureList.add(lectureSearchResponse);
            }
        }

        return myLectureList;
    }

    @Override
    public Lecture findLectureByTitle(String title) {
        return lectureRepository.findByTitle(title);
    }

    @Override
    public void changeState(Long id) {

        Optional<Lecture> lecture = lectureRepository.findById(id);

        Lecture l;
        if (lecture.isPresent()) {
            l = lecture.get();

            System.out.println(l.isOnline());
            l.setOnline(true);
            System.out.println(l.isOnline());


        } else {

            throw new RuntimeException("Lecture not found with id: " + id);
        }
        lectureRepository.save(l);
    }

    @Override
    public boolean getState(Long lectureId) {

        Lecture lecture= lectureRepository.findById(lectureId).orElse(null);
        return lecture.isOnline();

    }

    @Override
    public boolean checkTeacher(Long userId, Long lectureId) {
        Optional<Lecture> lecture = lectureRepository.findById(lectureId);

        return lecture.isPresent() && lecture.get().getUser().getId() == userId;
    }

}
