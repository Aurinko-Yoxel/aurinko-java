package com.yoxel.aurinko.dto;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public abstract class AurRecords<T extends AurIdEntity> extends GenericJson {

    @Key
    long length;

    @Key
    T[] records;

    @Key
    String nextPageToken;

    @Key
    String deltaToken;
}
