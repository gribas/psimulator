package com.automatak.dnp3.tools.testset.ui;

import com.automatak.dnp3.LogLevel;
import com.automatak.dnp3.OutstationStackConfig;


public interface AddOutstationListener {
    void onAdd(String loggerID, LogLevel level, OutstationStackConfig config);
}
