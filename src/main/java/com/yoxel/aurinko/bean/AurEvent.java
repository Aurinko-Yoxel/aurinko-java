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
    private EventDateTime start;

    @Key
    private EventDateTime end;

    @Key
    private String location;

    @Key
    private boolean meeting;

    @Key
    private boolean cancelled;

    @Key
    private String response;

    @Key
    private EmailAddress organizer;

    private Attendee[] attendees;

    @Key
    private boolean attendeesOmitted;

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
