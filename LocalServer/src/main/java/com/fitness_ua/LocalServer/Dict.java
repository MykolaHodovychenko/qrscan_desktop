package com.fitness_ua.LocalServer;

import java.util.HashMap;

/**
 * Created by salterok on 11.03.2015.
 */
public class Dict extends HashMap<String, String> {
    public Dict(HashMap<String, String> data) {
        super(data);
    }
    public Dict() {}

    public String get(String key) {
        return this.containsKey(key) ? super.get(key) : String.format("{!%s}", key);
    }
}
