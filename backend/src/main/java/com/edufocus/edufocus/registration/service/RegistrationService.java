package com.edufocus.edufocus.registration.service;

import com.edufocus.edufocus.registration.entity.Registration;
import com.edufocus.edufocus.registration.entity.RegistrationSearchResponse;
import com.edufocus.edufocus.registration.entity.RegistrationStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RegistrationService {

    boolean createRegistration(Long userId, Long registrationId);

    boolean acceptRegistration(Long userId, Long RegistrationId);

    boolean deleteRegistration(Long userId, Long registrationId);

    List<RegistrationSearchResponse> searchRegistrations(Long LectureId);

    RegistrationStatus isOnline(Long userId, Long lectureId);
}
