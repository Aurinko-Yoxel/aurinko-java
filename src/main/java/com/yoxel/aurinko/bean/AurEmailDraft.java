package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurScheduledDraft;
import lombok.Data;

@Data
public class AurEmailDraft extends AurIdEntity {
    @Key
    private AurEmail message;

    @Key
    private AurScheduledDraft scheduled;
}
