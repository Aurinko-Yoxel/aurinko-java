package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurOffsetPage<T> extends GenericJson {

    @Key
    private int offset;

    @Key
    private int totalSize;

    @Key
    private boolean done;

    @Key
    private T[] records;
}
