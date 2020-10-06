package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class PostalAddress extends GenericJson {

    @Key
    private String street;

    @Key
    private String streetLine2;

    @Key
    private String poBox;

    @Key
    private String city;

    @Key
    private String state;

    @Key
    private String postalCode;

    @Key
    private String country;

    @Key
    private String type;
}
