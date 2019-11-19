package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public abstract class AurIdEntity extends GenericJson {

    @Key
    String id;

    @Key
    DateTime lastModifiedTime;
}
