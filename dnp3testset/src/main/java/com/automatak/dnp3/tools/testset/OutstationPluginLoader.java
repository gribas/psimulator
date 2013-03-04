package com.automatak.dnp3.tools.testset;

import com.automatak.dnp3.tools.pluginapi.OutstationPluginFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

class OutstationPluginLoader {

    List<OutstationPluginFactory> loadOutstationPlugins(PluginLoaderListener listener)
    {
        List<OutstationPluginFactory> factories = new LinkedList<OutstationPluginFactory>();

        File folder = new File("./plugins");
        File[] jars = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        if(jars != null) {
            for(int i=0; i< jars.length; ++i) {
                listener.onProgressUpdate(i, jars.length);
            }
        }

        return factories;
    }

}
