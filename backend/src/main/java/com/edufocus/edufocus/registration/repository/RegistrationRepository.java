package com.edufocus.edufocus.registration.repository;

import com.edufocus.edufocus.registration.entity.Registration;
import com.edufocus.edufocus.registration.entity.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findAllByUserId(@Param("userId") Long userId);

    List<Registration> findAllByLectureId(Long lectureId);

    Registration findByUserIdAndLectureId(Long userId, Long lectureId);

    List<Registration> findByLectureIdAndStatus(Long lectureId, RegistrationStatus status);
}
