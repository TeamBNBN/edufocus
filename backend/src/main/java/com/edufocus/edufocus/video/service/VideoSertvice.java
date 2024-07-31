package com.edufocus.edufocus.video.service;

import java.sql.SQLException;

public interface VideoSertvice {

    void startOnline(Long userId,Long lectureId) throws SQLException;
}
