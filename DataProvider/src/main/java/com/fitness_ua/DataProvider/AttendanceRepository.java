package com.fitness_ua.DataProvider;

import com.fitness_ua.DataProvider.model.Attendance;
import com.j256.ormlite.dao.Dao;

import java.util.List;

/**
 * Created by salterok on 23.07.2015.
 */
public interface AttendanceRepository extends Dao<Attendance, Integer> {
    void registerAttendance(Attendance att);
    List<Attendance> getAttendance(boolean sync);
    void setSyncAttendance(List<Attendance> att, boolean state);
    void deleteAttendance(Attendance att);
}