package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.*;
import lombok.Data;

import java.util.List;

@Data
public class AurContact extends AurLiveIdEntity {
    @Key
    private ContactName name;

    @Key
    private String nickName;

    @Key
    private ContactCompany company;

    @Key
    private String fileAs;

    @Key
    private String birthday;

    @Key
    private List<String> keywords;

    @Key
    private String notes;

    @Key
    private List<ContactRelation> relations;

    @Key
    private List<ContactEmailAddress> emailAddresses;

    @Key
    private List<PhoneNumber> phoneNumbers;

    @Key
    List<PostalAddress> addresses;

    @Key
    List<Url> urls;

}
