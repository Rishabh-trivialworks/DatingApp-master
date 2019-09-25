package com.quintus.labs.datingapp.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by MyU10 on 2/6/2017.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }
}
