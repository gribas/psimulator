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

import com.automatak.dnp3.CommandProcessor;
import com.automatak.dnp3.DataObserver;
import com.automatak.dnp3.MasterStackConfig;
import com.automatak.dnp3.tools.pluginapi.MasterPlugin;

public class ExampleMasterPlugin implements MasterPlugin {

    private final ExampleMasterUI form = new ExampleMasterUI("Example Master");

    @Override
    public DataObserver getDataObserver()
    {
       return form;
    }

    @Override
    public void configure(CommandProcessor processor)
    {
      form.configure(processor);
      form.pack();
    }

    @Override
    public boolean hasUiComponent()
    {
        return true;
    }

    @Override
    public void shutdown()
    {
        form.setVisible(false);
    }

    @Override
    public void showUi()
    {
      form.setVisible(true);
    }
}
