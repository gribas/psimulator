package com.automatak.dnp3.tools.testset;

public interface PluginLoaderListener {

    void onProgressUpdate(int num, int max);

    void onException(Exception ex);

}
