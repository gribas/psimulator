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

import com.automatak.dnp3.*;
import com.automatak.dnp3.mock.SuccessCommandHandler;
import com.automatak.dnp3.tools.pluginapi.OutstationPlugin;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class ExampleOutstationPlugin implements OutstationPlugin {

    private DataObserver publisher = null;
    private final ExampleOutstationPluginFactory factory;
    private final DatabaseConfig db;

    public ExampleOutstationPlugin(ExampleOutstationPluginFactory factory, DatabaseConfig db)
    {
        this.factory = factory;
        this.db = db;
    }

    public void updateData(Random rand, long timestamp)
    {
        publisher.start();
        for(int i=0; i<db.binaryInputs.size(); ++ i)
        {
            publisher.update(new BinaryInput(rand.nextBoolean(), BinaryInputQuality.ONLINE.toByte(), timestamp), i);
        }
        for(int i=0; i<db.analogInputs.size(); ++i) {
            publisher.update(new AnalogInput(rand.nextInt(1000), AnalogInputQuality.ONLINE.toByte(), timestamp), i);
        }
        publisher.end();
    }

    @Override
    public void shutdown()
    {
        factory.unregister(this);
    }

    @Override
    public void configure(DataObserver publisher)
    {
        this.publisher = publisher;
        factory.register(this);
    }

    @Override
    public boolean hasUiComponent()
    {
        return false;
    }

    @Override
    public void showUi()
    {

    }

    @Override
    public CommandHandler getCommandHandler()
    {
        return SuccessCommandHandler.getInstance();
    }
}
