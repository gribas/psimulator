package com.automatak.dnp3.tools.testset;

import com.automatak.dnp3.*;
import com.automatak.dnp3.tools.xml.*;
import com.sun.javaws.exceptions.InvalidArgumentException;

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

    public static XLinkLayer convert(LinkLayerConfig cfg)
    {
        XLinkLayer link = new XLinkLayer();
        link.setConfirmedDataRetry(cfg.numRetry);
        link.setLocalAddress(cfg.localAddr);
        link.setRemoteAddress(cfg.remoteAddr);
        link.setRspTimeout(cfg.timeoutMs);
        link.setUseConfirms(cfg.useConfirms);
        return link;
    }

    public static XAppLayer convert(AppLayerConfig cfg)
    {
        XAppLayer app = new XAppLayer();
        app.setMaxRcvFragSize(cfg.maxFragSize);
        app.setRetryCount(cfg.numRetry);
        app.setRspTimeout(cfg.rspTimeoutMs);
        return app;
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
