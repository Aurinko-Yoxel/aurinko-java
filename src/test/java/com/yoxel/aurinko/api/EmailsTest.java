package com.yoxel.aurinko.api;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.*;
import com.yoxel.aurinko.bean.sub.EmailAddress;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EmailsTest implements FakeHttpImpl {

    @Test
    void sendEmail() throws IOException {
        AurEmail dto = new AurEmail();
        dto.setSubject("s");
        EmailAddress eAddr = new EmailAddress();
        eAddr.setAddress("to@to.com");
        dto.setTo(List.of(eAddr));
        String data = """
                {
                  "id": "i",
                  "status": "ok",
                  "submittedMessageId": "i"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEmailSendResponse r = new Emails(buildFakeHttp(mockTransport))
                .create(dto, QueryParams.of("bodyType", "text"));

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/messages?bodyType=text");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("i");
        assertThat(r.getStatus()).isEqualTo("ok");
        assertThat(r.getSubmittedMessageId()).isEqualTo("i");
    }

    @Test
    void readEmail() throws IOException {
        String id = "id";
        String data = """
                {
                  "id": "i",
                  "subject": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEmail r = new Emails(buildFakeHttp(mockTransport))
                .read(id, QueryParams.of("bodyType", "text"));

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/messages/" + id + "?bodyType=text");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("i");
        assertThat(r.getSubject()).isEqualTo("ok");
    }

    @Test
    void deleteEmail() throws IOException {
        String id = "id";
        String data = """
                {
                  "status": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        new Emails(buildFakeHttp(mockTransport))
                .delete(id);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/messages/" + id);
    }

    @Test
    void pageEmail() throws IOException {
        String data = """
                {
                  "length": 1,
                  "records": [
                    {
                        "id": "id"
                    }
                  ]
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEmailsPage r = new Emails(buildFakeHttp(mockTransport))
                .loadPage(QueryParams.of("bodyType", "text"));

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/messages?bodyType=text");

        assertThat(r).isNotNull();
        assertThat(r.getRecords().length).isEqualTo(1);
        assertThat(r.getRecords()[0].getId()).isEqualTo("id");
    }

    @Test
    void readAttachment() throws IOException {
        String id = "id";
        String aId = "aId";
        String data = """
                {
                  "content": "abc"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurContent r = new Emails(buildFakeHttp(mockTransport))
                .getAttachment(id, aId);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/messages/" + id + "/attachments/" + aId);

        assertThat(r).isNotNull();
        assertThat(r.getContent()).isEqualTo("abc");
    }

    @Test
    void readRawMessage() throws IOException {
        String id = "id";
        String data = """
                abc""";
        MockLowLevelHttpResponse mockResponse = successBodyResponse("text/plain", data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        String r = new Emails(buildFakeHttp(mockTransport))
                .rawMessage(id);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/messages/" + id + "/raw");

        assertThat(r).isNotNull();
        assertThat(r).isEqualTo("abc");
    }

    @Test
    void updateStatus() throws IOException {
        AurEmailStatus dto = new AurEmailStatus();
        dto.setUnread(true);
        String id = "id";
        String data = """
                {
                  "status": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurStatus r = new Emails(buildFakeHttp(mockTransport))
                .updateStatus(id, dto);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/messages/" + id + "/status");

        assertThat(r).isNotNull();
        assertThat(r.getStatus()).isEqualTo("ok");
    }
    @Test
    void reply() throws IOException {
        AurEmail dto = new AurEmail();
        dto.setSubject("s");
        String id = "id";
        String data = """
                {
                  "id": "id",
                  "status": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEmailSendResponse r = new Emails(buildFakeHttp(mockTransport))
                .reply(id, dto);


        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/email/messages/" + id + "/reply");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo("id");
        assertThat(r.getStatus()).isEqualTo("ok");
    }


}
