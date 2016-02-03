package com.fitness_ua.LocalService;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 24.07.2015.
 */
public class JSONDateFormatter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private static SimpleDateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {

        return new JsonPrimitive(ISO8601.format(src));
    }

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        Date tmpDate = new Date(0);

        try {
            String tmp = json.getAsJsonPrimitive().getAsString();
            tmpDate = ISO8601.parse(tmp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return tmpDate;
    }

    public static String serialize(Date src) {

        return ISO8601.format(src);
    }

    public static Date deserialize(String date) {

        Date tmpDate = new Date(0);

        try {
            tmpDate = ISO8601.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return tmpDate;
    }

}
