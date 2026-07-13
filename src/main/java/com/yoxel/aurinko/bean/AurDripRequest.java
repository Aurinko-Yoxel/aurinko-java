package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurFollowupRuleAction;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AurDripRequest extends GenericJson {

    @Key
    String status;

    @Key
    Long ruleId;

    @Key
    List<AurFollowupRuleAction> actionOverrides;

    @Key
    DateTime dripAfter;

    @Key
    List<String> targets;

    @Key
    Map<String, String> actionVars;
}
