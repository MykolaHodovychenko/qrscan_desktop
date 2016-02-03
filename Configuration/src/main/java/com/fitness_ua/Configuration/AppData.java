package com.fitness_ua.Configuration;

/**
 * Created by salterok on 02.03.2015.
 */
public class AppData {
    private String serverRoot;
    private int serverPort;
    private int discoverPort;
    private String topicName;
    private String brokerHost;
    private String remoteUrl;
    private String pcnOnlyParam;
    private String linkOpenUser;
    private String linkOpenUserQuery;
    private String dbFilePath;
    private Boolean useIFrameIntegration;

    public String getPcnOnlyParam() {
        return pcnOnlyParam;
    }

    public void setPcnOnlyParam(String pcnOnlyParam) {
        this.pcnOnlyParam = pcnOnlyParam;
    }

    public String getLinkOpenUser() {
        return linkOpenUser;
    }

    public void setLinkOpenUser(String linkOpenUser) {
        this.linkOpenUser = linkOpenUser;
    }

    public String getLinkOpenUserQuery() {
        return linkOpenUserQuery;
    }

    public void setLinkOpenUserQuery(String linkOpenUserQuery) {
        this.linkOpenUserQuery = linkOpenUserQuery;
    }

    public String getServerRoot() {
        return serverRoot;
    }

    public void setServerRoot(String serverRoot) {
        this.serverRoot = serverRoot;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setDiscoverPort(int discoverPort) { this.discoverPort = serverPort; }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setBrokerHost(String brokerHost) {
        this.brokerHost = brokerHost;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getDiscoverPort() { return discoverPort; }

    public String getTopicName() {
        return topicName;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public String getDbFilePath() {
        return dbFilePath;
    }

    public void setDbFilePath(String dbFilePath) {
        this.dbFilePath = dbFilePath;
    }

    public Boolean getUseIFrameIntegration() {
        return useIFrameIntegration;
    }

    public void setUseIFrameIntegration(Boolean useIFrameIntegration) {
        this.useIFrameIntegration = useIFrameIntegration;
    }

    public AppData(String serverRoot, int serverPort, int discoverPort, String topicName, String brokerHost, String remoteUrl, String pcnOnlyParam, String linkOpenUser, String linkOpenUserQuery, String dbFilePath, Boolean useIFrameIntegration) {
        this.serverRoot = serverRoot;
        this.serverPort = serverPort;
        this.discoverPort = discoverPort;
        this.topicName = topicName;
        this.brokerHost = brokerHost;
        this.remoteUrl = remoteUrl;
        this.pcnOnlyParam = pcnOnlyParam;
        this.linkOpenUser = linkOpenUser;
        this.linkOpenUserQuery = linkOpenUserQuery;
        this.dbFilePath = dbFilePath;
        this.useIFrameIntegration = useIFrameIntegration;
    }
}
