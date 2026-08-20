package com.yoxel.aurinko.api;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurEmail;
import com.yoxel.aurinko.bean.AurEmailDraft;
import com.yoxel.aurinko.bean.AurEmailDraftResponse;
import com.yoxel.aurinko.bean.EmailDraftSendResponse;
import com.yoxel.aurinko.bean.sub.EmailAddress;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EmailDraftsTest implements FakeHttpImpl {
    @Test
    void createEmailDraft() throws IOException {
        AurEmail dto = new AurEmail();
        dto.setSubject("s");
        EmailAddress eAddr = new EmailAddress();
        eAddr.setAddress("to@to.com");
        dto.setTo(List.of(eAddr));
        String data = """
                {
                  "id": "i",
                  "webLink": "s"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEmailDraftResponse r = new EmailDrafts(buildFakeHttp(mockTransport))
                .create(dto, QueryParams.of("bodyType", "text"));


        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/drafts?bodyType=text");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("i");
        assertThat(r.getWebLink()).isEqualTo("s");
    }

    @Test
    void updateEmailDraft() throws IOException {
        String id = "id";
        AurEmail dto = new AurEmail();
        dto.setSubject("s");
        EmailAddress eAddr = new EmailAddress();
        eAddr.setAddress("to@to.com");
        dto.setTo(List.of(eAddr));
        String data = """
                {
                  "id": "i",
                  "webLink": "s"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEmailDraftResponse r = new EmailDrafts(buildFakeHttp(mockTransport))
                .update(id, dto, QueryParams.of("bodyType", "text"));


        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/drafts/" + id + "?bodyType=text");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("i");
        assertThat(r.getWebLink()).isEqualTo("s");
    }

    @Test
    void readEmailDraft() throws IOException {
        String id = "id";
        String data = """
                {
                  "id": "i",
                  "message": {
                    "id": "i"
                  }
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEmailDraft r = new EmailDrafts(buildFakeHttp(mockTransport))
                .read(id, QueryParams.of("bodyType", "text"));


        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/drafts/" + id + "?bodyType=text");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("i");
        assertThat(r.getMessage().getId()).isEqualTo("i");
    }

    @Test
    void deleteEmailDraft() throws IOException {
        String id = "id";
        String data = """
                {
                  "status": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        new EmailDrafts(buildFakeHttp(mockTransport))
                .delete(id);


        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/drafts/" + id);
    }

    @Test
    void sendEmailDraft() throws IOException {
        String id = "id";
        String data = """
                {
                  "status": "ok",
                  "id": "i"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        EmailDraftSendResponse r = new EmailDrafts(buildFakeHttp(mockTransport))
                .send(id, QueryParams.of("returnIds", "true"));


        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/drafts/" + id + "/send?returnIds=true");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("i");
    }
}
