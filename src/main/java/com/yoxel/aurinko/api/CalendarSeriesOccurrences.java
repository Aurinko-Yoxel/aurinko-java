package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.ListSupport_TokenBased;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.bean.AurEvent;
import com.yoxel.aurinko.bean.AurEventsPage;
import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

/**
 * Occurrences API: /calendars/:calendarId/events/:masterId/occurrences
 */
public class CalendarSeriesOccurrences extends HttpApiSupport
        implements ListSupport_TokenBased<AurEvent, String, AurEventsPage>,
        ReadSupport<AurEvent, String> {

    private final String calendarId;
    private final String masterId;

    CalendarSeriesOccurrences(String calendarId, String masterId, HttpImpl httpImpl) {
        super(httpImpl);
        this.calendarId = calendarId;
        this.masterId = masterId;
    }

    @Override
    public Class<AurEvent> entityClass() {
        return AurEvent.class;
    }

    @Override
    public Class<AurEventsPage> entityPageClass() {
        return AurEventsPage.class;
    }

    @Override
    public String entityPath() {
        return "/calendars/" + calendarId + "/events/" + masterId + "/occurrences";
    }
}
