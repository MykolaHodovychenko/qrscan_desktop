package com.fitness_ua.LocalService;

import com.fitness_ua.Configuration.AppData;
import com.fitness_ua.Configuration.ClubStuffDescription;
import com.fitness_ua.Configuration.Utils;
import com.fitness_ua.DiscoverServer.DiscoverServer;
import com.fitness_ua.LocalServer.Dict;
import com.fitness_ua.LocalServer.IDataRetrievalCallback;
import com.fitness_ua.LocalServer.LocalServer;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Consumer;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Created by salterok on 11.06.2015.
 */
public class MainForm {
    private static final String APP_NAME = "MLB";
    private static final Logger logger = getLogger(MainForm.class);
    private int mIdCurr = 1;
    private HashMap<Integer, Message> mClients = new HashMap();
    private LocalServer server;
    private Configurator conf;
    private BrokerQueue brokerQueue;
    private DiscoverServer discoverServer;
    private Consumer<String> eventOccurred;
    private Synchronizer synchronizer;

    public MainForm() {

    }

    public void init() {
        try {
            conf = Configurator.getInstance(APP_NAME);
            if (!conf.acquireConfiguration()) {
                // todo: try read old cached information
                //logger.info("Closing...");
                //System.exit(8);
            }
        } catch (Exception ex) {
            logger.error("Initialization error", ex);
        }

        logger.info("Initializing...");
        AppData appData = conf.props.getAppData();

        synchronizer = new Synchronizer(conf);
        //synchronizer.setConfigurator(); //development server
        synchronizer.synchronizeAttendancies();


        discoverServer = new DiscoverServer(appData.getDiscoverPort());
        discoverServer.start();
        logger.info("Starting local server");
        server = new LocalServer(appData.getServerPort());
        server.setRoot(appData.getServerRoot());
        String indexPage = appData.getUseIFrameIntegration() ? "index-iframe.html" : "index.html";
        server.setIndex(indexPage);
        server.setDataCallback(new IDataRetrievalCallback() {
            public Dict getById(int id) {
                return getMessage(id);
            }
        });

        Boolean state = startBroker();
        if (state) {
            try {
                server.start();
            } catch (Exception ex) {
                logger.error("Error while starting server", ex);
                System.exit(5); // server fail
            }
        } else {
            logger.error("Connection to broker is not established");
            System.exit(4); // broker fail
        }
        eventOccurred.accept("Service running...");
    }

    public void dispose() {
        logger.warn("FitnessUA closing...");
        logger.warn("Disconnecting from MQTT-broker...");
        brokerQueue.disconnect();
        logger.warn("Stopping discovery server...");
        discoverServer.dispose();
        logger.warn("Stopping local web-server...");
        server.stop();
    }

    private Dict getMessage(int id) {
        if (id > 0 && mClients.containsKey(id)) {
            final Message msg = mClients.get(id);
            Dict data = new Dict();

            if (msg.message.cardId == -1) {
                data.put("clientId", msg.message.clientId.toString());
                data.put("clientFullname", msg.message.fullname);
                data.put("abonnementId", msg.message.abonnementId.toString());
                data.put("subscriptionId", msg.message.subscriptionId.toString());

                for (ClubStuffDescription desc : conf.getClubServices()) {
                    if (desc.id == msg.message.abonnementId) {
                        data.put("abon_title", desc.title);
                        break;
                    }
                }
                for (ClubStuffDescription desc : conf.getClubSubscriptions()) {
                    if (desc.id == msg.message.subscriptionId) {
                        data.put("subs_title", desc.title);
                        break;
                    }
                }
            } else {
                data.put("clientId", msg.message.cardId.toString());
            }
            return data;
        }
        return new Dict();
    }

    private boolean startBroker() {
        try {
            AppData appData = conf.props.getAppData();

            brokerQueue = new BrokerQueue(appData.getBrokerHost(), "MalibuLocalService");
            boolean state = brokerQueue.connect(new MqttCallback() {
                public void connectionLost(Throwable throwable) {

                }

                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    logger.debug(new String(mqttMessage.getPayload(), "UTF-8"));
                    logger.debug("Received data: {}", new String(mqttMessage.getPayload(), Charset.forName("UTF8")));
                    processMessage(s, new String(mqttMessage.getPayload(), Charset.forName("UTF8")));
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            if (!state) {
                return false;
            }
            brokerQueue.subscribe(appData.getTopicName());

        } catch (Exception ex) {
            logger.error("Error while starting broker consumer", ex);
            return false;
        }
        logger.info("Broker running...");
        return true;
    }

    private void processMessage(String consumerTag, String message) {
        Message msg = consumeMessage(consumerTag, message);
        if (msg == null) {
            logger.info("Some error while consuming message. Body: " + message);
        } else {
            int id = mIdCurr;
            mClients.put(mIdCurr++, msg);
            if (!openClientPage(id, msg.message))
                synchronizer.saveMessage(msg); //save to local DB
        }
    }

    private Message consumeMessage(String consumerTag, String message) {
        logger.info(String.format("Received message from %s", consumerTag));
        try {
            JSONObject obj = new JSONObject(message);
            Message msg = new Message();
            msg.time = new Date();
            QRData data = new QRData();

            if (obj.has("CardId")) {
                data.cardId = obj.getInt("CardId");
            } else {
                data.clientId = obj.getInt("ClientId");
                if (obj.has("AbonementId")) {
                    data.abonnementId = obj.getInt("AbonementId");
                } else if (obj.has("AbonnementId")) {
                    data.abonnementId = obj.getInt("AbonnementId");
                }

                data.subscriptionId = obj.getInt("SubscriptionId");
                data.fullname = obj.getString("Fullname");
            }
            msg.message = data;

            return msg;
        } catch (Exception ex) {
            logger.error("Error while consuming message", ex);
            return null;
        }
    }

    private String buildRemoteUrl(AppData appData, QRData data) {
        String value = appData.getRemoteUrl() + appData.getLinkOpenUser() + "?";
        if (data.cardId != -1) {
            value += String.format(appData.getPcnOnlyParam(), data.cardId);
        } else {
            value += String.format(appData.getLinkOpenUserQuery(), data.clientId, data.subscriptionId);
        }
        return value;
    }

    private boolean openClientPage(int id, QRData data) {
        AppData appData = conf.props.getAppData();
        String local = "http://localhost:" + appData.getServerPort() + "/?clientHash=" + id;
        String localOnlyPcn = "http://localhost:" + appData.getServerPort() + "/?onlyPcn";
        String remote = buildRemoteUrl(appData, data);
        try {
            if (appData.getUseIFrameIntegration()) {
                logger.info(String.format("Opening [LOCAL-IFRAME] page - %s", local));
                Desktop.getDesktop().browse(new URL(local).toURI());
                return true;
            }

            if (Utils.ping(appData.getRemoteUrl(), 500)) {
                logger.info(String.format("Opening [REMOTE] page - %s", remote));
                Desktop.getDesktop().browse(new URL(remote).toURI());
                return true;
            } else {
                if (data.cardId != -1) {
                    logger.info(String.format("Opening [LOCAL] page (only pcn) - %s", localOnlyPcn));
                    Desktop.getDesktop().browse(new URL(localOnlyPcn).toURI());
                } else {
                    logger.info(String.format("Opening [LOCAL] page - %s", local));
                    Desktop.getDesktop().browse(new URL(local).toURI());
                }
                return false;
            }
        } catch (Exception ex) {
            logger.error("Error while opening web-page", ex);
        }
        return false;
    }

    public void setEventOccurred(Consumer<String> eventOccurred) {
        this.eventOccurred = eventOccurred;
    }
}