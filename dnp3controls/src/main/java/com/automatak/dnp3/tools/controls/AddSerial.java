package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.LogLevel;
import com.automatak.dnp3.SerialSettings;

public interface AddSerial {
    void onAdd(LogLevel level, int retryMs, SerialSettings settings);
}
