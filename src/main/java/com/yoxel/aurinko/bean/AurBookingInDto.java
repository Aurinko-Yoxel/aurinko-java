package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurAvailabilityIntervals;
import com.yoxel.aurinko.bean.sub.AurWeekWorkSchedule;
import lombok.Data;

import java.time.Instant;
import java.time.Period;

@Data
public class AurBookingInDto extends GenericJson {
    @Key
    String name;

    @Key
    Integer availabilityStep;

    @Key
    Integer durationMinutes;

    @Key
    Integer bufferBetweenMinutes;

    @Key
    Integer startAfterMinutes;

    @Key
    Instant startTime;

    @Key
    Instant endTime;

    @Key
    Period timeAvailableFor;

    @Key
    String subject;

    @Key
    String description;

    @Key
    String location;

    @Key
    AurWeekWorkSchedule workHours;

    @Key
    AurAvailabilityIntervals availabilityIntervals;

    @Key
    String context;

    @Key
    Boolean startConference;

    @Key
    String openMeetingUrl;

    @Key
    String clientOrgId;
}
