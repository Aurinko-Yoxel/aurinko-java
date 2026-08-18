package com.yoxel.aurinko.api.booking;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
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
import java.time.Instant;
import java.time.Period;
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
                  "name": "name"
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
    }
}
