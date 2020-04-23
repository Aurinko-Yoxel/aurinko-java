package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public final class InetHeader extends GenericJson {
    @Key
    private String name;

    @Key
    private String value;
}
