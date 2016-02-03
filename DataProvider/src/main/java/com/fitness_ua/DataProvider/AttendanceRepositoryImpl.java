package com.fitness_ua.DataProvider;

import com.fitness_ua.DataProvider.model.Attendance;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

/**
 * Created by salterok on 23.07.2015.
 */
public class AttendanceRepositoryImpl extends BaseDaoImpl<Attendance, Integer> implements AttendanceRepository {
    private static final Logger logger = LogManager.getLogger(AttendanceRepositoryImpl.class);
    public AttendanceRepositoryImpl(ConnectionSource connection) throws SQLException {
        super(connection, Attendance.class);
    }

    @Override
    public void registerAttendance(Attendance att) {
        try {
            this.create(att);
        }
        catch (Exception ex) {
            logger.error(ex);
        }
    }

    @Override
    public List<Attendance> getAttendance(boolean sync) {
        try {
            return this.queryBuilder().where().eq(Attendance.SYNC, sync).query();
        }
        catch (Exception ex) {
            logger.error(ex);
            return Collections.emptyList();
        }
    }

    @Override
    public void setSyncAttendance(final List<Attendance> att, boolean state) {
         int[] i = att.stream().mapToInt(new ToIntFunction<Attendance>() {
            @Override
            public int applyAsInt(Attendance value) {
                return value.id;
            }
        }).toArray();

        Object[] o = att.stream().map(new Function<Attendance, Integer>() {
            @Override
            public Integer apply(Attendance attendance) {
                return attendance.id;
            }
        }).toArray();

        try {
            Where<Attendance, Integer> where = this.queryBuilder().where().in(Attendance.ID, o);
            UpdateBuilder updteBuilder = this.updateBuilder();
            updteBuilder.updateColumnValue(Attendance.SYNC, state).setWhere(where);
            updteBuilder.update();
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    @Override
    public void deleteAttendance(Attendance att) {
        try {
            this.delete(att);
        }
        catch (Exception ex) {
            logger.error(ex);
        }
    }
}
