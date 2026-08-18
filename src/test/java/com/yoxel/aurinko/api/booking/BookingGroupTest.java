package com.yoxel.aurinko.api.booking;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.util.DateTime;
import com.yoxel.aurinko.api.FakeHttpImpl;
import com.yoxel.aurinko.bean.AurBookingAvailableProfilesInDto;
import com.yoxel.aurinko.bean.AurBookingAvailableProfilesOutDto;
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
}
