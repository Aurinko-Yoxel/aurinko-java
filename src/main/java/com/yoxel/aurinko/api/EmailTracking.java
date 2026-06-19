package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.AurTracking;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

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

    public EmailTrackingEvents events() {
        return new EmailTrackingEvents(httpImpl);
    }
}
