package com.automatak.dnp3.tools.pluginapi;

import com.automatak.dnp3.BinaryInputQuality;

public interface QualityConverter {

    public String getQualitySummary(byte quality);

    public String getQualityDescription(byte quality);

}
