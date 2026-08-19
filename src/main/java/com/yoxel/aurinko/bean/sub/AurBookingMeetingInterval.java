package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class AurBookingMeetingInterval extends GenericJson {

    @Key
    DateTime start;

    @Key
    DateTime end;

    @Key
    List<String> groupXids;

    @Key
    List<Long> accountIds;
}
