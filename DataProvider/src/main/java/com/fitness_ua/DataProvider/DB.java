package com.fitness_ua.DataProvider;

import com.fitness_ua.DataProvider.model.Attendance;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Created by salterok on 23.07.2015.
 */
public class DB {
    private static ConnectionSource connectionSource;

    public static void init(DBConfig conf) {
        try {
            connectionSource = new JdbcPooledConnectionSource(conf.dbFilePath);

            attendances = new AttendanceRepositoryImpl(connectionSource);

            TableUtils.createTableIfNotExists(connectionSource, Attendance.class);
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void dispose() {
        try {
            connectionSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static <T> void transaction(Callable<T> action) {
        TransactionManager transactionManager = new TransactionManager(connectionSource);
        try {
            transactionManager.callInTransaction(action);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static AttendanceRepository attendances;
}
