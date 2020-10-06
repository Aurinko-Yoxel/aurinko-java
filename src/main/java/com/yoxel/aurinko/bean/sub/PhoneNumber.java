package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class PhoneNumber extends GenericJson {

    @Key
    private String number;

    @Key
    private String type;

    @Key
    private String canonicalForm;
}
