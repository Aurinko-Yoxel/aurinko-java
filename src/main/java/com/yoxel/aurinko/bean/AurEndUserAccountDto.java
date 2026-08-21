package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurOrganizationDto;
import lombok.Data;

import java.util.List;

@Data
public class AurEndUserAccountDto extends GenericJson {

    @Key
    private Long id;

    @Key
    private Long parentId;

    @Key
    private String serviceType;

    @Key
    private String userAccountType;

    @Key
    private String type;

    @Key
    private Boolean active;

    @Key
    private String loginString;

    @Key
    private String email;

    @Key
    private String name;

    @Key
    private String serverUrl;

    @Key
    private String serverUrl2;

    @Key
    private String clientOrgId;

    @Key
    private String authUserId;

    @Key
    private String authOrgId;

    @Key
    private String timezone;

    @Key
    private DateTime authObtainedAt;

    @Key
    private AurOrganizationDto organization;

    @Key
    private String tokenStatus;

    @Key
    private List<String> scopes;

    @Key
    private List<String> authScopes;

    @Key
    private List<String> nativeScopes;

    @Key
    private List<String> authNativeScopes;

    @Key
    private Boolean hasApiErrors;

    @Key
    private Integer bookingCount;

    @Key
    private Boolean trackingActive;

    @Key
    private Integer templatesCount;

    @Key
    private DateTime createdAt;

    @Key
    private DateTime updatedAt;

    public static class Page extends AurTokenPage<AurEndUserAccountDto> {

    }
}
