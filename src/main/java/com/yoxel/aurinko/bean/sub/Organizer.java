package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Organizer extends GenericJson implements EventParticipant {
    @Key
    private String id;

    @Key
    private EmailAddress emailAddress;
}
