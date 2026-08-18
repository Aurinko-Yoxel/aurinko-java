package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AurBookingMeetingInterval extends GenericJson {

    @Key
    Instant start;

    @Key
    Instant end;

    @Key
    List<String> groupXids;

    @Key
    List<Long> accountIds;
}
