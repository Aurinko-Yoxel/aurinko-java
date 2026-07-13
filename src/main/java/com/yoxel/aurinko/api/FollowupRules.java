package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.CreateSupport;
import com.yoxel.aurinko.apis.DeleteSupport;
import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.bean.AurFollowupRuleAggregate;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class FollowupRules extends HttpApiSupport
    implements ListSupport_OffsetBased<AurFollowupRuleAggregate, Long, AurFollowupRuleAggregate.Page>,
        CreateSupport<AurFollowupRuleAggregate, Long, AurFollowupRuleAggregate>,
        DeleteSupport<Long> {

    public FollowupRules(HttpImpl httpImpl) {
        super(httpImpl);
    }

    @Override
    public Class<AurFollowupRuleAggregate.Page> entityPageClass() {
        return AurFollowupRuleAggregate.Page.class;
    }

    @Override
    public Class<AurFollowupRuleAggregate> entitySaveResultClass() {
        return AurFollowupRuleAggregate.class;
    }

    @Override
    public String entityPath() {
        return "/followup/rules";
    }

    public AurFollowupRuleAggregate update(Long id, AurFollowupRuleAggregate rule) throws IOException {
        return httpPut(
                entityPath() + "/" + normalizeId(id),
                new JsonHttpContent(Utils.getDefaultJsonFactory(), rule)
        ).parseAs(AurFollowupRuleAggregate.class);
    }
}
