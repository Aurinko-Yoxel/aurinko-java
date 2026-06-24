package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurEmailFolderInfo extends AurIdEntity {

    @Key
    String name;

    @Key
    String fullName;

    @Key
    Boolean isFolder;

    @Key
    Boolean isLabel;

    @Key
    String parentId;

    @Key
    String sysLabel;
}
