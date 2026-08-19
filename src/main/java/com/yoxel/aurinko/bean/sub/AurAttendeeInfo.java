package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurAttendeeInfo extends GenericJson {

    @Key
    String name;

    @Key
    String email;

    @Key
    String id;
}
