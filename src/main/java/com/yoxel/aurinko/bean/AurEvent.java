package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.*;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class AurEvent extends AurLiveIdEntity {

    @Key // ("folderId")
    private String calendarId;

    @Key
    private String subject;

    @Key
    private String description;

    @Key
    private String location;

    @Key
    private EventDateTime start;

    @Key
    private EventDateTime end;

    @Key
    private Organizer createdBy;

    @Key
    private Organizer organizer;

    @Key
    private MeetingInfo meetingInfo;

    @Key
    private String recurrenceType;

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
    private List<String> categories;

    @Key
    private Map<String, String> nativeProperties;
}
