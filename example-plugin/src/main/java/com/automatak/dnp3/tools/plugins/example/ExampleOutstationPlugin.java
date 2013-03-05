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
package com.automatak.dnp3.tools.plugins.example;

import com.automatak.dnp3.*;
import com.automatak.dnp3.tools.pluginapi.OutstationPlugin;

class ExampleOutstationPlugin implements OutstationPlugin {

    private final DatabaseConfig database = new DatabaseConfig(2,2,0,0,0);
    private DataObserver publisher = null;
    private ExampleOutstationUI ui = null;

    public ExampleOutstationPlugin()
    {

    }

    @Override
    public void shutdown()
    {
        ui.setVisible(false);
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
        this.ui = new ExampleOutstationUI(publisher);
        this.ui.pack();
    }

    @Override
    public boolean hasUiComponent()
    {
        return true;
    }

    @Override
    public void showUi()
    {
        ui.setVisible(true);
    }

    @Override
    public CommandHandler getCommandHandler()
    {
        return new CommandHandler() {

            private CommandStatus validateCROB(ControlRelayOutputBlock command) {
                switch(command.function) {
                    case LATCH_OFF:
                        return CommandStatus.SUCCESS;
                    case LATCH_ON:
                        return CommandStatus.SUCCESS;
                    default:
                        return CommandStatus.NOT_SUPPORTED;
                }
            }

            private CommandStatus operateAnalog(double value, long index) {
                if(index == 0) {
                    ui.setAnalogOutput1(value);
                    return CommandStatus.SUCCESS;
                }
                else if(index == 1) {
                    ui.setAnalogOutput2(value);
                    return CommandStatus.SUCCESS;
                }
                else return CommandStatus.NOT_SUPPORTED;
            }

            private CommandStatus validateAnalog(double value, long index) {
                if(index == 0) {
                    return CommandStatus.SUCCESS;
                }
                else if(index == 1) {
                    return CommandStatus.SUCCESS;
                }
                else return CommandStatus.NOT_SUPPORTED;
            }

            @Override
            public CommandStatus select(ControlRelayOutputBlock command, long index) {
                CommandStatus validation = validateCROB(command);
                if(validation == CommandStatus.SUCCESS) {
                    if(index == 0) return CommandStatus.SUCCESS;
                    else if(index == 1) return CommandStatus.SUCCESS;
                    else return CommandStatus.NOT_SUPPORTED;
                }
                else return CommandStatus.NOT_SUPPORTED;
            }

            @Override
            public CommandStatus select(AnalogOutputInt32 command, long index) {
                return validateAnalog(command.value, index);
            }

            @Override
            public CommandStatus select(AnalogOutputInt16 command, long index) {
                return validateAnalog(command.value, index);
            }

            @Override
            public CommandStatus select(AnalogOutputFloat32 command, long index) {
                return validateAnalog(command.value, index);
            }

            @Override
            public CommandStatus select(AnalogOutputDouble64 command, long index) {
                return validateAnalog(command.value, index);
            }

            @Override
            public CommandStatus operate(ControlRelayOutputBlock command, long index) {
                CommandStatus validation = validateCROB(command);
                if(validation == CommandStatus.SUCCESS) {
                    if(index == 0) {
                        ui.setBinaryOutput1(command.function == ControlCode.LATCH_ON);
                        return CommandStatus.SUCCESS;
                    }
                    else if(index == 1) {
                        ui.setBinaryOutput2(command.function == ControlCode.LATCH_ON);
                        return CommandStatus.SUCCESS;
                    }
                    else return CommandStatus.NOT_SUPPORTED;
                }
                else return CommandStatus.NOT_SUPPORTED;
            }

            @Override
            public CommandStatus operate(AnalogOutputInt32 command, long index) {
                return operateAnalog(command.value, index);
            }

            @Override
            public CommandStatus operate(AnalogOutputInt16 command, long index) {
                return operateAnalog(command.value, index);
            }

            @Override
            public CommandStatus operate(AnalogOutputFloat32 command, long index) {
                return operateAnalog(command.value, index);
            }

            @Override
            public CommandStatus operate(AnalogOutputDouble64 command, long index) {
                return operateAnalog(command.value, index);
            }

            @Override
            public CommandStatus directOperate(ControlRelayOutputBlock command, long index) {
                CommandStatus validation = validateCROB(command);
                if(validation == CommandStatus.SUCCESS) {
                    if(index == 0) {
                        ui.setBinaryOutput1(command.function == ControlCode.LATCH_ON);
                        return CommandStatus.SUCCESS;
                    }
                    else if(index == 1) {
                        ui.setBinaryOutput2(command.function == ControlCode.LATCH_ON);
                        return CommandStatus.SUCCESS;
                    }
                    else return CommandStatus.NOT_SUPPORTED;
                }
                else return CommandStatus.NOT_SUPPORTED;
            }

            @Override
            public CommandStatus directOperate(AnalogOutputInt32 command, long index) {
                return operateAnalog(command.value, index);
            }

            @Override
            public CommandStatus directOperate(AnalogOutputInt16 command, long index) {
                return operateAnalog(command.value, index);
            }

            @Override
            public CommandStatus directOperate(AnalogOutputFloat32 command, long index) {
                return operateAnalog(command.value, index);
            }

            @Override
            public CommandStatus directOperate(AnalogOutputDouble64 command, long index) {
                return operateAnalog(command.value, index);
            }
        };
    }
}
