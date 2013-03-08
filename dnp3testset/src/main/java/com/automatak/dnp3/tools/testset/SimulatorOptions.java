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
package com.automatak.dnp3.tools.testset;

import com.automatak.dnp3.tools.xml.XSimulatorOptions;

import java.io.File;

public class SimulatorOptions {

    public File getLogFile() {
        return logFile;
    }

    public boolean isLogToFile() {
        return logToFile;
    }

    public boolean isLogToTable() {
        return logToTable;
    }

    public int getMaxLogTableSize() {
        return maxLogTableSize;
    }

    private int maxLogTableSize = 500;
    private boolean logToTable = true;
    private boolean logToFile = false;

    public void setMaxLogTableSize(int maxLogTableSize) {
        this.maxLogTableSize = maxLogTableSize;
    }

    public void setLogToTable(boolean logToTable) {
        this.logToTable = logToTable;
    }

    public void setLogToFile(boolean logToFile) {
        this.logToFile = logToFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    private File logFile = new File("./simulator.log");

    public SimulatorOptions(int maxLogTableSize, boolean logToTable, boolean logToFile, File logFile)
    {
        this.maxLogTableSize = maxLogTableSize;
        this.logToTable = logToTable;
        this.logToFile = logToFile;
        this.logFile = logFile;
    }

    XSimulatorOptions getOptions()
    {
        XSimulatorOptions options = new XSimulatorOptions();
        options.setLogFile(logFile.toString());
        options.setLogToFile(logToFile);
        options.setLogToTable(logToTable);
        options.setMaxLogTableSize(maxLogTableSize);
        return options;
    }

    public void configure(XSimulatorOptions options)
    {
        this.maxLogTableSize = options.getMaxLogTableSize();
        this.logToTable = options.isLogToTable();
        this.logToFile = options.isLogToFile();
        this.logFile = new File(options.getLogFile());
    }

    public SimulatorOptions()
    {

    }
}
