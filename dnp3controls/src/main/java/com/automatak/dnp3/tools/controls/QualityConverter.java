package com.automatak.dnp3.tools.controls;

import com.automatak.dnp3.BinaryInputQuality;

public class QualityConverter {

    public static String getBinaryQualityPneumonic(BinaryInputQuality quality)
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
            case CHATTER_FILTER:
                return "Cf";
            case RESERVED:
                return "V1";
            case STATE:
                return "S";
            default:
                return "INVALID";
        }
    }

    public static String getBinaryQualitySummary(byte quality)
    {
        StringBuilder sb = new StringBuilder();
        for(BinaryInputQuality q: BinaryInputQuality.getValuesInBitField(quality))
        {
            sb.append(getBinaryQualityPneumonic(q));
        }
        return sb.toString();
    }

    public static String getBinaryQualityDescription(byte quality)
    {
        StringBuilder sb = new StringBuilder();
        for(BinaryInputQuality q: BinaryInputQuality.getValuesInBitField(quality))
        {
            sb.append(q.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
