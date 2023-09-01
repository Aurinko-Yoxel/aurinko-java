package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class AurContact extends AurLiveIdEntity implements AurNativePropertiesSupport {
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
    private boolean hasPhoto;

    @Key
    private ContactPhoto photo;

    @Key
    private List<ContactRelation> relations;

    @Key
    private List<ContactEmailAddress> emailAddresses;

    @Key
    private List<PhoneNumber> phoneNumbers;

    @Key
    private List<PostalAddress> addresses;

    @Key
    private List<Url> urls;

    @Key
    private String companyId;

    @Key
    private Map<String, Object> nativeProperties;
}
