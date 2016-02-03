package com.fitness_ua.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by salterok on 02.03.2015.
 */
public class Main extends JFrame {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String APP_NAME = "MLB";
    private DefaultListModel<Map.Entry<String, String>> propertiesModel;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JLabel selectedClub;
    private JList<ClubStuffDescription> clubsList;
    private JButton selectClubBtn;
    private JList propertiesList;
    private JButton savePropertiesBtn;
    private JButton loadClubsListBtn;
    private JProgressBar loadProgress;
    private PropertiesHolder props;

    public Main() {}

    public void init(List<String> args) {
        loadProgress.setVisible(false);
        propertiesModel = new DefaultListModel<Map.Entry<String, String>>();
        initializeComponent();
        initializeProperties();

        selectedClub.setText(props.getClubData().getTitle());
        if (args.contains("--select-club")) {
            retrieveClubListAsync();
            tabbedPane.setSelectedIndex(1); // Options tab
        }

        updatePropertiesList();


    }

    private void updatePropertiesList() {
        final Map<String, String> dict = props.toDict();
        propertiesModel.clear();
        dict.entrySet().forEach(new Consumer<Map.Entry<String, String>>() {
            public void accept(Map.Entry<String, String> entry) {
                propertiesModel.addElement(entry);
            }
        });
    }

    private void initializeProperties() {
        props = new PropertiesHolder(APP_NAME);
        props.init();
    }

    private void retrieveClubListAsync() {
        loadProgress.setIndeterminate(true);
        loadProgress.setVisible(true);
        new Thread(new Runnable() {
            public void run() {
                getOptions();
            }
        }).start();
    }

    private void getOptions() {
        AppData appData = props.getAppData();
        ApiData apiData = props.getApiData();

        List<ClubStuffDescription> clubs = Collections.emptyList();
        try {
            String url = appData.getRemoteUrl() + apiData.getClubsListUrl();
            String json = Utils.getUrlWithBasic(url, apiData.getLogin(), apiData.getPassword());
            clubs = Utils.fillFromJson(json);
        }
        catch (Exception ex) {
            logger.error("Error loading clubs list", ex);
        }
        final ClubStuffDescription[] result = clubs.toArray(new ClubStuffDescription[clubs.size()]);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loadProgress.setIndeterminate(false);
                loadProgress.setValue(100);
                clubsList.setListData(result);
            }
        });
    }

    public static void main(String[] args) {
        final List<String> params = Arrays.asList(args);

        JFrame frame = new JFrame("Main");
        final Main main = new Main();
        frame.setContentPane(main.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 400));
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            //
            // Invoked when a window has been opened.
            //
            public void windowOpened(WindowEvent e) {
                main.init(params);
            }

            //
            // Invoked when a window has been closed.
            //
            public void windowClosed(WindowEvent e) {
                main.onClose(e);
            }

        });
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void onClose(WindowEvent e) {
        props.save();
    }

    private void initializeComponent() {
        clubsList.setCellRenderer(new ClubListItemRenderer());
        propertiesList.setCellRenderer(new PropertyRenderer());
        propertiesList.setModel(propertiesModel);

        selectClubBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClubStuffDescription item = (ClubStuffDescription) clubsList.getSelectedValue();
                if (item != null) {
                    selectedClub.setText(item.title);
                    ClubData clubData = new ClubData(item.id, item.title);
                    props.setClubData(clubData);
                    props.save();

                    updatePropertiesList();
                }
            }
        });
        loadClubsListBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                retrieveClubListAsync();
            }
        });
        savePropertiesBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                props.save();
            }
        });
    }
}
