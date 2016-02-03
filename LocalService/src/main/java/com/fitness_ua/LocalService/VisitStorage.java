package com.fitness_ua.LocalService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by salterok on 19.03.2015.
 */
public class VisitStorage {
    private static final Logger log = LogManager.getLogger(VisitStorage.class);

    private String filename;
    private List<Message> flushed = null;
    private List<Message> mem = new ArrayList<Message>();

    public VisitStorage(String name) {
        this.filename = name;
        try {
            List<String> lines = Files.readAllLines(Paths.get(name));
            flushed = Arrays.stream(lines.toArray(new String[0])).map(new Function<String, Message>() {
                public Message apply(String s) {
                    return Message.parse(s);
                }
            }).collect(new Supplier<List<Message>>() {
                public List<Message> get() {
                    return flushed;
                }
            }, new BiConsumer<List<Message>, Message>() {
                public void accept(List<Message> messages, Message message) {
                    messages.add(message);
                }
            }, new BiConsumer<List<Message>, List<Message>>() {
                public void accept(List<Message> messages, List<Message> messages2) {
                    messages.addAll(messages2);
                }
            });
        }
        catch (Exception ex) {
            log.error("Can't load storage file", ex);
        }
    }


    public void put(Message msg) {
        if (msg != null)
            mem.add(msg);
    }

    public boolean save() {
        try {
            FileWriter fw = new FileWriter(this.filename, true);

            List<String> temp = new ArrayList<String>();
            for (Message msg : mem) {
                temp.add(msg.toString());
            }

            String value = String.join("\n", temp);
            fw.write(value + "\n");
            fw.close();

            flushed.addAll(mem);
            mem.clear();
            return true;
        }
        catch(Exception ex)
        {
            log.error("Error while saving visit store", ex);
            return false;
        }
    }

}
