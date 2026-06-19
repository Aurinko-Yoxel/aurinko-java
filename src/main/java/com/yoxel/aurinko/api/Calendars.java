package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.bean.AurCalendar;
import com.yoxel.aurinko.bean.AurCalendarsPage;
import com.yoxel.aurinko.http.HttpImpl;

/**
 * Calendars API: /calendars
 */
public class Calendars extends EntitySupport_TokenBased<AurCalendar, String, AurCalendarsPage, AurCalendar> {

    private final HttpImpl httpImpl;

    public Calendars(HttpImpl httpImpl) {
        super("/calendars", AurCalendar.class, AurCalendarsPage.class, AurCalendar.class, httpImpl);
        this.httpImpl = httpImpl;
    }

    @Override
    public String normalizeId(String id) {
        return id == null ? "primary" : id;
    }

    public CalendarEvents calendarEvents(String calendarId) {
        return new CalendarEvents(normalizeId(calendarId), httpImpl);
    }
}
