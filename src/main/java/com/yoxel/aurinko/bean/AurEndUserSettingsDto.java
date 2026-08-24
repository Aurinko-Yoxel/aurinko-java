package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurWeekWorkSchedule;
import lombok.Data;

@Data
public class AurEndUserSettingsDto extends GenericJson {

    @Key
    private AurWeekWorkSchedule workHours;
}
