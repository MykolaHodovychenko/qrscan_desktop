package com.fitness_ua.Configuration;

/**
 * Created by salterok on 02.03.2015.
 */
public class ApiData {
    private String clubsListUrl;
    private String clubsServicesUrl;
    private String clubsSubscriptionsUrl;
    private String login;
    private String password;

    public void setClubsListUrl(String clubsListUrl) {
        this.clubsListUrl = clubsListUrl;
    }

    public void setClubsServicesUrl(String clubsServicesUrl) {
        this.clubsServicesUrl = clubsServicesUrl;
    }

    public void setClubsSubscriptionsUrl(String clubsSubscriptionsUrl) {
        this.clubsSubscriptionsUrl = clubsSubscriptionsUrl;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClubsListUrl() {
        return clubsListUrl;
    }

    public String getClubsServicesUrl() {
        return clubsServicesUrl;
    }

    public String getClubsSubscriptionsUrl() {
        return clubsSubscriptionsUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public ApiData(String clubsListUrl, String clubsServicesUrl, String clubsSubscriptionsUrl, String login, String password) {
        this.clubsListUrl = clubsListUrl;
        this.clubsServicesUrl = clubsServicesUrl;
        this.clubsSubscriptionsUrl = clubsSubscriptionsUrl;
        this.login = login;
        this.password = password;
    }
}
