package com.yoxel.aurinko.api;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurStatus;
import com.yoxel.aurinko.bean.AurTracking;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EmailTrackingTest implements FakeHttpImpl {

    @Test
    void listTrackings() throws IOException {
        String data = """
                {
                  "records": [
                    {
                        "id": 1,
                        "messageId": "messageId"
                    }
                  ]
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurTracking.Page r = new EmailTracking(buildFakeHttp(mockTransport))
                .loadPage(10, 0, QueryParams.EMPTY);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/tracking?limit=10&offset=0");

        assertThat(r).isNotNull();
        assertThat(r.getRecords().length).isEqualTo(1);
        assertThat(r.getRecords()[0].getId()).isEqualTo(1L);
        assertThat(r.getRecords()[0].getMessageId()).isEqualTo("messageId");
    }

    @Test
    void readTrackings() throws IOException {
        Long id = 1L;
        String data = """
                {
                  "id": 1,
                  "messageId": "messageId"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurTracking r = new EmailTracking(buildFakeHttp(mockTransport))
                .read(id);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/tracking/" + id);

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo(1L);
        assertThat(r.getMessageId()).isEqualTo("messageId");
    }

    @Test
    void purgeTracking() throws IOException {
        Long id = 1L;
        String data = """
                {
                  "status": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurStatus r = new EmailTracking(buildFakeHttp(mockTransport))
                .purgeTracking(QueryParams.of("threadId", "tid"), "user_ag");

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/tracking/purgeMyTracking?threadId=tid");


        String actualUserAgent = mockTransport.getLowLevelHttpRequest().getHeaders()
                .entrySet().stream()
                .filter(entry -> "user-agent".equalsIgnoreCase(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .map(l -> l.get(0))
                .orElse(null);

        assertThat(actualUserAgent)
                .isEqualTo("user_ag");

        assertThat(r).isNotNull();
        assertThat(r.getStatus()).isEqualTo("ok");
    }

    @Test
    void ignoreOpenClicks() throws IOException {
        String data = """
                {
                  "status": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurStatus r = new EmailTracking(buildFakeHttp(mockTransport))
                .ignoreOpenClicks(
                        QueryParams.of("isIgnored", true)
                                .add("messageId", "id")
                );

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/tracking/ignoreOpenClicks?isIgnored=true&messageId=id");

        assertThat(r).isNotNull();
        assertThat(r.getStatus()).isEqualTo("ok");
    }

}
