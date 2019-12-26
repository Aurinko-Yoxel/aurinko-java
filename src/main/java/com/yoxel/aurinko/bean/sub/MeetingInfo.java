package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class MeetingInfo extends GenericJson {

    @Key
    private boolean cancelled;

    private Attendee[] attendees;

    @Key
    private boolean attendeesOmitted;

    @Key
    private String response;
}
