package com.fitness_ua.DiscoverServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by salterok on 25.05.2015.
 */
public class DiscoverServer extends Thread {
    private static final Logger log = LogManager.getLogger(DiscoverServer.class);
    private static final String KEY = "malibu_hash";
    private int DISCOVERY_PORT;
    private DatagramSocket socket;
    private boolean needRun = true;

    public DiscoverServer(int listenPort) {
        this.DISCOVERY_PORT = listenPort;
    }

    public void dispose() {
        needRun = false;
        socket.close();
    }

    @Override
    public void run() {
        log.info("Starting discovery server");
        byte[] buf = new byte[1024];
        try {
            socket = new DatagramSocket(DISCOVERY_PORT);
        }
        catch (Exception ex) {
            log.error("Error creating socket", ex);
            return;
        }
        while (needRun) { // forever
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String data = new String(packet.getData(), 0, packet.getLength());
                log.info("Response received from {} with data: {}", packet.getAddress().toString(), data);
                String responseData = getSignature(data);

                DatagramPacket response = new DatagramPacket(responseData.getBytes(), responseData.length(), packet.getAddress(), DISCOVERY_PORT);
                socket.send(response);
                String snd = new String(response.getData(), 0 , response.getLength());
                log.info("Sending response to {}: {}", packet.getAddress().toString(), snd);
            }
            catch (SocketException ex) {
                // do nothing
            }
            catch (Exception ex) {
                log.error("Some error occurred", ex);
            }
        }
    }

    private String getSignature(String input) throws NoSuchAlgorithmException {
        MessageDigest digest;
        byte[] md5sum = null;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            digest.update(KEY.getBytes());
            md5sum = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        StringBuffer hexString = new StringBuffer();
        for (int k = 0; k < md5sum.length; ++k) {
            String s = Integer.toHexString((int) md5sum[k] & 0xFF);
            if (s.length() == 1)
                hexString.append('0');
            hexString.append(s);
        }
        return hexString.toString();
    }
}
