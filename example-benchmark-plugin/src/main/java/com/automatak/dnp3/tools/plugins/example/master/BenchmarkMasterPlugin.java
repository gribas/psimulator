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
package com.automatak.dnp3.tools.plugins.example.master;

import com.automatak.dnp3.*;
import com.automatak.dnp3.tools.pluginapi.MasterPlugin;

public class BenchmarkMasterPlugin implements MasterPlugin {

    private final BenchmarkMasterPluginFactory factory;

    private class CountingDataObserver implements DataObserver {

        private int count = 0;

        @Override
        public void start()
        {
            count = 0;
        }

        @Override
        public void update(BinaryInput meas, long index)
        {
            ++count;
        }


        @Override
        public void update(AnalogInput meas, long index)
        {
            ++count;
        }


        @Override
        public void update(Counter meas, long index)
        {
            ++count;
        }


        @Override
        public void update(BinaryOutputStatus meas, long index)
        {
            ++count;
        }


        @Override
        public void update(AnalogOutputStatus meas, long index)
        {
            ++count;
        }

        @Override
        public void end()
        {
            factory.onTransaction(count);
        }
    }

    private final DataObserver observer = new CountingDataObserver();

    public BenchmarkMasterPlugin(BenchmarkMasterPluginFactory factory)
    {
        this.factory = factory;
    }

    @Override
    public DataObserver getDataObserver()
    {
         return observer;
    }

    @Override
    public void configure(CommandProcessor processor)
    {
        factory.register(this);
    }

    @Override
    public boolean hasUiComponent()
    {
        return false;
    }

    @Override
    public void shutdown()
    {

    }

    @Override
    public void showUi()
    {}
}
