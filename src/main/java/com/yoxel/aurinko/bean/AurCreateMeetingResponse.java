package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurCreateMeetingResponse extends GenericJson {

    @Key
    Boolean created;

    @Key
    String id;

    @Key
    Long reservationId;

    @Key
    String groupXid;

    @Key
    String rescheduleToken;
}
