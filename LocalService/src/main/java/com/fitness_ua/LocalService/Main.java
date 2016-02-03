package com.fitness_ua.LocalService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

import org.apache.logging.log4j.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import static org.apache.logging.log4j.LogManager.getLogger;

public class Main {
    static {
        //System.setProperty("log4j.configurationFile", "log4j2.xml");
    }

    private static final Logger logger = getLogger(Main.class);


    public static void main(String[] args) {
        final JFrame frame = new JFrame("Main");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(false);

        try {
            initialize(frame);
        } catch (Exception e) {
            logger.error("Error while starting app: ", e);
        }
    }

    private static void initialize(final JFrame frame) throws Exception {
        final MainForm main = new MainForm();

        SystemTray tray = SystemTray.getSystemTray();

        PopupMenu popup = new PopupMenu();
        // create menu item for the default action
        MenuItem defaultItem = new MenuItem("Exit");
        defaultItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                main.dispose();
                frame.setVisible(false);
                frame.dispose();
                System.exit(0);
            }
        });
        popup.add(defaultItem);

        BufferedImage iconImage = ImageIO.read(main.getClass().getResource("/img/icon16.png"));
        final TrayIcon trayIcon = new TrayIcon(iconImage, "FitnessUA", popup);
        trayIcon.setImageAutoSize(true);

        tray.add(trayIcon);

        main.setEventOccurred(new Consumer<String>() {
            public void accept(String s) {
                trayIcon.displayMessage("FitnessUA", s, TrayIcon.MessageType.INFO);
            }
        });
        logger.info("Starting FitnessUA...");

        frame.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {

            }

            public void windowClosing(WindowEvent e) {
                main.dispose();
            }

            public void windowClosed(WindowEvent e) {

            }

            public void windowIconified(WindowEvent e) {

            }

            public void windowDeiconified(WindowEvent e) {

            }

            public void windowActivated(WindowEvent e) {

            }

            public void windowDeactivated(WindowEvent e) {

            }
        });

        main.init();
    }
}
