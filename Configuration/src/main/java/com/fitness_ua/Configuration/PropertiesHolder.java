package com.fitness_ua.Configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by salterok on 20/01/2015.
 */
public class PropertiesHolder {
    private Properties props;
    private Path path;

    private AppData appData;
    private ClubData clubData;
    private ApiData apiData;


    public PropertiesHolder(String appName) {
        try {
            path = Paths.get(appName + ".properties");
            System.out.println("Search for configuration at: " + path.toAbsolutePath());
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        }
        catch (Exception ex) {
            // TODO: log
            ex.printStackTrace();
        }
    }

    public void save() {
        if (props != null) {
            try {
                OutputStream stream = new FileOutputStream(path.toString());
                props.store(stream, null);
                stream.close();
            }
            catch (Exception ex) {
                System.out.print("Error while saving properties: " + ex.getMessage());
            }
        }
    }

    public void init() {
        try {
            if (Files.exists(path)) {
                props = new Properties();
                InputStream stream = new FileInputStream(path.toString());
                props.load(stream);
                stream.close();
            } else {
                throw new Exception("Can't load properties file");
            }
        }
        catch (Exception ex) {
            // TODO: log err
            System.out.println(ex.getMessage());
        }
    }

    public Map<String, String> toDict() {
        final Map<String, String> dict = new HashMap<String, String>();
        props.keySet().forEach(new Consumer<Object>() {
            public void accept(Object key) {
                dict.put(key.toString(), props.get(key).toString());
            }
        });
        return dict;
    }

    //region AppData
    public AppData getAppData() {
        if (this.appData == null) {
            this.appData = loadAppData();
        }
        return this.appData;
    }
    public void setAppData(AppData appData) {
        if (appData != null) {
            this.appData = appData;
            storeAppData(appData);
        }
    }
    private AppData loadAppData() {
        String serverRoot = getString("SERVER_ROOT", "www");
        int serverPort = getInt("SERVER_PORT", 8080);
        int discoverPort = getInt("DISCOVER_PORT", 62308);
        String brokerQueueName = getString("BROKER_QUEUE_NAME", "client_check");
        String brokerHost = getString("BROKER_HOST", "localhost");
        String remoteUrl = getString("REMOTE_URL", "");
        String pcn = getString("PCN_ONLY_PARAM", "");
        String lOU = getString("LINK_OPEN_USER", "");
        String lOUQ = getString("LINK_OPEN_USER_QUERY", "");
        String dbFPath = getString("DB_FILE_PATH", "");
        Boolean useIFrameIntegration = getBool("USE_IFRAME_INTEGRATION", false);

        return new AppData(serverRoot, serverPort, discoverPort, brokerQueueName, brokerHost,
                remoteUrl, pcn, lOU, lOUQ, dbFPath, useIFrameIntegration);
    }

    private void storeAppData(AppData appData) {
        setProp("SERVER_ROOT", appData.getServerRoot());
        setProp("SERVER_PORT", String.valueOf(appData.getServerPort()));
        setProp("DISCOVER_PORT", String.valueOf(appData.getDiscoverPort()));
        setProp("BROKER_QUEUE_NAME", appData.getTopicName());
        setProp("BROKER_HOST", appData.getBrokerHost());
        setProp("REMOTE_URL", appData.getRemoteUrl());
        setProp("PCN_ONLY_PARAM", appData.getPcnOnlyParam());
        setProp("LINK_OPEN_USER", appData.getLinkOpenUser());
        setProp("LINK_OPEN_USER_QUERY", appData.getLinkOpenUserQuery());
        setProp("DB_FILE_PATH", appData.getDbFilePath());
        setProp("USE_IFRAME_INTEGRATION", appData.getUseIFrameIntegration());
    }
    //endregion

    //region ClubData
    public ClubData getClubData() {
        if (this.clubData == null) {
            this.clubData = loadClubData();
        }
        return this.clubData;
    }
    public void setClubData(ClubData clubData) {
        if (clubData != null) {
            this.clubData = clubData;
            storeClubData(clubData);
        }
    }
    private ClubData loadClubData() {
        int id = getInt("CLUB_ID", -1);
        String title = getString("CLUB_TITLE", "");
        return new ClubData(id, title);
    }
    private void storeClubData(ClubData clubData) {
        setProp("CLUB_ID", String.valueOf(clubData.getId()));
        setProp("CLUB_TITLE", clubData.getTitle());
    }
    //endregion

    //region ApiData
    public ApiData getApiData() {
        if (this.apiData == null) {
            this.apiData = loadApiData();
        }
        return this.apiData;
    }
    public void setApiData(ApiData apiData) {
        if (apiData != null) {
            this.apiData = apiData;
            storeApiData(apiData);
        }
    }
    private ApiData loadApiData() {
        String list = getString("API_CLUBS_LIST_URL", "");
        String serv = getString("API_CLUBS_SERVICES_URL", "");
        String subs = getString("API_CLUBS_SUBSCRIPTIONS_URL", "");
        String login = getString("API_AUTH_LOGIN", "");
        String pass = getString("API_AUTH_PASS", "");
        return new ApiData(list, serv, subs, login, pass);
    }
    private void storeApiData(ApiData apiData) {
        setProp("API_CLUBS_LIST_URL", apiData.getClubsListUrl());
        setProp("API_CLUBS_SERVICES_URL", apiData.getClubsServicesUrl());
        setProp("API_CLUBS_SUBSCRIPTIONS_URL", apiData.getClubsSubscriptionsUrl());
        setProp("API_AUTH_LOGIN", apiData.getLogin());
        setProp("API_AUTH_PASS", apiData.getPassword());
    }
    //endregion

    //
    private Integer getInt(String propName, Integer defValue) {
        Integer value = null;
        if (props != null) {
            try {
                String strValue = props.getProperty(propName, null);
                value = Integer.parseInt(strValue);
            }
            catch (Exception ex) {

            }
        }
        return value == null ? defValue : value;
    }
    private String getString(String propName, String defValue) {
        String value = null;
        if (props != null) {
            value = props.getProperty(propName, null);
        }
        return value == null ? defValue : value;
    }
    private Boolean getBool(String propName, Boolean defValue) {
        Boolean value = null;
        if (props != null) {
            try {
                String strValue = props.getProperty(propName, "");
                value = strValue.equalsIgnoreCase("true");
            }
            catch (Exception ex) {

            }
        }
        return value == null ? defValue : value;
    }
    private void setProp(String propName, Object propValue) {
        if (props != null) {
            props.setProperty(propName, propValue.toString());
        }
    }
}
