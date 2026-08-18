package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;

@Data
public class AurDayWorkSchedule extends GenericJson {
    @Key
    DayOfWeek dayOfWeek;

    @Key
    List<AurWorkingInterval> workingIntervals;
}
