package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.tools.pluginapi.MasterPluginFactory;
import com.automatak.dnp3.tools.pluginapi.OutstationPluginFactory;

import java.util.LinkedList;
import java.util.List;

public class PluginConfiguration {

    private final List<OutstationPluginFactory> outstations;
    private final List<MasterPluginFactory> masters;

    public PluginConfiguration()
    {
        outstations = new LinkedList<OutstationPluginFactory>();
        masters = new LinkedList<MasterPluginFactory>();
    }

    public PluginConfiguration(List<OutstationPluginFactory> outstations, List<MasterPluginFactory> masters) {
        this.outstations = outstations;
        this.masters = masters;
    }

    public List<OutstationPluginFactory> getOutstations() {
        return outstations;
    }

    public List<MasterPluginFactory> getMasters() {
        return masters;
    }
}
