package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public abstract class AurQueryResult<T extends AurIdEntity> extends GenericJson {

    @Key
    private long length;

    @Key
    private T[] records;

    @Key
    private String nextPageToken;

    @Key
    private String nextDeltaToken;
}
