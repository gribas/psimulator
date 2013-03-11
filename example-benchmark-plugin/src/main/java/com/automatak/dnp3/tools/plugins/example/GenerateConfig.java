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

import com.automatak.dnp3.tools.pluginapi.MasterPluginFactory;
import com.automatak.dnp3.tools.pluginapi.OutstationPluginFactory;
import com.automatak.dnp3.tools.plugins.example.master.BenchmarkMasterPluginFactory;
import com.automatak.dnp3.tools.plugins.example.outstation.BenchmarkOutstationPluginFactory;
import com.automatak.dnp3.tools.xml.ConfigGenerator;
import com.automatak.dnp3.tools.xml.SimulatorOptions;
import com.automatak.dnp3.tools.xml.XChannel;
import com.automatak.dnp3.tools.xml.XSimulatorConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class GenerateConfig {

    public static void main(String[] args) throws JAXBException
    {
        XSimulatorConfig cfg = getPairedConfig(250);
        SimulatorOptions options = new SimulatorOptions();
        cfg.setXSimulatorOptions(options.getOptions());
        JAXBContext ctx = JAXBContext.newInstance(XSimulatorConfig.class);
        Marshaller m = ctx.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(cfg, new File("./benchmarkConfig.xml"));
    }

    static XSimulatorConfig getPairedConfig(int pairs)
    {
        MasterPluginFactory mfac = new BenchmarkMasterPluginFactory();
        OutstationPluginFactory ofac = new BenchmarkOutstationPluginFactory();

        XSimulatorConfig config = new XSimulatorConfig();
        for(int i=0; i<pairs; ++i)
        {
            int port = 20000 + i;
            XChannel client = ConfigGenerator.getTcpClient(port);
            XChannel server = ConfigGenerator.getTcpServer(port);
            client.getXStack().add(ConfigGenerator.getMasterStack(i, mfac.getPluginName(), mfac.getDefaultConfig()));
            server.getXStack().add(ConfigGenerator.getOutstationStack(i, ofac.getPluginName(), ofac.getDefaultConfig()));
            config.getXChannel().add(client);
            config.getXChannel().add(server);
        }
        return config;
    }

}
