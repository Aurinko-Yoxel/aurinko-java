package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.bean.sub.AurFollowupRuleAction;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AurConvoDripStatus extends GenericJson {
    @Key
    String convoId;

    @Key
    String subject;

    @Key
    String status;

    @Key
    AurFollowupRuleAggregate rule;

    @Key
    List<AurFollowupRuleAction> actionOverrides;

    @Key
    DateTime dripStartedAt;

    @Key
    Integer dripLastNum;

    @Key
    String dripError;

    @Key
    DateTime dripEndedAt;

    @Key
    Map<String, String> actionVars;

    @Key
    List<String> targets;

    @Key
    String dripResponder;
}
