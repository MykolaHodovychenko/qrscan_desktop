package com.fitness_ua.LocalBrokerConnector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by salterok on 04.03.2015.
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private final static String QUEUE_NAME = "client_check";
    private static Frame instance;
    private MqttClient mqttClient;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        instance = frame;
        frame.setContentPane(new Main().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public Main() {
        connectToBrokerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //connectToRabbitMQ();
                connectToMQTT();
            }
        });
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });
    }

    private void connectToMQTT() {
        String content      = "Message from MqttPublishSample";
        String broker       = "tcp://localhost";
        String clientId     = "LocalBrokerConnector";

        MemoryPersistence persistence = new MemoryPersistence();

        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            logger.info("Connecting to broker: " + broker);
            mqttClient.connect(connOpts);
            logger.info("Connected");

            mqttClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable throwable) {

                }

                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    logger.info(s + " -> " + mqttMessage);
                }

                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
        } catch(MqttException me) {
            logger.error("reason " + me.getReasonCode());
            logger.error("msg " + me.getMessage());
            logger.error("loc " + me.getLocalizedMessage());
            logger.error("cause " + me.getCause());
            logger.error("excep " + me);
            me.printStackTrace();
        }
    }

    private void publishMQTT(String text) throws MqttException {
        System.out.println("Publishing message: " + text);
        try {
            MqttMessage message = new MqttMessage(text.getBytes("UTF8"));
            int qos = 2;
            message.setQos(qos);

            mqttClient.publish(QUEUE_NAME, message);
            logger.info("Message published");
        }
        catch (Exception x) {}
    }

    private void publishRabbitMQ(String message) {
//        channel.basicPublish("", QUEUE_NAME, null, value.getBytes("UTF-8"));
    }


    private void send() {
        try {
            // {"ClientId":1083,"AbonementId":4,"SubscriptionId":7,"Fullname":"Абушаев  Руслан  Ровильевич"}
            String[] parts = new String[]{
                    "\"ClientId\":" + clientIdField.getValue().toString(),
                    "\"AbonnementId\":" + abonemetnIdField.getValue().toString(),
                    "\"SubscriptionId\":" + subscriptionIdField.getValue().toString(),
                    "\"Fullname\":\"" + nameField.getText() + "\""
            };
            String value = "{" + String.join(",", parts) + "}";
            publishMQTT(value);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(instance, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private JPanel panel1;
    private JButton connectToBrokerButton;
    private JButton sendButton;
    private JTextField nameField;
    private JSpinner clientIdField;
    private JSpinner abonemetnIdField;
    private JSpinner subscriptionIdField;
}
