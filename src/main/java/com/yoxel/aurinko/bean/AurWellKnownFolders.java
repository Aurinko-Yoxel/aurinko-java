package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurWellKnownFolders extends GenericJson {

    @Key
    String archive;

    @Key
    String sent;
}
