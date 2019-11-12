package com.yoxel.aurinko.dto;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurEmail extends AurIdEntity {
    @Key
    String subject;
}
