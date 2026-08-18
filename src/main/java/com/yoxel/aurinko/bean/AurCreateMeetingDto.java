package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurAttendeeInfo;
import com.yoxel.aurinko.bean.sub.AurSelectedMeetingTime;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AurCreateMeetingDto extends GenericJson {

    @Key
    AurSelectedMeetingTime time;

    @Key
    List<String> groupXids;

    @Key
    List<Long> accountIds;

    @Key
    String name;

    @Key
    String email;

    @Key
    Map<String, String> substitutionData;

    @Key
    AurAttendeeInfo schedulerInfo;

    @Key
    List<AurAttendeeInfo> invitees;
}
