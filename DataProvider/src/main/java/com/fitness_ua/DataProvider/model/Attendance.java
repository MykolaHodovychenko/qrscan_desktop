package com.fitness_ua.DataProvider.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by salterok on 23.07.2015.
 */

@DatabaseTable(tableName = "Attendance")
public class Attendance {
    public static final String ID = "Attendance_Id";
    public static final String CARD_ID = "Attendance_CardId";
    public static final String CLIENT_ID = "Attendance_ClientId";
    public static final String ABONNEMENT_ID = "Attendance_AbonnementId";
    public static final String SERVICE_ID = "Attendance_ServiceId";
    public static final String NAME = "Attendance_Name";
    public static final String TIME = "Attendance_Time";
    public static final String SYNC = "Attendance_Sync";

    @DatabaseField(generatedId = true, columnName = ID)
    public int id;

    @DatabaseField(columnName = CARD_ID)
    public int cardId;

    @Expose
    @SerializedName("clientId")
    @DatabaseField(columnName = CLIENT_ID)
    public int clientId;

    @Expose
    @SerializedName("subscriptionId")
    @DatabaseField(columnName = ABONNEMENT_ID)
    public int abonementId;

    @Expose
    @SerializedName("serviceId")
    @DatabaseField(columnName = SERVICE_ID)
    public int serviceId;

    @DatabaseField(columnName = NAME)
    public String name;

    @Expose
    @SerializedName("occuredAt")
    @DatabaseField(columnName = TIME, canBeNull = false)
    public Date time;

    @DatabaseField(columnName = SYNC, canBeNull = false)
    public boolean sync;
}
