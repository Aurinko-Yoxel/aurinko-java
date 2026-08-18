package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurDayWorkSchedule;
import lombok.Data;

import java.util.List;

@Data
public class AurWeekWorkScheduleResponse extends GenericJson {
    @Key
    List<AurDayWorkSchedule> daySchedules;

    @Key
    String timezone;

    @Key
    String source;
}
