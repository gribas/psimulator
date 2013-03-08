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

import com.automatak.dnp3.LogEntry;
import com.automatak.dnp3.LogSubscriber;
import com.automatak.dnp3.tools.pluginapi.StaticResources;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class LogToFile implements LogSubscriber {

    private interface Message {}
    private class LogMessage implements Message
    {
        public final LogEntry entry;

        public LogMessage(LogEntry entry)
        {
            this.entry = entry;
        }
    }
    private class StopMessage implements Message
    {}
    private class ChangeFileMessage implements Message
    {
        public final File file;
        public ChangeFileMessage(File file)
        {
            this.file = file;
        }
    }

    private class EnableDisableMessage implements Message
    {
        public final boolean isEnabled;

        public EnableDisableMessage(boolean isEnabled)
        {
            this.isEnabled = isEnabled;
        }
    }

    private FileWriter fw = null;
    private final Thread thread;
    private final BlockingQueue<Message> queue = new LinkedBlockingDeque<Message>();
    private boolean isEnabled = false;

    private void runLogs()
    {
        while(true)
        {
            try {
                Message msg = queue.poll(1, TimeUnit.DAYS);
                if(msg != null) {
                    if(!handleMessage(msg)) return;
                }
            }
            catch(InterruptedException ex)
            {

            }
        }
    }

    private final boolean handleMessage(Message msg)
    {
        if(LogMessage.class.isInstance(msg)) {
            handleLogMessage((LogMessage) msg);
            return true;
        }
        else if(ChangeFileMessage.class.isInstance(msg)) {
            handleChangeFileMessage((ChangeFileMessage) msg);
            return true;
        }
        else if(StopMessage.class.isInstance(msg))
        {
            closeWriter();
            return false;
        } else if(EnableDisableMessage.class.isInstance(msg)){
            this.isEnabled = ((EnableDisableMessage) msg).isEnabled;
            return true;
        } else {
            return true;
        }
    }

    private void closeWriter()
    {
        if(fw != null) {
            try {
                fw.close();
                fw = null;
            }
            catch(IOException ex)
            {
                System.err.println("Unable to close file: " + ex);
            }
        }
    }

    private void handleChangeFileMessage(ChangeFileMessage msg)
    {
        closeWriter();
        try {
            fw = new FileWriter(msg.file, true);
        }
        catch(IOException ex)
        {
            System.err.println("Unable to open log file: " + msg.file + " - " + ex.getMessage());
        }
    }

    private void handleLogMessage(LogMessage msg)
    {
         if(fw != null && isEnabled)
         {
             try {
                 String timestamp = StaticResources.defaulUTCDateFormat.format(msg.entry.getTimestamp());
                 fw.write(timestamp + " - " + msg.entry.getLogLevel() + " - " + msg.entry.getLoggerName() + " - " + msg.entry.getMessage() + System.getProperty("line.separator"));
             }
             catch(IOException ex)
             {
                 System.err.println("Unable to write log file: " + ex.getMessage());
             }
         }
    }

    public LogToFile()
    {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runLogs();
            }
        });
        thread.start();
    }

    public void changeFiles(File file)
    {
        this.putInQueue(new ChangeFileMessage(file));
    }

    public void setEnabled(boolean isEnabled)
    {
        this.putInQueue(new EnableDisableMessage(isEnabled));
    }

    public void stop()
    {
        if(thread != null)
        {
            putInQueue(new StopMessage());
            try {
                thread.join();
            }
            catch(InterruptedException ex)
            {
                System.err.println("Interrupt while shutting down: " + ex.getMessage());
            }
        }
    }

    private void putInQueue(Message msg)
    {
        try {
            queue.put(msg);
        }
        catch(InterruptedException ex)
        {
            System.err.println("Unable to queue item: " + ex.getMessage());
        }
    }

    @Override
    public void onLogEntry(LogEntry entry)
    {
        putInQueue(new LogMessage(entry));
    }
}
