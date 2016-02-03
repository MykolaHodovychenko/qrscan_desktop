package com.fitness_ua.LocalService;

import com.fitness_ua.Configuration.*;
import org.apache.logging.log4j.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

/**
 * Created by salterok on 24.02.2015.
 */
public class Configurator {
    private static Logger log = LogManager.getLogger(Configurator.class);
    private static Configurator instance = null;

    private ArrayList<ClubStuffDescription> clubServices = new ArrayList();

    private ArrayList<ClubStuffDescription> clubSubscriptions = new ArrayList();;

    public ArrayList<ClubStuffDescription> getClubServices() {
        return clubServices;
    }

    public ArrayList<ClubStuffDescription> getClubSubscriptions() {
        return clubSubscriptions;
    }

    public PropertiesHolder props = null;

    public static Configurator getInstance(String appName) {
        if (instance == null) {
            Configurator temp = new Configurator();
            try {
                temp.load(appName);
            }
            catch (Exception ex) {
                log.error("Error while loading configuration", ex);
            }
            instance = temp;
        }
        return instance;
    }

    private Configurator() {}

    private void load(String appName) throws Exception {
        props = new PropertiesHolder(appName);
        props.init();


        if (props.getClubData().getId() == -1) {
            // TODO: open local web page with msg: ~need configuration
            log.error("Initial configuration needed. CLUB_ID is not set");
            System.exit(7);
        }
    }

    public boolean isConfigured() {
        throw new NotImplementedException();
    }

    public boolean acquireConfiguration() {
        log.info("Acquire configuration");
        AppData appData = props.getAppData();
        ApiData apiData = props.getApiData();
        ClubData clubData = props.getClubData();

        String url = appData.getRemoteUrl() + String.format(apiData.getClubsServicesUrl(), clubData.getId());
        String json = Utils.getUrlWithBasic(url, apiData.getLogin(), apiData.getPassword());
        if (json == null) {
            log.error("Can't load configuration from " + url);
            return false;
        }
        this.clubServices = Utils.fillFromJson(json.trim());

        url = appData.getRemoteUrl() + String.format(apiData.getClubsSubscriptionsUrl(), clubData.getId());
        json = Utils.getUrlWithBasic(url, apiData.getLogin(), apiData.getPassword());
        if (json == null) {
            log.error("Can't load configuration from " + url);
            return false;
        }
        this.clubSubscriptions = Utils.fillFromJson(json);

        return true;
    }

}
