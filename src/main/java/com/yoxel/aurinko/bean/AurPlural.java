package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurPlural<T extends GenericJson> extends GenericJson {

    @Key
    private long offset;

    @Key
    private long totalSize;

    @Key
    private boolean done;

    @Key
    private T[] records;
}
