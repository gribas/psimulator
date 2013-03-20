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
package com.automatak.dnp3.tools.xml;


import com.automatak.dnp3.MasterStackConfig;
import com.automatak.dnp3.OutstationStackConfig;

public class ConfigGenerator {

    public static XStack getMasterStack(int i, String plugin,  MasterStackConfig config)
    {
        XStack stack = new XStack();
        XStack.XMasterStack s = new XStack.XMasterStack();
        s.setId("master" + i);
        s.setLevel(XLogLevel.INFO);
        s.setPlugin(plugin);
        s.setXLinkLayer(XMLConversions.convert(config.linkConfig));
        s.setXAppLayer(XMLConversions.convert(config.appConfig));
        s.setXMaster(XMLConversions.convert(config.masterConfig));
        stack.setXMasterStack(s);
        return stack;
    }

    public static XStack getOutstationStack(int i, String plugin, OutstationStackConfig config)
    {
        XStack stack = new XStack();
        XStack.XOutstationStack s = new XStack.XOutstationStack();
        s.setId("outstation" + i);
        s.setLevel(XLogLevel.INFO);
        s.setPlugin(plugin);
        s.setXLinkLayer(XMLConversions.convert(config.linkConfig));
        s.setXAppLayer(XMLConversions.convert(config.appConfig));
        s.setXOutstation(XMLConversions.convert(config.outstationConfig));
        stack.setXOutstationStack(s);
        return stack;
    }

    public static XChannel getTcpClient(String ip, int port)
    {
        XChannel channel = new XChannel();
        XChannel.XTCPClientChannel client = new XChannel.XTCPClientChannel();
        client.setId("port: " + port);
        client.setIp(ip);
        client.setLevel(XLogLevel.INFO);
        client.setPort(port);
        client.setRetry(5000L);
        channel.setXTCPClientChannel(client);
        return channel;
    }

    public static XChannel getTcpServer(int port)
    {
        XChannel channel = new XChannel();
        XChannel.XTCPServerChannel server = new XChannel.XTCPServerChannel();
        server.setId("port: " + port);
        server.setIp("0.0.0.0");
        server.setLevel(XLogLevel.INFO);
        server.setPort(port);
        server.setRetry(5000L);
        channel.setXTCPServerChannel(server);
        return channel;
    }

}
