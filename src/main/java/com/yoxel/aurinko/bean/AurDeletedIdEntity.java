package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurDeletedIdEntity extends AurIdEntity {

    @Key
    private DateTime timestamp;
}
