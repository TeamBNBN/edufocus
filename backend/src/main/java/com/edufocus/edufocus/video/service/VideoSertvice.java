package com.edufocus.edufocus.video.service;

import java.sql.SQLException;

public interface VideoSertvice {

    void startOnline(Long userId, Long lectureId) throws SQLException;

    void stopOnline(Long userId, Long lectureId) throws SQLException;

    boolean isRoomAccessible(Long lectureId, Long userId);

    boolean checkAdmin(Long userId, Long lectureId);

}
