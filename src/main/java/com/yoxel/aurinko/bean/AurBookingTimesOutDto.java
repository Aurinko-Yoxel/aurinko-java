package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurAdditionalField;
import com.yoxel.aurinko.bean.sub.AurBookingMeetingInterval;
import com.yoxel.aurinko.bean.sub.AurEventScheduleDetailsOutDto;
import lombok.Data;

import java.util.List;

@Data
public class AurBookingTimesOutDto extends GenericJson {

    @Key
    List<AurBookingMeetingInterval> items;

    @Key
    DateTime startTime;

    @Key
    DateTime endTime;

    @Key
    String timeAvailableFor;

    @Key
    Integer durationMinutes;

    @Key
    Integer availabilityStep;

    @Key
    String subject;

    @Key
    Integer primaryColor;

    @Key
    Integer secondaryColor;

    @Key
    List<AurAdditionalField> additionalFields;

    @Key
    String nextPageToken;

    @Key
    Integer limit;

    @Key
    Integer offset;

    @Key
    Boolean done;

    @Key
    Long totalSize;

    @Key
    String nextFromDate;

    @Key
    AurEventScheduleDetailsOutDto existingMeeting;
}
