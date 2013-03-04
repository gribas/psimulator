package com.automatak.dnp3.tools.plugins.example;

import com.automatak.dnp3.tools.pluginapi.OutstationPlugin;
import com.automatak.dnp3.tools.pluginapi.OutstationPluginFactory;

public class ExampleOutstationPluginFactory implements OutstationPluginFactory {

    @Override
    public String getPluginName()
    {
        return "Example Outstation Plugin";
    }

    @Override
    public boolean requiresConfigurationString()
    {
        return false;
    }

    @Override
    public OutstationPlugin newOutstationInstance(String configuration)
    {
        return new ExampleOutstationPlugin();
    }

}
