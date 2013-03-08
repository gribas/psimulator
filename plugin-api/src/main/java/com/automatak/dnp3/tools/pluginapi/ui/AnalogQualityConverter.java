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
package com.automatak.dnp3.tools.pluginapi.ui;

import com.automatak.dnp3.AnalogInputQuality;

public class AnalogQualityConverter implements QualityConverter {

    public static AnalogQualityConverter getInstance() {
        return instance;
    }

    private static final AnalogQualityConverter instance = new AnalogQualityConverter();

    private AnalogQualityConverter()
    {}

    private static String getAnalogQualityPneumonic(AnalogInputQuality quality)
    {
        switch(quality)
        {
            case ONLINE:
                return "O";
            case RESTART:
                return "R";
            case COMM_LOST:
                return "Cl";
            case REMOTE_FORCED_DATA:
                return "Rf";
            case LOCAL_FORCED_DATA:
                return "Lf";
            case OVERRANGE:
                return "G";
            case REFERENCE_CHECK:
                return "H";
            default:
                return "INVALID";
        }
    }

    public String getQualitySummary(byte quality)
    {
        StringBuilder sb = new StringBuilder();
        for(AnalogInputQuality q: AnalogInputQuality.getValuesInBitField(quality))
        {
            sb.append(getAnalogQualityPneumonic(q));
        }
        return sb.toString();
    }

    public String getQualityDescription(byte quality)
    {
        StringBuilder sb = new StringBuilder();
        for(AnalogInputQuality q: AnalogInputQuality.getValuesInBitField(quality))
        {
            sb.append(q.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
