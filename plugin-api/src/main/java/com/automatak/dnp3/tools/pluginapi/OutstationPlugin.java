package com.automatak.dnp3.tools.pluginapi;

import com.automatak.dnp3.CommandHandler;
import com.automatak.dnp3.DataObserver;
import com.automatak.dnp3.DatabaseConfig;
import com.automatak.dnp3.OutstationConfig;

public interface OutstationPlugin {

    CommandHandler getCommandHandler();

    DatabaseConfig getDatabaseConfig();

    void setDataObserver(DataObserver publisher);

    boolean hasUiComponent();

    void showUi();

}
