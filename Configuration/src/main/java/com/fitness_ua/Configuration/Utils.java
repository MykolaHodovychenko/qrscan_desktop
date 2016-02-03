package com.fitness_ua.Configuration;

import org.apache.logging.log4j.*;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.IOUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by salterok on 02.03.2015.
 */
public class Utils {
    private static Logger log = LogManager.getLogger(Utils.class);
    public static ArrayList<ClubStuffDescription> fillFromJson(String json) {
        JSONArray arr = new JSONArray(json);
        ArrayList<ClubStuffDescription> services = new ArrayList<ClubStuffDescription>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject item = arr.getJSONObject(i);
            ClubStuffDescription service = new ClubStuffDescription();
            service.id = item.getInt("Id");
            service.title = item.getString("Title");
            services.add(service);
        }
        return services;
    }

    private static URLConnection setUpBasicConnection(String uri, String name, String pass) throws IOException {
        URL url = new URL(uri);
        URLConnection uc = url.openConnection();
        String userpass = name + ":" + pass;
        String basicAuth = "Basic " + toBase64(userpass.getBytes());
        uc.setRequestProperty("Authorization", basicAuth);
        uc.setRequestProperty("Content-Type", "application/json");
        return uc;
    }

    private static String prepareConnectionResponse(URLConnection uc) throws IOException {
        InputStream in = uc.getInputStream();
        return new String(IOUtils.readFully(in, -1, true), Charset.forName("UTF-8"));
    }

    public static String getUrlWithBasic(String uri, String name, String pass) {
        try {
            URLConnection uc = setUpBasicConnection(uri, name, pass);
            return prepareConnectionResponse(uc);
        }
        catch (Exception ex) {
            // TODO: log
            log.error("Url get error", ex);
            return null;
        }
    }

    public static String getUrlWithBasic(String uri, String name, String pass, String body) {
        try {
            URLConnection uc = setUpBasicConnection(uri, name, pass);
            uc.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(uc.getOutputStream());
            wr.write(body);
            wr.close();
            return prepareConnectionResponse(uc);
        }
        catch (Exception ex) {
            // TODO: log
            log.error("Url get error", ex);
            return null;
        }
    }

    public static String toBase64(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    public static byte[] fromBase64(String value) {
        return DatatypeConverter.parseBase64Binary(value);
    }

    public static boolean ping(String url, int timeout) {
        // Otherwise an exception may be thrown on invalid SSL certificates:
        url = url.replaceFirst("^https", "http");

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }
}
