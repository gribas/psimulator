package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.LogLevel;

public interface AddTcpListener {

    void onAdd(String loggerId, LogLevel level, int retryMs, String host, int port);

}
