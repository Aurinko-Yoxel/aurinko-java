package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class MeetingInfo extends GenericJson {

    @Key
    private boolean cancelled;

    @Key
    private List<Attendee> attendees;

    @Key
    private String response;
}
