package com.fitness_ua.LocalService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by salterok on 23.03.2015.
 */
public class BrokerQueue {
    private static Logger log = LogManager.getLogger(BrokerQueue.class);
    private String host;
    private String clientId;
    private MqttClient client;

    public BrokerQueue(String host, String clientId) {
        this.host = host;
        this.clientId = clientId;
        try {
            this.client = new MqttClient(host, clientId, new MemoryPersistence());
        }
        catch (Exception ex) {
            log.error("Error creating MqttClient", ex);
        }
    }

    public boolean connect(MqttCallback callback) {
        try {
            if (this.client == null) {
                log.error("MqttClient is not initialized");
                return false;
            }
            if (callback == null) {
                log.warn("MqttCallback is not set");
                return false;
            }
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            this.client.connect(connOpts);
            this.client.setCallback(callback);
            return true;
        }
        catch (Exception ex) {
            log.error("Connecting to broker error: ", ex);
            return false;
        }
    }

    public void disconnect() {
        try {
            this.client.disconnect();
        }
        catch (Exception ex) {
            log.error("Error disconnecting from MQTT-broker");
        }
    }

    public void subscribe(String topic) {
        if (this.client == null) {
            log.error("MqttClient is not initialized");
            return;
        }
        try {
            this.client.subscribe(topic);
        }
        catch (Exception ex) {
            log.error("Can't subscribe to " + topic, ex);
        }
    }
}
