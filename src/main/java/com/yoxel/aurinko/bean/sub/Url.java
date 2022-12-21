package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Url extends GenericJson {

    @Key
    private String id;

    @Key
    private Boolean deleted;

    @Key
    private String href;

    @Key
    private String type;
}
