package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class AurWeekWorkSchedule extends GenericJson {

    @Key
    List<AurDayWorkSchedule> daySchedules;

    @Key
    String timezone;
}
