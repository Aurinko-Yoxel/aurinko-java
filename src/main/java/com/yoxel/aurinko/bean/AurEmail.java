package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.AurIdEntity;
import lombok.Data;

@Data
public class AurEmail extends AurIdEntity {
    @Key
    String subject;
}
