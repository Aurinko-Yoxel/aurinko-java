package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurSyncStatus {

    @Key
    private boolean ready;

    @Key
    private String syncUpdatedToken;

    @Key
    private String syncDeletedToken;
}
