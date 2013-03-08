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
package com.automatak.dnp3.tools.plugins.example.outstationgui;

import com.automatak.dnp3.DatabaseConfig;
import com.automatak.dnp3.EventBinaryResponse;
import com.automatak.dnp3.OutstationStackConfig;
import com.automatak.dnp3.tools.pluginapi.OutstationPlugin;
import com.automatak.dnp3.tools.pluginapi.OutstationPluginFactory;

public class ExampleGuiOutstationPluginFactory implements OutstationPluginFactory {

    private final static DatabaseConfig database = new DatabaseConfig(2,2,0,0,0);

    @Override
    public String getPluginName()
    {
        return "Example GUI Outstation";
    }

    @Override
    public boolean hasUi()
    {
        return false;
    }

    @Override
    public void showUi()
    {}

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
        return new ExampleGuiOutstationPlugin();
    }

}
