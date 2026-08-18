package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.time.Instant;

@Data
public class AurSelectedMeetingTime extends GenericJson {
    @Key
    Instant start;

    @Key
    Instant end;
}
