package com.automatak.dnp3.tools.plugins.example;

import com.automatak.dnp3.*;
import com.automatak.dnp3.mock.SuccessCommandHandler;
import com.automatak.dnp3.tools.pluginapi.OutstationPlugin;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

class ExampleOutstationPlugin implements OutstationPlugin {

    private DataObserver publisher = null;
    private final DatabaseConfig database = new DatabaseConfig(5,0,0,0,0);

    public ExampleOutstationPlugin()
    {

    }

    @Override
    public CommandHandler getCommandHandler()
    {
        return SuccessCommandHandler.getInstance();
    }

    @Override
    public DatabaseConfig getDatabaseConfig()
    {
        return database;
    }

    @Override
    public void setDataObserver(DataObserver publisher)
    {
        this.publisher = publisher;
    }

    @Override
    public boolean hasUiComponent()
    {
        return false;
    }

    @Override
    public void showUi()
    {
        throw new NotImplementedException();
    }
}
