package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurFollowupRuleAction;
import lombok.Data;

import java.util.List;

@Data
public class AurFollowupRuleAggregate extends GenericJson {

    @Key
    private Long id;

    @Key
    private String name;

    @Key
    private Boolean expectThreadResponse;

    @Key
    private String templateBody;

    @Key
    private String templateSubject;

    @Key
    private List<AurFollowupRuleAction> actions;

    public static class Page extends AurOffsetPage<AurFollowupRuleAggregate> {
    }
}
