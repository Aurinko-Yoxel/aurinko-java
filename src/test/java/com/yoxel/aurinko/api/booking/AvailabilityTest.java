package com.yoxel.aurinko.api.booking;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.api.FakeHttpImpl;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurBookingTimesOutDto;
import com.yoxel.aurinko.bean.AurCreateMeetingDto;
import com.yoxel.aurinko.bean.AurCreateMeetingResponse;
import com.yoxel.aurinko.bean.sub.AurDayWorkSchedule;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AvailabilityTest implements FakeHttpImpl {
    @Test
    void availableMeetingTimes() throws IOException {
        String cId = "1L";
        String name = "1L";
        String data = """
                {
                  "subject": "s"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurBookingTimesOutDto r = new Bookings(buildFakeHttp(mockTransport))
                .availability(cId, name)
                .getMeetingTimes(QueryParams.EMPTY, "pg");


        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/" + cId + "/" + name + "/meeting?pageToken=pg");

        assertThat(r).isNotNull();
        var schedule = new AurDayWorkSchedule();
        assertThat(r.getSubject()).isEqualTo("s");
    }

    @Test
    void createMeeting() throws IOException {
        String cId = "1L";
        String name = "1L";
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
                .availability(cId, name)
                .createMeeting(QueryParams.EMPTY, dto);

        // verify endpoint and method
        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/book/" + cId + "/" + name + "/meeting");

        // verify parsed response
        assertThat(resp).isNotNull();
        assertThat(resp.getCreated()).isTrue();
        assertThat(resp.getId()).isEqualTo("m1");
        assertThat(resp.getReservationId()).isEqualTo(123L);
        assertThat(resp.getGroupXid()).isEqualTo("g1");
        assertThat(resp.getRescheduleToken()).isEqualTo("rt");
    }
}
