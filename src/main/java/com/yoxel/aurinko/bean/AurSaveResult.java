package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public abstract class AurSaveResult <T extends AurLiveIdEntity> extends GenericJson {
    @Key
    private String id;

    @Key
    private String etag;

    @Key
    private T record;
}
