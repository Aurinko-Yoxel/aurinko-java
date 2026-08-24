package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurFollowupRuleAction extends GenericJson {

    @Key
    Integer intervalDays;

    @Key
    String message;
}
