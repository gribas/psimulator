/**
 * Copyright 2013 Automatak, LLC
 *
 * Licensed to Automatak, LLC under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Automatak, LLC licenses this file to you
 * under the GNU Affero General Public License Version 3.0 (the "License");
 * you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/agpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.automatak.dnp3.tools.plugins.example.outstation;

import com.automatak.dnp3.DatabaseConfig;
import com.automatak.dnp3.EventBinaryResponse;
import com.automatak.dnp3.OutstationStackConfig;
import com.automatak.dnp3.tools.pluginapi.OutstationPlugin;
import com.automatak.dnp3.tools.pluginapi.OutstationPluginFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BenchmarkOutstationPluginFactory implements OutstationPluginFactory {

    private interface Message {}
    public class ShutdownMessage implements Message {}
    public class ChangeUpdateRate implements Message {

        public final long sleepMs;

        public ChangeUpdateRate(long sleepMs)
        {
            this.sleepMs = sleepMs;
        }
    }

    private final static DatabaseConfig database = new DatabaseConfig(500,500,500,0,0);
    private final List<BenchmarkOutstationPlugin> plugins = new LinkedList<BenchmarkOutstationPlugin>();
    private final BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();
    private final Random rand = new Random();
    private final BenchmarkOutstationFactoryUI form = new BenchmarkOutstationFactoryUI(this);
    private long sleepMs = 1000;
    private boolean isEnabled = false;
    private Thread thread = null;

    public BenchmarkOutstationPluginFactory()
    {
        form.pack();
    }

    @Override
    public boolean hasUi()
    {
        return true;
    }

    @Override
    public void showUi()
    {
       form.setVisible(true);
    }

    public void setUpdateRate(int updatesPerSec)
    {
        if(updatesPerSec == 0) {
            this.sendMsg(new ChangeUpdateRate(-1));
        }
        else {
            int sleepms = 1000/updatesPerSec;
            this.sendMsg(new ChangeUpdateRate(sleepms));
        }
    }

    public void register(BenchmarkOutstationPlugin plugin)
    {
        synchronized(plugins)
        {
            plugins.add(plugin);
            this.form.setNumOutstations(plugins.size());
            if(thread == null) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runUpdates();
                    }
                });
                thread.start();
            }
        }
    }

    @Override
    public void shutdown()
    {
        synchronized (plugins)
        {
            if(thread != null)
            {
               doThreadShutdown();
            }
            this.form.setVisible(false);
        }
    }

    public void unregister(BenchmarkOutstationPlugin plugin)
    {
        synchronized(plugins)
        {
            plugins.remove(plugin);
            this.form.setNumOutstations(plugins.size());
            if(thread != null && plugins.isEmpty())
            {
                doThreadShutdown();
            }
        }
    }

    private void doThreadShutdown()
    {
        sendMsg(new ShutdownMessage());
        try {
            thread.join();
            thread = null;
        }
        catch(InterruptedException ex)
        {
            System.err.println("Interrupted while joining");
        }
    }

    private void updateValues()
    {
       long timestamp = System.currentTimeMillis();
       int count = 0;
       synchronized(plugins)
       {
            for(BenchmarkOutstationPlugin plugin: plugins)
            {
               count += plugin.updateData(rand, timestamp);
            }
       }
       form.addUpdates(count);
    }

    private void sendMsg(Message m)
    {
        try {
            queue.put(m);
        }
        catch(InterruptedException ex)
        {
            System.err.println("Interrupt while putting item in queue: " + ex.getMessage());
        }
    }

    private void runUpdates()
    {
        while(true)
        {
            try {
               Message msg = queue.poll(sleepMs, TimeUnit.MILLISECONDS);
               if(msg == null) {
                   if(isEnabled) updateValues();
               } else {
                   if(ShutdownMessage.class.isInstance(msg))
                   {
                       return;
                   } else if(ChangeUpdateRate.class.isInstance(msg)) {
                       long sleep = ((ChangeUpdateRate) msg).sleepMs;
                       if(sleep < 0) {
                           isEnabled = false;
                           sleepMs = 60000;
                       }
                       else {
                         sleepMs = sleep;
                           isEnabled = true;
                       }
                   } else {
                       // bad msg type
                   }
               }

            }
            catch(InterruptedException ex)
            {

            }
        }
    }



    @Override
    public String getPluginName()
    {
        return "Benchmark Outstation";
    }

    @Override
    public OutstationStackConfig getDefaultConfig()
    {
        OutstationStackConfig config = new OutstationStackConfig(database);
        config.outstationConfig.eventBinaryInput = EventBinaryResponse.GROUP2_VAR2;
        return config;
    }

    @Override
    public boolean requiresConfigurationString()
    {
        return false;
    }

    @Override
    public OutstationPlugin newOutstationInstance(String configuration)
    {
        return new BenchmarkOutstationPlugin(this, database);
    }

}
