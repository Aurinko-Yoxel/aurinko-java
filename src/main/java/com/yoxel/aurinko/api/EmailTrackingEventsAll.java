package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.bean.AurTrackingEvent;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

public class EmailTrackingEventsAll extends HttpApiSupport
        implements ListSupport_OffsetBased<AurTrackingEvent, Long, AurTrackingEvent.Page> {

    EmailTrackingEventsAll(HttpImpl httpImpl) {
        super(httpImpl);
    }

    @Override
    public Class<AurTrackingEvent.Page> entityPageClass() {
        return AurTrackingEvent.Page.class;
    }

    @Override
    public String entityPath() {
        return "/email/tracking/events";
    }
}
