package com.yoxel.aurinko.api.user;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.api.FakeHttpImpl;
import com.yoxel.aurinko.bean.AurEndUserDto;
import com.yoxel.aurinko.bean.AurEndUserSettingsDto;
import com.yoxel.aurinko.bean.AurStatus;
import com.yoxel.aurinko.bean.sub.AurWeekWorkSchedule;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTest implements FakeHttpImpl {

    @Test
    void getInfo() throws IOException {
        String data = """
                {
                  "id": "i",
                  "appId": 123,
                  "trustedIdentity": true
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEndUserDto r = new User(buildFakeHttp(mockTransport))
                .getInfo();

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/user");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("i");
        assertThat(r.getAppId()).isEqualTo(123L);
        assertThat(r.getTrustedIdentity()).isEqualTo(true);
    }

    @Test
    void changeSettings() throws IOException {
        AurEndUserSettingsDto dto = new AurEndUserSettingsDto();
        AurWeekWorkSchedule sh = new AurWeekWorkSchedule();
        sh.setTimezone("America/New_York");
        dto.setWorkHours(sh);
        String data = """
                {
                  "id": "i",
                  "appId": 123,
                  "trustedIdentity": true
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEndUserDto r = new User(buildFakeHttp(mockTransport))
                .changeSettings(dto);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/user/settings");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("i");
        assertThat(r.getAppId()).isEqualTo(123L);
        assertThat(r.getTrustedIdentity()).isEqualTo(true);
    }

    @Test
    void logout() throws IOException {
        String data = """
                {
                  "status": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurStatus r = new User(buildFakeHttp(mockTransport))
                .logout();

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/user/logout");

        assertThat(r).isNotNull();
        assertThat(r.getStatus()).isEqualTo("ok");
    }
}
