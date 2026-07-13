package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurFollowupRuleAction;
import lombok.Data;

import java.util.List;

@Data
public class AurFollowupRuleAggregate extends AurIdEntity {

    @Key
    String name;

    @Key
    Boolean expectThreadResponse;

    @Key
    String templateBody;

    @Key
    String templateSubject;

    @Key
    List<AurFollowupRuleAction> actions;

    public static class Page extends AurOffsetPage<AurFollowupRuleAggregate> {
    }
}
