package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class ContactEmailAddress extends GenericJson {

    @Key
    private String address;

    @Key
    private String name;

    @Key
    private String type;
}
