package com.fitness_ua.LocalServer;

/**
 * Created by salterok on 10.03.2015.
 */
public class Main {
    public static void main(String[] args) {

        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        LocalServer server = new LocalServer(80);
                        server.setRoot("www/");
                        server.setIndex("index.html");
                        server.setDataCallback(new IDataRetrievalCallback() {
                            public Dict getById(int id) {
                                Dict d = new Dict();
                                d.put("clientId", "56");
                                return d;
                            }
                        });
                        server.start();

                    }
                    catch (Exception ex) {}
                }
            }).start();


            int y = 0;
            while (true) {
                Thread.sleep(500);
                System.out.println("ss+" + y++);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
