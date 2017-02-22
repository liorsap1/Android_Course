package com.example.and.mysignal;

/**
 * Created by and on 2/22/2017.
 */

import android.content.Context;
import android.provider.BaseColumns;



public final class Points {
    private Points() {
        throw new AssertionError("can't create Points class");
    }


    public static abstract class MapPointsLocation implements BaseColumns {
        public static final String TABLE_NAME = "locationTable";
        public static final String Longtitude = "0.0";
        public static final String Altitude = "0.0";
        public static final String Strength = "0";
        public static final String TYPE= "type";

    }



}
