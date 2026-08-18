package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.AurCreateMeetingDto;
import lombok.Data;

@Data
public class AurEventScheduleDetailsOutDto extends GenericJson {

    @Key
    DateTime startTime;

    @Key
    DateTime endTime;

    @Key
    AurCreateMeetingDto meetingDto;

    @Key
    Boolean deleted;
}
