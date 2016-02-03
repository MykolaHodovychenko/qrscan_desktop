package com.fitness_ua.LocalService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Анит on 15.07.2015.
 */
public class SynchronizerResponse {
    public int synced = 0;
    public int errorsCount = 0;
    public List<String> errors = new ArrayList<String>(0);
}
