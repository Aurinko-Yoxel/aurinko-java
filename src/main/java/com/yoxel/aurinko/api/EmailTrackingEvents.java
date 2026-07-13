package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.AurTrackingEvent;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

public class EmailTrackingEvents extends HttpApiSupport
        implements ListSupport_OffsetBased<AurTrackingEvent, Long, AurTrackingEvent.Page>,
        ReadSupport<AurTrackingEvent, Long> {

    private final String trackingId;

    EmailTrackingEvents(String trackingId, HttpImpl httpImpl) {
        super(httpImpl);
        this.trackingId = trackingId;
    }

    @Override
    public Class<AurTrackingEvent.Page> entityPageClass() {
        return AurTrackingEvent.Page.class;
    }

    @Override
    public String entityPath() {
        return "/email/tracking/" + trackingId + "/events";
    }

    @Override
    public Class<AurTrackingEvent> entityClass() {
        return AurTrackingEvent.class;
    }
}
