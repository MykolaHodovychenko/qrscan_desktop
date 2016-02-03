/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fitness_ua.LocalService;

import java.text.SimpleDateFormat;

/**
 *
 * @author salterok
 */

public class QRData {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    public Integer cardId = -1;
    public Integer clientId;
    public Integer abonnementId;
    public Integer subscriptionId;

    public String fullname;


//    @Override
//    public String toString() {
//        return
//            Integer.toString(clientId) + "\n" +
//            format.format(from) + "\n" +
//            format.format(to) + "\n" +
//            type + "\n" +
//            fullname + "\n" +
//            surname + "\n" +
//            phone + "\n";
//    }
//
//    public static QRData create(String value) {
//        String[] data = value.split("\n");
//        if (data.length != 7)
//            return null;
//        try {
//            QRData obj = new QRData();
//            obj.clientId = Integer.parseInt(data[0]);
//            obj.from = format.parse(data[1]);
//            obj.to = format.parse(data[2]);
//            obj.type = data[3];
//            obj.fullname = data[4];
//            obj.surname = data[5];
//            obj.phone = data[6];
//            return obj;
//        }
//        catch (Exception ex) {
//            return null;
//        }
//    }
}