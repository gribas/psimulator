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
package com.automatak.dnp3.tools.plugins.example;

import com.automatak.dnp3.*;
import com.automatak.dnp3.mock.SuccessCommandHandler;
import com.automatak.dnp3.tools.pluginapi.OutstationPlugin;

class ExampleOutstationPlugin implements OutstationPlugin {

    private DataObserver publisher = null;
    private final DatabaseConfig database = new DatabaseConfig(5,0,0,0,0);

    public ExampleOutstationPlugin()
    {

    }

    @Override
    public CommandHandler getCommandHandler()
    {
        return SuccessCommandHandler.getInstance();
    }

    @Override
    public DatabaseConfig getDatabaseConfig()
    {
        return database;
    }

    @Override
    public void setDataObserver(DataObserver publisher)
    {
        this.publisher = publisher;
        publisher.start();
        publisher.update(new BinaryInput(true, BinaryInputQuality.ONLINE.toByte(), 0), 0);
        publisher.end();
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
}
