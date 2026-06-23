package com.yoxel.aurinko.api;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;
import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.SyncSupport;
import com.yoxel.aurinko.bean.AurEvent;
import com.yoxel.aurinko.bean.AurEventSaveResult;
import com.yoxel.aurinko.bean.AurEventsPage;
import com.yoxel.aurinko.bean.AurSeriesInfo;
import com.yoxel.aurinko.bean.sub.MeetingResponse;
import com.yoxel.aurinko.http.HttpImpl;
import com.yoxel.commons.xstream.XStream;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.yoxel.aurinko.apis.QueryParams.qp;

/**
 * Events API: /calendars/:id/events
 */
public class CalendarEvents
        extends EntitySupport_TokenBased<AurEvent, String, AurEventsPage, AurEventSaveResult>
        implements SyncSupport<AurEvent, AurEventsPage> {

    private final String calendarId;
    private final HttpImpl httpImpl;

    CalendarEvents(String calendarId, HttpImpl httpImpl) {
        this(calendarId, "", httpImpl);
    }

    private CalendarEvents(String calendarId, String postfix, HttpImpl httpImpl) {
        super(calendarPath(calendarId) + "/events" + postfix,
                AurEvent.class, AurEventsPage.class, AurEventSaveResult.class, httpImpl);
        this.calendarId = calendarId;
        this.httpImpl = httpImpl;
    }

    private static String calendarPath(String calendarId) {
       return "/calendars/" + URLEncoder.encode(calendarId, StandardCharsets.UTF_8);
    }

    @Override
    public String syncRootPath() {
        return calendarPath(calendarId);
    }

    public XStream<AurEvent, IOException> streamRange(DateTime timeMin, DateTime timeMax)
            throws IOException {

        return new CalendarEvents(calendarId, "/range", httpImpl).streamPaged(
                QueryParams.of(
                        qp("timeMin", timeMin.toDateTimeISO()),
                        qp("timeMax", timeMax.toDateTimeISO())
                )
        );
    }

    public XStream<AurEvent, IOException> streamFindEvents(List<String> iCalUIds)
            throws IOException {
        // trick to be able to call /find endpoint
        return new CalendarEvents(calendarId, "/find", httpImpl).streamPaged(
                QueryParams.of(
                        iCalUIds.stream()
                                .map(iCalUId -> qp("iCalUId", iCalUId))
                                .collect(Collectors.toList())
                )
        );
    }

    /**
     * Retrieves a series description by masterId
     */
    public AurSeriesInfo series(String masterId) throws IOException {
        return httpGet(calendarPath(calendarId) + "/events/" + masterId + "/series")
                .parseAs(AurSeriesInfo.class);
    }

    public CalendarSeriesOccurrences occurrences(String masterId) {
        return new CalendarSeriesOccurrences(calendarId, masterId, httpImpl);
    }

    public void updateMeetingResponse(String eventId, MeetingResponse response) throws IOException {
        updateMeetingResponse(eventId, response, true);
    }

    public void updateMeetingResponse(String eventId, MeetingResponse response, boolean notifyAttendees)
            throws IOException {
        httpPut(
                entityPath() + "/" + normalizeId(eventId) + "/response",
                QueryParams.of("notifyAttendees", notifyAttendees),
                new JsonHttpContent(Utils.getDefaultJsonFactory(), response)
        ).ignore();
    }
}
