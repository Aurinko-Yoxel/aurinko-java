package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.AurStatus;
import com.yoxel.aurinko.bean.AurTracking;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;

public class EmailTracking extends HttpApiSupport
        implements ListSupport_OffsetBased<AurTracking, Long, AurTracking.Page>,
        ReadSupport<AurTracking, Long> {

    private final HttpImpl httpImpl;

    EmailTracking(HttpImpl httpImpl) {
        super(httpImpl);
        this.httpImpl = httpImpl;
    }

    @Override
    public Class<AurTracking> entityClass() {
        return AurTracking.class;
    }

    @Override
    public Class<AurTracking.Page> entityPageClass() {
        return AurTracking.Page.class;
    }

    @Override
    public String entityPath() {
        return "/email/tracking";
    }

    public EmailDraftTracking draftTracking() {
        return new EmailDraftTracking(httpImpl);
    }

    public EmailTrackingEventsAll events() {
        return new EmailTrackingEventsAll(httpImpl);
    }

    public EmailTrackingEvents events(String trackingId) {
        return new EmailTrackingEvents(trackingId, httpImpl);
    }

    public AurStatus purgeTracking(QueryParams params) throws IOException {
        return httpPost(
                entityPath() + "/purgeMyTracking",
                params
        ).parseAs(AurStatus.class);
    }

    public AurStatus ignoreOpenClicks(QueryParams params) throws IOException {
        return httpPost(
                entityPath() + "/ignoreOpenClicks",
                params
        ).parseAs(AurStatus.class);
    }
}
