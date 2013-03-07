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

import com.automatak.dnp3.*;

public class XMLConversions {

    public static XLogLevel convert(LogLevel level)
    {
        switch(level)
        {
            case DEBUG: return XLogLevel.DEBUG;
            case COMM: return XLogLevel.COMM;
            case INTERPRET: return XLogLevel.INTERPRET;
            case INFO: return XLogLevel.INFO;
            case WARNING: return XLogLevel.WARNING;
            case ERROR: return XLogLevel.ERROR;
            case EVENT: return XLogLevel.EVENT;
            default:
                throw new IllegalArgumentException(level.toString());
        }
    }

    public static LogLevel convert(XLogLevel level)
    {
        switch(level)
        {
            case DEBUG: return LogLevel.DEBUG;
            case COMM: return LogLevel.COMM;
            case INTERPRET: return LogLevel.INTERPRET;
            case INFO: return LogLevel.INFO;
            case WARNING: return LogLevel.WARNING;
            case ERROR: return LogLevel.ERROR;
            case EVENT: return LogLevel.EVENT;
            default:
                throw new IllegalArgumentException(level.toString());
        }
    }

    public static XFLowControl convert(FlowControl flow)
    {
        switch(flow)
        {
            case FLOW_NONE: return XFLowControl.NONE;
            case FLOW_HARDWARE: return XFLowControl.HARDWARE;
            case FLOW_XONXOFF: return XFLowControl.XON_XOFF;
            default:
                throw new IllegalArgumentException(flow.toString());
        }
    }

    public static FlowControl convert(XFLowControl flow)
    {
        switch(flow)
        {
            case NONE: return FlowControl.FLOW_NONE;
            case HARDWARE: return FlowControl.FLOW_HARDWARE;
            case XON_XOFF: return FlowControl.FLOW_XONXOFF;
            default:
                throw new IllegalArgumentException(flow.toString());
        }
    }

    public static XParity convert(Parity parity)
    {
        switch(parity)
        {
            case PAR_NONE: return XParity.NONE;
            case PAR_EVEN: return XParity.EVEN;
            case PAR_ODD: return XParity.ODD;
            default:
                throw new IllegalArgumentException(parity.toString());
        }
    }

    public static Parity convert(XParity parity)
    {
        switch(parity)
        {
            case NONE: return Parity.PAR_NONE;
            case EVEN: return Parity.PAR_EVEN;
            case ODD: return Parity.PAR_ODD;
            default:
                throw new IllegalArgumentException(parity.toString());
        }
    }

    public static SerialSettings convert(XChannel.XSerialChannel channel)
    {
        String port = channel.getPort();
        int baud = channel.getBaud();
        int dataBits = channel.getDataBits();
        Parity parity = convert(channel.getParity());
        int stopBits = channel.getStopBits();
        FlowControl flow = convert(channel.getFlowControl());
        SerialSettings s = new SerialSettings(port, baud, dataBits, parity, stopBits, flow);
        return s;
    }

    public static XLinkLayer convert(LinkLayerConfig cfg)
    {
        XLinkLayer link = new XLinkLayer();
        link.setConfirmedDataRetry(cfg.numRetry);
        link.setLocalAddress(cfg.localAddr);
        link.setRemoteAddress(cfg.remoteAddr);
        link.setRspTimeout(cfg.timeoutMs);
        link.setUseConfirms(cfg.useConfirms);
        link.setIsMaster(cfg.isMaster);
        return link;
    }

    public static void convert(LinkLayerConfig link, XLinkLayer cfg)
    {
        link.localAddr = cfg.getLocalAddress();
        link.remoteAddr = cfg.getRemoteAddress();
        link.numRetry = cfg.getConfirmedDataRetry();
        link.timeoutMs = cfg.getRspTimeout();
        link.useConfirms = cfg.isUseConfirms();
    }

    public static XAppLayer convert(AppLayerConfig cfg)
    {
        XAppLayer app = new XAppLayer();
        app.setMaxRcvFragSize(cfg.maxFragSize);
        app.setRetryCount(cfg.numRetry);
        app.setRspTimeout(cfg.rspTimeoutMs);
        return app;
    }

    public static void convert(AppLayerConfig app, XAppLayer cfg)
    {
        app.maxFragSize = cfg.getMaxRcvFragSize();
        app.numRetry = cfg.getRetryCount();
        app.rspTimeoutMs = cfg.getRspTimeout();
    }

    public static XMaster convert(MasterConfig cfg)
    {
        XMaster master = new XMaster();
        master.setAllowTimeSync(cfg.allowTimeSync);
        master.setDoUnsolEnableDisable(cfg.doUnsolOnStartup);
        master.setEnableUnsol(cfg.enableUnsol);
        master.setIntegrityPeriod(cfg.integrityRateMs);
        master.setTaskRetryPeriod(cfg.taskRetryRateMs);
        master.setUnsolClassMask(cfg.unsolClassMask);
        return master;
    }

    public static void convert(MasterConfig m, XMaster cfg)
    {
        m.allowTimeSync = cfg.isAllowTimeSync();
        m.doUnsolOnStartup = cfg.isDoUnsolEnableDisable();
        m.enableUnsol = cfg.isEnableUnsol();
        m.integrityRateMs = cfg.getIntegrityPeriod();
        m.taskRetryRateMs = cfg.getTaskRetryPeriod();
        m.unsolClassMask = cfg.getUnsolClassMask();
    }

    public static XStaticBinary convert(StaticBinaryResponse rsp)
    {
        switch(rsp)
        {
            case GROUP1_VAR2:
                return XStaticBinary.GROUP_1_VAR_2;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static XStaticAnalog convert(StaticAnalogResponse rsp)
    {
        switch(rsp)
        {
            case GROUP30_VAR1:
                return XStaticAnalog.GROUP_30_VAR_1;
            case GROUP30_VAR2:
                return XStaticAnalog.GROUP_30_VAR_2;
            case GROUP30_VAR3:
                return XStaticAnalog.GROUP_30_VAR_3;
            case GROUP30_VAR4:
                return XStaticAnalog.GROUP_30_VAR_4;
            case GROUP30_VAR5:
                return XStaticAnalog.GROUP_30_VAR_5;
            case GROUP30_VAR6:
                return XStaticAnalog.GROUP_30_VAR_6;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static XStaticCounter convert(StaticCounterResponse rsp)
    {
        switch(rsp)
        {
            case GROUP20_VAR1:
                return XStaticCounter.GROUP_20_VAR_1;
            case GROUP20_VAR2:
                return XStaticCounter.GROUP_20_VAR_2;
            case GROUP20_VAR5:
                return XStaticCounter.GROUP_20_VAR_5;
            case GROUP20_VAR6:
                return XStaticCounter.GROUP_20_VAR_6;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static XStaticAnalogOutputStatus convert(StaticAnalogOutputStatusResponse rsp)
    {
        switch(rsp)
        {
            case GROUP40_VAR1:
                return XStaticAnalogOutputStatus.GROUP_40_VAR_1;
            case GROUP40_VAR2:
                return XStaticAnalogOutputStatus.GROUP_40_VAR_2;
            case GROUP40_VAR3:
                return XStaticAnalogOutputStatus.GROUP_40_VAR_3;
            case GROUP40_VAR4:
                return XStaticAnalogOutputStatus.GROUP_40_VAR_4;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static XEventBinary convert(EventBinaryResponse rsp)
    {
        switch(rsp)
        {
            case GROUP2_VAR1:
                return XEventBinary.GROUP_2_VAR_1;
            case GROUP2_VAR2:
                return XEventBinary.GROUP_2_VAR_2;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static XEventAnalog convert(EventAnalogResponse rsp)
    {
        switch(rsp)
        {
            case GROUP32_VAR1:
                return XEventAnalog.GROUP_32_VAR_1;
            case GROUP32_VAR2:
                return XEventAnalog.GROUP_32_VAR_2;
            case GROUP32_VAR3:
                return XEventAnalog.GROUP_32_VAR_3;
            case GROUP32_VAR4:
                return XEventAnalog.GROUP_32_VAR_4;
            case GROUP32_VAR5:
                return XEventAnalog.GROUP_32_VAR_5;
            case GROUP32_VAR6:
                return XEventAnalog.GROUP_32_VAR_6;
            case GROUP32_VAR7:
                return XEventAnalog.GROUP_32_VAR_7;
            case GROUP32_VAR8:
                return XEventAnalog.GROUP_32_VAR_8;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static XEventCounter convert(EventCounterResponse rsp)
    {
        switch(rsp)
        {
            case GROUP22_VAR1:
                return XEventCounter.GROUP_22_VAR_1;
            case GROUP22_VAR2:
                return XEventCounter.GROUP_22_VAR_2;
            case GROUP22_VAR5:
                return XEventCounter.GROUP_22_VAR_5;
            case GROUP22_VAR6:
                return XEventCounter.GROUP_22_VAR_6;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static StaticBinaryResponse convert(XStaticBinary rsp)
    {
        switch(rsp)
        {
            case GROUP_1_VAR_2:
                return StaticBinaryResponse.GROUP1_VAR2;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static StaticAnalogResponse convert(XStaticAnalog rsp)
    {
        switch(rsp)
        {
            case GROUP_30_VAR_1:
                return StaticAnalogResponse.GROUP30_VAR1;
            case GROUP_30_VAR_2:
                return StaticAnalogResponse.GROUP30_VAR2;
            case GROUP_30_VAR_3:
                return StaticAnalogResponse.GROUP30_VAR3;
            case GROUP_30_VAR_4:
                return StaticAnalogResponse.GROUP30_VAR4;
            case GROUP_30_VAR_5:
                return StaticAnalogResponse.GROUP30_VAR5;
            case GROUP_30_VAR_6:
                return StaticAnalogResponse.GROUP30_VAR6;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static StaticCounterResponse convert(XStaticCounter rsp)
    {
        switch(rsp)
        {
            case GROUP_20_VAR_1:
                return StaticCounterResponse.GROUP20_VAR1;
            case GROUP_20_VAR_2:
                return StaticCounterResponse.GROUP20_VAR2;
            case GROUP_20_VAR_5:
                return StaticCounterResponse.GROUP20_VAR5;
            case GROUP_20_VAR_6:
                return StaticCounterResponse.GROUP20_VAR6;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static StaticAnalogOutputStatusResponse convert(XStaticAnalogOutputStatus rsp)
    {
        switch(rsp)
        {
            case GROUP_40_VAR_1:
                return StaticAnalogOutputStatusResponse.GROUP40_VAR1;
            case GROUP_40_VAR_2:
                return StaticAnalogOutputStatusResponse.GROUP40_VAR2;
            case GROUP_40_VAR_3:
                return StaticAnalogOutputStatusResponse.GROUP40_VAR3;
            case GROUP_40_VAR_4:
                return StaticAnalogOutputStatusResponse.GROUP40_VAR4;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static EventBinaryResponse convert(XEventBinary rsp)
    {
        switch(rsp)
        {
            case GROUP_2_VAR_1:
                return EventBinaryResponse.GROUP2_VAR1;
            case GROUP_2_VAR_2:
                return EventBinaryResponse.GROUP2_VAR2;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static EventAnalogResponse convert(XEventAnalog rsp)
    {
        switch(rsp)
        {
            case GROUP_32_VAR_1:
                return EventAnalogResponse.GROUP32_VAR1;
            case GROUP_32_VAR_2:
                return EventAnalogResponse.GROUP32_VAR2;
            case GROUP_32_VAR_3:
                return EventAnalogResponse.GROUP32_VAR3;
            case GROUP_32_VAR_4:
                return EventAnalogResponse.GROUP32_VAR4;
            case GROUP_32_VAR_5:
                return EventAnalogResponse.GROUP32_VAR5;
            case GROUP_32_VAR_6:
                return EventAnalogResponse.GROUP32_VAR6;
            case GROUP_32_VAR_7:
                return EventAnalogResponse.GROUP32_VAR7;
            case GROUP_32_VAR_8:
                return EventAnalogResponse.GROUP32_VAR8;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static EventCounterResponse convert(XEventCounter rsp)
    {
        switch(rsp)
        {
            case GROUP_22_VAR_1:
                return EventCounterResponse.GROUP22_VAR1;
            case GROUP_22_VAR_2:
                return EventCounterResponse.GROUP22_VAR2;
            case GROUP_22_VAR_5:
                return EventCounterResponse.GROUP22_VAR5;
            case GROUP_22_VAR_6:
                return EventCounterResponse.GROUP22_VAR6;
            default:
                throw new IllegalArgumentException(rsp.toString());
        }
    }

    public static void convert(OutstationConfig os, XOutstation cfg)
    {
        os.disableUnsol = cfg.isDisableUnsol();
        os.unsolPackDelayMs = cfg.getUnsolTimer();
        os.staticBinaryInput = convert(cfg.getStaticBinary());
        os.staticAnalogInput = convert(cfg.getStaticAnalog());
        os.staticCounter = convert(cfg.getStaticCounter());
        os.staticAnalogOutputStatus = convert(cfg.getStaticAnalogOutputStatus());
        os.eventBinaryInput = convert(cfg.getEventBinary());
        os.eventAnalogInput = convert(cfg.getEventAnalog());
        os.eventCounter = convert(cfg.getEventCounter());
    }

    public static XOutstation convert(OutstationConfig cfg)
    {
        XOutstation os = new XOutstation();
        os.setDisableUnsol(cfg.disableUnsol);
        os.setUnsolTimer(cfg.unsolPackDelayMs);
        os.setStaticBinary(XMLConversions.convert(cfg.staticBinaryInput));
        os.setStaticAnalog(XMLConversions.convert(cfg.staticAnalogInput));
        os.setStaticCounter(XMLConversions.convert(cfg.staticCounter));
        os.setStaticAnalogOutputStatus(XMLConversions.convert(cfg.staticAnalogOutputStatus));
        os.setEventBinary(XMLConversions.convert(cfg.eventBinaryInput));
        os.setEventAnalog(XMLConversions.convert(cfg.eventAnalogInput));
        os.setEventCounter(XMLConversions.convert(cfg.eventCounter));
        return os;
    }
}
