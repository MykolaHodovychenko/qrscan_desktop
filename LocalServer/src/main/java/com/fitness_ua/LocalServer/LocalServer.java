/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fitness_ua.LocalServer;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;
import org.apache.logging.log4j.*;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;

/**
 *
 * @author salterok
 */
public class LocalServer extends NanoHTTPD {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private static final Logger log = LogManager.getLogger(LocalServer.class);
    private String root = null;
    private String index = null;
    private IDataRetrievalCallback dataCallback;

    public static final String
            MIME_PLAINTEXT = "text/plain",
            MIME_HTML = "text/html",
            MIME_JS = "application/javascript",
            MIME_CSS = "text/css",
            MIME_PNG = "image/png",
            MIME_DEFAULT_BINARY = "application/octet-stream",
            MIME_XML = "text/xml";
    
    public LocalServer(int port) {
        super(port);
    }

    public void setRoot(String root) {
        if (root != null) {
            if (!root.endsWith("/"))
                this.root = root + "/";
            else
                this.root = root;
        }
    }

    public void setIndex(String filename) {
        this.index = filename;
    }

    public void setDataCallback(IDataRetrievalCallback cb) {
        this.dataCallback = cb;
    }

    private Dict getData(int id) {
        if (this.dataCallback != null) {
            return this.dataCallback.getById(id);
        }
        return new Dict();
    }

    private Dict prepareData(IHTTPSession session) {
        if (session.getParms().containsKey("clientHash")) {
            int clientHash = Integer.parseInt(session.getParms().get("clientHash"));
            if (clientHash <= 0) {
                return new Dict();
            }
            return getData(clientHash);
        }
        return new Dict();
    }

    @Override public Response serve(IHTTPSession session) {
        if (session.getUri().startsWith("/ping"))
            return respondPing();

        return getClientPage(session);
    }

    private Response respondPing() {
        return new Response(Response.Status.ACCEPTED, MIME_PLAINTEXT, "ok");
    }

    private Response getClientPage(IHTTPSession session) {
        if (!session.getHeaders().get("http-client-ip").equals("127.0.0.1")) {
            log.warn(String.format("Peer %s trying to connect. Refused", session.getHeaders().get("http-client-ip")));
            return new Response(Response.Status.FORBIDDEN, MIME_PLAINTEXT, "Only loopback ip allowed. Access denied.");
        }

        String uri = session.getUri();
//        Method method = session.getMethod();
//        System.out.println(method + " '" + uri + "' ");

        try {
            if (uri.matches("/(\\w+\\.html?(\\?.*)?)?$")) {
                String file;
                if (uri.equalsIgnoreCase("/")) {
                    file = this.index;
                }
                else {
                    file = uri.substring(1);
                }
                Dict data = prepareData(session);

                String content = Templater.apply(root + file, data);
                return new NanoHTTPD.Response(Response.Status.OK, MIME_HTML,
                        content);
            }
            if (uri.endsWith(".js")) {
                return new NanoHTTPD.Response(Response.Status.OK, MIME_JS,
                        new FileInputStream(root + uri.substring(1)));
            }
            if (uri.endsWith(".css")) {
                return new NanoHTTPD.Response(Response.Status.OK, MIME_CSS,
                        new FileInputStream(root + uri.substring(1)));
            }
            if (uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".bmp")) {
                return new NanoHTTPD.Response(Response.Status.OK, MIME_PNG,
                        new FileInputStream(root + uri.substring(1)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new NanoHTTPD.Response("ERROR");
    }


    public static void main(String[] args) {
        ServerRunner.run(LocalServer.class);
    }
}
