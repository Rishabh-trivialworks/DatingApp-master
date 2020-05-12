package com.quintus.labs.datingapp.helper;

import android.content.Context;
import android.os.Vibrator;

public class Utility {
    public static void vibrate(Context context, int durationMillis) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(durationMillis);
    }
}
