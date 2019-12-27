package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.*;
import lombok.Data;


@Data
public class AurEvent extends AurIdEntity {
    @Key("folderId")
    String calendarId;

    @Key
    private String subject;

    @Key
    private boolean descriptionOmitted;

    @Key
    private String description;

    @Key
    private EventDateTime start;

    @Key
    private EventDateTime end;

    @Key
    private String location;

    @Key
    private Organizer organizer;

    @Key
    private MeetingInfo meetingInfo;

//    @Key
//    private String recurrenceType;

    @Key
    private Recurrence recurrence;

    @Key
    private OccurrenceInfo occurrenceInfo;

    @Key
    private String iCalUId;

    @Key
    private String globalId;

    @Key
    private String showAs;

    @Key
    private String sensitivity;

    @Key
    private String[] categories;
}
