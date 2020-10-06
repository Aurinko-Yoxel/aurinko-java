package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class ContactCompany extends GenericJson {

    @Key
    private String name;

    @Key
    private String officeLocation;

    @Key
    private String department;

    @Key
    private String jobTitle;

    @Key
    private String yomiName;
}
