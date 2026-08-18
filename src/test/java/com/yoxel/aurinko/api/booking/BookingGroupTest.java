package com.yoxel.aurinko.api.booking;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.util.DateTime;
import com.yoxel.aurinko.api.FakeHttpImpl;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BookingGroupTest implements FakeHttpImpl {

    @Test
    void groupProfilesDynamicAvailability() throws IOException {
        String data = """
                {
                  "profileIds": [1, 2]
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        AurBookingAvailableProfilesInDto in = new AurBookingAvailableProfilesInDto();
        in.setTimeMin(DateTime.parseRfc3339("1970-01-01T00:00:00Z"));
        in.setTimeMax(DateTime.parseRfc3339("1970-01-02T00:00:00Z"));
        in.setProfileNames(List.of("p1"));

        AurBookingAvailableProfilesOutDto out = new Bookings(buildFakeHttp(mockTransport))
                .group
                .profiles
                .dynamicAvailability(com.yoxel.aurinko.apis.QueryParams.EMPTY, in);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/group/profiles/dynamic/availability");

        assertThat(out).isNotNull();
        assertThat(out.getProfileIds()).isEqualTo(List.of(1L, 2L));
    }

    @Test
    void groupProfilesMeetingLoadPage() throws IOException {
        String data = """
                {
                  "items": [{
                    "start": "1970-01-01T00:00:00Z",
                    "end": "1970-01-01T01:00:00Z",
                    "groupXids": ["g1"],
                    "accountIds": [10]
                  }],
                  "startTime": "1970-01-01T00:00:00Z",
                  "endTime": "1970-01-02T00:00:00Z",
                  "timeAvailableFor": "time",
                  "durationMinutes": 60,
                  "availabilityStep": 15,
                  "subject": "s",
                  "primaryColor": 123,
                  "secondaryColor": 456,
                  "nextPageToken": "nxt",
                  "limit": 10,
                  "offset": 0,
                  "done": true,
                  "totalSize": 1,
                  "nextFromDate": "1970-01-02"
                }
                """;

        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        AurBookingTimesOutDto out = new Bookings(buildFakeHttp(mockTransport))
                .group
                .profiles
                .meeting(1L)
                .loadPage(QueryParams.EMPTY, "pt");

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/group/profiles/1/meeting?pageToken=pt");

        assertThat(out).isNotNull();

        com.yoxel.aurinko.bean.sub.AurBookingMeetingInterval interval = new com.yoxel.aurinko.bean.sub.AurBookingMeetingInterval();
        interval.setStart(DateTime.parseRfc3339("1970-01-01T00:00:00Z"));
        interval.setEnd(DateTime.parseRfc3339("1970-01-01T01:00:00Z"));
        interval.setGroupXids(List.of("g1"));
        interval.setAccountIds(List.of(10L));

        assertThat(out.getItems()).isEqualTo(List.of(interval));
        assertThat(out.getStartTime()).isEqualTo(DateTime.parseRfc3339("1970-01-01T00:00:00Z"));
        assertThat(out.getEndTime()).isEqualTo(DateTime.parseRfc3339("1970-01-02T00:00:00Z"));
        assertThat(out.getNextPageToken()).isEqualTo("nxt");
        assertThat(out.getDone()).isEqualTo(true);
        assertThat(out.getTotalSize()).isEqualTo(1L);
    }

    @Test
    void groupCreateMeeting() throws IOException {
        Long profileId = 1L;
        String data = """
                {
                  "created": true,
                  "id": "m1",
                  "reservationId": 123,
                  "groupXid": "g1",
                  "rescheduleToken": "rt"
                }
                """;

        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        AurCreateMeetingDto dto = new AurCreateMeetingDto();
        dto.setName("Scheduler");
        dto.setEmail("scheduler@example.com");
        dto.setAccountIds(List.of(10L));

        AurCreateMeetingResponse resp = new Bookings(buildFakeHttp(mockTransport))
                .group
                .profiles
                .meeting(profileId)
                .create(dto);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/group/profiles/" + profileId + "/meeting");

        assertThat(resp).isNotNull();
        assertThat(resp.getCreated()).isTrue();
        assertThat(resp.getId()).isEqualTo("m1");
        assertThat(resp.getReservationId()).isEqualTo(123L);
        assertThat(resp.getGroupXid()).isEqualTo("g1");
        assertThat(resp.getRescheduleToken()).isEqualTo("rt");
    }

    @Test
    void groupProfilesLoadPage() throws IOException {
        String data = """
                {
                  "offset": 0,
                  "totalSize": 1,
                  "done": true,
                  "records": [
                    {
                      "id": 1,
                      "name": "n1"
                    }
                  ]
                }
                """;

        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        AurBookingOutDto.Page page = new Bookings(buildFakeHttp(mockTransport))
                .group
                .profiles
                .loadPage(10, 0, com.yoxel.aurinko.apis.QueryParams.EMPTY);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/group/profiles?limit=10&offset=0");

        assertThat(page).isNotNull();
        assertThat(page.getOffset()).isEqualTo(0);
        assertThat(page.getTotalSize()).isEqualTo(1);
        assertThat(page.isDone()).isTrue();

        AurBookingOutDto rec = page.getRecords()[0];
        assertThat(rec).isNotNull();
        assertThat(rec.getId()).isEqualTo(1);
        assertThat(rec.getName()).isEqualTo("n1");
    }

    @Test
    void groupCreateProfile() throws IOException {
        String data = """
                {
                  "id": 1,
                  "name": "name"
                }
                """;

        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        AurBookingInDto dto = new AurBookingInDto();
        dto.setName("name");

        AurBookingOutDto r = new Bookings(buildFakeHttp(mockTransport))
                .group
                .profiles
                .create(dto);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/group/profiles");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo(1);
        assertThat(r.getName()).isEqualTo("name");
    }

    @Test
    void groupReadProfile() throws IOException {
        Long id = 3L;
        String data = """
                {
                  "id": 3,
                  "name": "group-name"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        AurBookingOutDto r = new Bookings(buildFakeHttp(mockTransport))
                .group
                .profiles
                .read(id);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/group/profiles/" + id);

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo(3);
        assertThat(r.getName()).isEqualTo("group-name");
    }

    @Test
    void groupUpdateProfile() throws IOException {
        Long id = 7L;
        String data = """
                {
                  "status": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        AurBookingUpdateDto dto = new AurBookingUpdateDto();
        dto.setName("updated name");

        AurStatus status = new Bookings(buildFakeHttp(mockTransport))
                .group
                .profiles
                .update(id, dto, com.yoxel.aurinko.apis.QueryParams.EMPTY);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/group/profiles/" + id);

        assertThat(status).isNotNull();
        assertThat(status.getStatus()).isEqualTo("ok");
    }

    @Test
    void groupDeleteProfile() throws IOException {
        Long id = 9L;
        MockLowLevelHttpResponse mockResponse = successJsonResponse("{}");
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        new Bookings(buildFakeHttp(mockTransport))
                .group
                .profiles
                .delete(id);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/group/profiles/" + id);
    }
}
