package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.AurCreateMeetingDto;
import lombok.Data;

import java.time.Instant;

@Data
public class AurEventScheduleDetailsOutDto extends GenericJson {

    @Key
    Instant startTime;

    @Key
    Instant endTime;

    @Key
    AurCreateMeetingDto meetingDto;

    @Key
    Boolean deleted;
}
