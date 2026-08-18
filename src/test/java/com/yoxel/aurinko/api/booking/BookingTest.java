package com.yoxel.aurinko.api.booking;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.util.DateTime;
import com.yoxel.aurinko.api.FakeHttpImpl;
import com.yoxel.aurinko.bean.AurBookingInDto;
import com.yoxel.aurinko.bean.AurBookingOutDto;
import com.yoxel.aurinko.bean.AurWeekWorkScheduleResponse;
import com.yoxel.aurinko.bean.sub.AurAvailabilityInterval;
import com.yoxel.aurinko.bean.sub.AurAvailabilityIntervals;
import com.yoxel.aurinko.bean.sub.AurDayWorkSchedule;
import com.yoxel.aurinko.bean.sub.AurWeekWorkSchedule;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BookingTest implements FakeHttpImpl {
    @Test
    void accountWorkHours() throws IOException {
        String data = """
                {
                  "daySchedules": [{"dayOfWeek": "SUNDAY"}],
                  "timezone": "America/New_York",
                  "source": "aurinko"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurWeekWorkScheduleResponse r = new Bookings(buildFakeHttp(mockTransport)).account.workHours();


        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/account/workHours");

        assertThat(r).isNotNull();
        var schedule = new AurDayWorkSchedule();
        schedule.setDayOfWeek("SUNDAY");
        assertThat(r.getDaySchedules()).isEqualTo(List.of(schedule));

        assertThat(r.getTimezone()).isEqualTo("America/New_York");
        assertThat(r.getSource()).isEqualTo("aurinko");
    }

    @Test
    void createBookingProfile() throws IOException {
        String data = """
                {
                  "name": "name",
                  "durationMinutes": 1,
                  "bufferBetweenMinutes": 1,
                  "availabilityStep": 1,
                  "startAfterMinutes": 1,
                  "startTime": "1970-01-01T03:00:01.235+03:00",
                  "endTime": "1970-01-02T03:00:01.235+03:00",
                  "timeAvailableFor": "time",
                  "subject": "s",
                  "description": "d",
                  "location": "l",
                  "workHours": {"timezone":"America/New_York"},
                  "availabilityIntervals": {
                    "intervals": [{ 
                      "dateStartInclusive": "1970-01-01",
                      "dateEndInclusive": "1970-01-02"
                    }]
                  },
                  "context": "c",
                  "startConference": true,
                  "openMeetingUrl": u,
                  "clientOrgId": "oi"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        AurBookingInDto dto = new AurBookingInDto();
        dto.setName("name");

        AurBookingOutDto r = new Bookings(buildFakeHttp(mockTransport)).account.profiles.create(dto);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/account/profiles");

        assertThat(r).isNotNull();
        assertThat(r.getName()).isEqualTo("name");
        assertThat(r.getDurationMinutes()).isEqualTo(1);
        assertThat(r.getBufferBetweenMinutes()).isEqualTo(1);
        assertThat(r.getAvailabilityStep()).isEqualTo(1);
        assertThat(r.getStartAfterMinutes()).isEqualTo(1);
        assertThat(r.getStartTime()).isEqualTo(DateTime.parseRfc3339("1970-01-01T03:00:01.235+03:00"));
        assertThat(r.getEndTime()).isEqualTo(DateTime.parseRfc3339("1970-01-02T03:00:01.235+03:00"));
        assertThat(r.getTimeAvailableFor()).isEqualTo("time");
        assertThat(r.getSubject()).isEqualTo("s");
        assertThat(r.getDescription()).isEqualTo("d");
        assertThat(r.getLocation()).isEqualTo("l");
        AurWeekWorkSchedule wsc = new AurWeekWorkSchedule();
        wsc.setTimezone("America/New_York");
        assertThat(r.getWorkHours()).isEqualTo(wsc);

        AurAvailabilityIntervals ais = new AurAvailabilityIntervals();
        AurAvailabilityInterval ai = new AurAvailabilityInterval();
        ai.setDateStartInclusive("1970-01-01");
        ai.setDateEndInclusive("1970-01-02");
        ais.setIntervals(List.of(ai));
        assertThat(r.getAvailabilityIntervals()).isEqualTo(ais);
        assertThat(r.getContext()).isEqualTo("c");
        assertThat(r.getStartConference()).isEqualTo(true);
        assertThat(r.getOpenMeetingUrl()).isEqualTo("u");
        assertThat(r.getClientOrgId()).isEqualTo("oi");
    }

    @Test
    void readBookingProfile() throws IOException {
        String id = "profile-id";
        String data = """
                {
                  "id": "profile-id",
                  "name": "name",
                  "email": "user@example.com",
                  "phone": "+1234567890",
                  "timezone": "Europe/Helsinki",
                  "source": "aurinko",
                  "active": true,
                  "notes": "Some notes about the profile",
                  "createdAt": "2026-01-01T00:00:00Z",
                  "updatedAt": "2026-06-01T12:34:56Z"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        AurBookingOutDto r = new Bookings(buildFakeHttp(mockTransport)).account.profiles.read(id);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/account/profiles/" + id);

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("profile-id");
        assertThat(r.getName()).isEqualTo("name");
        assertThat(r.get("email")).isEqualTo("user@example.com");
        assertThat(r.get("phone")).isEqualTo("+1234567890");
        assertThat(r.get("timezone")).isEqualTo("Europe/Helsinki");
        assertThat(r.get("source")).isEqualTo("aurinko");
        assertThat(r.get("active")).isEqualTo(Boolean.TRUE);
        assertThat(r.get("notes")).isEqualTo("Some notes about the profile");
        assertThat(String.valueOf(r.get("createdAt"))).contains("2026-01-01T00:00:00");
        assertThat(String.valueOf(r.get("updatedAt"))).contains("2026-06-01T12:34:56");
    }
}
