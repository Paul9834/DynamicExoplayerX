/*
 *
 *  * Copyright (c) 2020. [Kevin Paul Montealegre Melo]
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *
 */

package com.paul9834.dynamicexoplayer.androidx.Others;

import android.text.format.DateUtils;
import com.paul9834.dynamicexoplayer.androidx.EPG.model.ChannelData;
import com.paul9834.dynamicexoplayer.androidx.EPG.model.ProgramData;

import java.util.*;

public class MockDataProviderMain {

    public static long startOfToday;
    public static long endOfToday;

    public static LinkedHashMap<ChannelData, List<ProgramData>> prepareMockData() {

        String channelStr = Locale.getDefault().getLanguage().equals("en") ? "Channel " : "قناة ";
        String programStr = Locale.getDefault().getLanguage().equals("en") ? "Program " : "برنامج ";


        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        startOfToday = now.getTimeInMillis();

        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.MILLISECOND, 999);
        endOfToday = now.getTimeInMillis();

        Random random = new Random();

        LinkedHashMap<ChannelData, List<ProgramData>> data = new LinkedHashMap<>();



        for (int channelIndex = 0; channelIndex < 4; channelIndex++) {

            ChannelData channelData = new ChannelData();
            channelData.setChannelName(channelStr + channelIndex);
            channelData.setChannelNumber(channelIndex);

            long timeProgress = startOfToday;

            List<ProgramData> channelPrograms = new ArrayList<>();

            while (timeProgress < endOfToday) {

                ProgramData program = new ProgramData();

                program.setTitle(channelData.getChannelName() + " " + programStr + channelPrograms.size());
                program.setStartTime(timeProgress);

                //minimum 5 minutes and max 120 min
                int durationMin = 5 + random.nextInt(120);
                timeProgress += (durationMin * DateUtils.MINUTE_IN_MILLIS);
                program.setEndTime(timeProgress);

                channelPrograms.add(program);
            }

            data.put(channelData, channelPrograms);
        }




        ChannelData channelData = new ChannelData();
        channelData.setChannelName(channelStr + 4);
        channelData.setChannelNumber(4);

        data.put(channelData, null);

        return data;
    }
}
