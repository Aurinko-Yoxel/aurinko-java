package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurAdditionalField;
import com.yoxel.aurinko.bean.sub.AurBookingMeetingInterval;
import com.yoxel.aurinko.bean.sub.AurEventScheduleDetailsOutDto;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Set;

@Data
public class AurBookingTimesOutDto extends GenericJson {

    @Key
    List<AurBookingMeetingInterval> items;

    @Key
    Instant startTime;

    @Key
    Instant endTime;

    @Key
    Period timeAvailableFor;

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
    Set<AurAdditionalField> additionalFields;

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
    LocalDate nextFromDate;

    @Key
    AurEventScheduleDetailsOutDto existingMeeting;
}
