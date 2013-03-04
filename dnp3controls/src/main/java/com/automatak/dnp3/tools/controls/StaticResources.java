package com.automatak.dnp3.tools.controls;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class StaticResources {

    public static final Image dnpIcon = Toolkit.getDefaultToolkit().getImage(StaticResources.class.getResource("/images/icon.png"));
    public static final SimpleDateFormat defaulUTCDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    static {
        defaulUTCDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
}
