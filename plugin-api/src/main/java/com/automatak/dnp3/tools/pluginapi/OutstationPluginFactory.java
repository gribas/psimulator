package com.automatak.dnp3.tools.pluginapi;

import com.automatak.dnp3.DataObserver;

public interface OutstationPluginFactory {

    String getPluginName();

    boolean requiresConfigurationString();

    OutstationPlugin newOutstationInstance(String configuration);

}
