package com.fitness_ua.LocalService;

import com.fitness_ua.Configuration.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Created by salterok on 19/01/2015.
 */
class Message {
    public Date time;
    public QRData message;

    @Override
    public String toString() {
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(this.time == null ? 0 : this.time.getTime()));
        if (this.message == null) {
            list.add("EOL");
        }
        else {
            list.add(String.valueOf(this.message.cardId));
            list.add(String.valueOf(this.message.clientId));
            list.add(String.valueOf(this.message.abonnementId));
            list.add(String.valueOf(this.message.subscriptionId));
            list.add(Utils.toBase64(this.message.fullname.getBytes()));
        }
        return String.join("|", list);
    }

    public static Message parse(String value) {
        if (value == null) {
            return null;
        }
        String[] parts = value.split("|");
        if (parts.length < 2) {
            return null;
        }
        Message msg = new Message();
        try {
            long time = Long.parseLong(parts[0]);
            msg.time = new Date();
            msg.time.setTime(time);
            if (parts.length == 2 && parts[1].equals("EOL")) {
                return msg;
            }
            if (parts.length == 6) {
                Integer[] nums = Arrays.stream(parts).limit(5).skip(1).map(new Function<String, Integer>() {
                    public Integer apply(String s) {
                        return Integer.parseInt(s);
                    }
                }).toArray(new IntFunction<Integer[]>() {
                    public Integer[] apply(int value) {
                        return new Integer[value];
                    }
                });
                msg.message = new QRData();
                msg.message.cardId = nums[0];
                msg.message.clientId = nums[1];
                msg.message.abonnementId = nums[2];
                msg.message.subscriptionId = nums[3];
                msg.message.fullname = new String(Utils.fromBase64(parts[5]), "UTF-8");
                return msg;
            }
            return null;
        }
        catch (Exception ex) {
            // nothing to say. wrong format
            return null;
        }
    }
}
