package com.automatak.dnp3.tools.plugins.example;


import com.automatak.dnp3.MasterStackConfig;
import com.automatak.dnp3.OutstationStackConfig;
import com.automatak.dnp3.tools.pluginapi.MasterPluginFactory;
import com.automatak.dnp3.tools.pluginapi.OutstationPluginFactory;
import com.automatak.dnp3.tools.plugins.example.master.ExampleMasterPluginFactory;
import com.automatak.dnp3.tools.plugins.example.outstation.ExampleOutstationPluginFactory;
import com.automatak.dnp3.tools.xml.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

public class GenerateExampleConfigs {

    public static void main(String[] args) throws JAXBException
    {
       XSimulatorConfig cfg = getPairedConfig(500);
       JAXBContext ctx = JAXBContext.newInstance(XSimulatorConfig.class);
       Marshaller m = ctx.createMarshaller();
       m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
       m.marshal(cfg, new File("./output.xml"));
    }

    static XSimulatorConfig getPairedConfig(int pairs)
    {
        MasterPluginFactory mfac = new ExampleMasterPluginFactory();
        OutstationPluginFactory ofac = new ExampleOutstationPluginFactory();

        XSimulatorConfig config = new XSimulatorConfig();
        for(int i=0; i<pairs; ++i)
        {
           int port = 20000 + i;
           XChannel client = getTcpClient(port);
           XChannel server = getTcpServer(port);
           client.getXStack().add(getMasterStack(i, mfac));
           server.getXStack().add(getOutstationStack(i, ofac));
           config.getXChannel().add(client);
           config.getXChannel().add(server);
        }
        return config;
    }

    static XStack getMasterStack(int i, MasterPluginFactory plugin)
    {
        XStack stack = new XStack();
        XStack.XMasterStack s = new XStack.XMasterStack();
        MasterStackConfig cfg = plugin.getDefaultConfig();
        cfg.masterConfig.integrityRateMs = 5000;
        s.setId("master" + i);
        s.setLevel(XLogLevel.INFO);
        s.setPlugin(plugin.getPluginName());
        s.setXLinkLayer(XMLConversions.convert(cfg.linkConfig));
        s.setXAppLayer(XMLConversions.convert(cfg.appConfig));
        s.setXMaster(XMLConversions.convert(cfg.masterConfig));
        stack.setXMasterStack(s);
        return stack;
    }

    static XStack getOutstationStack(int i, OutstationPluginFactory plugin)
    {
        XStack stack = new XStack();
        XStack.XOutstationStack s = new XStack.XOutstationStack();
        OutstationStackConfig cfg = plugin.getDefaultConfig();
        s.setId("outstation" + i);
        s.setLevel(XLogLevel.INFO);
        s.setPlugin(plugin.getPluginName());
        s.setXLinkLayer(XMLConversions.convert(cfg.linkConfig));
        s.setXAppLayer(XMLConversions.convert(cfg.appConfig));
        s.setXOutstation(XMLConversions.convert(cfg.outstationConfig));
        stack.setXOutstationStack(s);
        return stack;
    }

    static XChannel getTcpClient(int port)
    {
        XChannel channel = new XChannel();
        XChannel.XTCPClientChannel client = new XChannel.XTCPClientChannel();
        client.setId("port: " + port);
        client.setIp("127.0.0.1");
        client.setLevel(XLogLevel.INFO);
        client.setPort(port);
        client.setRetry(5000L);
        channel.setXTCPClientChannel(client);
        return channel;
    }

    static XChannel getTcpServer(int port)
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
