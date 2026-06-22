package com.yoxel.aurinko.api;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.dto.AurAccountDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccountsTest implements FakeHttpImpl {

    @Test
    void getOAuthClientRegs() throws IOException {
        String oauthRecord = """
                {
                  "daemon": false,
                  "serviceType": "Google",
                  "clientId": "123",
                  "clientSecret": "secret",
                  "clientSecret2": "secret2",
                  "clientSecret3": "secret3",
                  "topicName": "topic",
                  "intermediateCallbackUrl": "url"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(offsetPageSingle(oauthRecord));

        Accounts acc = new Accounts(buildFakeHttp(mockResponse));

        var response = acc.getOAuthClientRegs();

        assertThat(response).isNotNull();
        assertThat(response.getTotalSize()).isEqualTo(1);
        assertThat(response.isDone()).isTrue();

        assertThat(response.getRecords())
                .singleElement()
                .satisfies(record -> {
                    assertThat(record.isDaemon()).isFalse();
                    assertThat(record.getServiceType()).isEqualTo("Google");
                    assertThat(record.getClientId()).isEqualTo("123");
                    assertThat(record.getClientSecret()).isEqualTo("secret");
                    assertThat(record.getClientSecret2()).isEqualTo("secret2");
                    assertThat(record.getClientSecret3()).isEqualTo("secret3");
                    assertThat(record.getTopicName()).isEqualTo("topic");
                    assertThat(record.getIntermediateCallbackUrl()).isEqualTo("url");
                });
    }

    @Test
    void getAccounts() throws IOException {
        String account = """
                {
                "id": 123,
                "serviceType": "Google",
                "serviceProvider": "Google",
                "active": true,
                "tokenStatus": "active",
                "type": "personal",
                "daemon": false,
                "loginString":"e@google.com",
                "email":"e@google.com",
                "name": "Test",
                "authOrgId": "org1",
                "authUserId": "user1",
                "tokenIssuedAt": "2020-01-01T00:00:00Z",
                "tokenLastActivity": "2020-01-01T00:00:00Z",
                "authScopes": [ "s1" ],
                "authObtainedAt": "2020-01-01T00:00:00Z",
                "authExpiresAt": "2020-01-02T00:00:00Z",
                "createdAt": "2020-01-01T00:00:00Z",
                "updatedAt": "2020-01-01T00:00:00Z"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(offsetPageSingle(account));
        Accounts acc = new Accounts(buildFakeHttp(mockResponse));

        var queryParams = QueryParams.of("serviceType", "Google");

        var response = acc.loadPage(1, 0, queryParams);

        assertThat(response).isNotNull();
        assertThat(response.getTotalSize()).isEqualTo(1);
        assertThat(response.isDone()).isTrue();

        assertThat(response.getRecords())
                .singleElement()
                .satisfies(record -> {
                    assertThat(record.getId()).isEqualTo(123L);
                    assertThat(record.getServiceType()).isEqualTo("Google");
                    assertThat(record.getServiceProvider()).isEqualTo("Google");
                    assertThat(record.isActive()).isTrue();
                });
    }

    @Test
    void upsertPersonalAccount() throws IOException {
        String created = """
                {
                "accountId": 1,
                "accessToken": "at"
                }
                """;

        MockLowLevelHttpResponse mockResponse = successJsonResponse(created);

        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);

        Accounts acc = new Accounts(buildFakeHttp(mockTransport));

        var accDto = new AurAccountDto();
        accDto.setServiceType("IMAP");
        accDto.setAuthScopes(new String[]{"Mail.Read"});
        accDto.setActive(true);
        accDto.setEmail("e@e.com");
        accDto.setAuthString1("pass");
        accDto.setServerUrl("url:993");

        var response = acc.upsertPersonal(accDto);


        String sentBody = mockTransport.getLowLevelHttpRequest().getContentAsString();
        assertThat(asJson(sentBody))
                .extracting("active", "email", "authString1")
                .containsExactly(true, "e@e.com", "pass");

        assertThat(response).isNotNull();
        assertThat(response.getAccountId()).isEqualTo(1);
        assertThat(response.getAccessToken()).isEqualTo("at");
    }

    @Test
    void getAccountById() throws IOException {
        String account = """
                {
                  "id": 1,
                  "serviceType": "Google",
                  "serviceProvider": "Google"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(account);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        Accounts acc = new Accounts(buildFakeHttp(mockTransport));

        var response = acc.read(1L);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/am/accounts/1");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getServiceType()).isEqualTo("Google");
        assertThat(response.getServiceProvider()).isEqualTo("Google");
    }

    @Test
    void deleteAccountById() throws IOException {
        MockLowLevelHttpResponse mockResponse = successJsonResponse("");
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        Accounts acc = new Accounts(buildFakeHttp(mockTransport));

        acc.delete(1L);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/am/accounts/1");
    }

    @Test
    void connect() throws IOException {
        MockLowLevelHttpResponse mockResponse = successJsonResponse("""
        {"status":"OK"}
        """);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        Accounts acc = new Accounts(buildFakeHttp(mockTransport));

        var accDto = new AurAccountDto();
        accDto.setServiceType("IMAP");
        accDto.setAuthScopes(new String[]{"Mail.Read"});
        accDto.setActive(true);
        accDto.setEmail("e@e.com");
        accDto.setAuthString1("pass");
        accDto.setServerUrl("url:993");

        acc.connect(accDto);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/am/accounts/connect");
        var sentBody = mockTransport.getLowLevelHttpRequest().getContentAsString();
        assertThat(asJson(sentBody))
                .extracting("active", "email", "authString1")
                .containsExactly(true, "e@e.com", "pass");
    }

    @Test
    void getServiceAccounts() throws IOException {
        String account = """
                {
                "id": 123,
                "serviceType": "Google",
                "serviceProvider": "Google",
                "active": true,
                "tokenStatus": "active",
                "type": "personal",
                "daemon": true,
                "loginString":"e@google.com",
                "email":"e@google.com",
                "name": "Test",
                "authOrgId": "org1",
                "authUserId": "user1",
                "tokenIssuedAt": "2020-01-01T00:00:00Z",
                "tokenLastActivity": "2020-01-01T00:00:00Z",
                "authScopes": [ "s1" ],
                "authObtainedAt": "2020-01-01T00:00:00Z",
                "authExpiresAt": "2020-01-02T00:00:00Z",
                "createdAt": "2020-01-01T00:00:00Z",
                "updatedAt": "2020-01-01T00:00:00Z"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(offsetPageSingle(account));
        Accounts acc = new Accounts(buildFakeHttp(mockResponse));

        var queryParams = QueryParams.of("serviceType", "Google");

        var response = acc.serviceAccounts.loadPage(1, 0, queryParams);

        assertThat(response).isNotNull();
        assertThat(response.getTotalSize()).isEqualTo(1);
        assertThat(response.isDone()).isTrue();

        assertThat(response.getRecords())
                .singleElement()
                .satisfies(record -> {
                    assertThat(record.getId()).isEqualTo(123L);
                    assertThat(record.getServiceType()).isEqualTo("Google");
                    assertThat(record.getServiceProvider()).isEqualTo("Google");
                    assertThat(record.isActive()).isTrue();
                    assertThat(record.isDaemon()).isTrue();
                });
    }

    @Test
    void upsertServiceAccount() throws IOException {
        String created = """
                {
                  "accountId": 1
                }
                """;

        MockLowLevelHttpResponse mockResponse = successJsonResponse(created);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        Accounts acc = new Accounts(buildFakeHttp(mockTransport));

        // Use a personal data only for test purposes
        var accDto = new AurAccountDto();
        accDto.setServiceType("IMAP");
        accDto.setAuthScopes(new String[]{"Mail.Read"});
        accDto.setActive(true);
        accDto.setEmail("e@e.com");
        accDto.setAuthString1("pass");
        accDto.setServerUrl("url:993");

        var response = acc.serviceAccounts.create(accDto);

        String sentBody = mockTransport.getLowLevelHttpRequest().getContentAsString();
        assertThat(asJson(sentBody))
                .extracting("active", "email", "authString1")
                .containsExactly(true, "e@e.com", "pass");

        assertThat(response).isNotNull();
        assertThat(response.getAccountId()).isEqualTo(1);
    }

    @Test
    void upsertManagedAccount() throws IOException {
        String created = """
                {
                  "accountId": 1
                }
                """;

        MockLowLevelHttpResponse mockResponse = successJsonResponse(created);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        Accounts acc = new Accounts(buildFakeHttp(mockTransport));

        var accDto = new AurAccountDto();
        accDto.setServiceType("IMAP");
        accDto.setAuthScopes(new String[]{"Mail.Read"});
        accDto.setActive(true);
        accDto.setEmail("e@e.com");
        accDto.setAuthString1("pass");
        accDto.setServerUrl("url:993");

        var response = acc.serviceAccounts.upsertManaged(accDto, 2);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .startsWith("https://api.aurinko.io/v1/am/svc_accounts/2/accounts");

        String sentBody = mockTransport.getLowLevelHttpRequest().getContentAsString();
        assertThat(asJson(sentBody))
                .extracting("active", "email", "authString1")
                .containsExactly(true, "e@e.com", "pass");

        assertThat(response).isNotNull();
        assertThat(response.getAccountId()).isEqualTo(1);
    }



    private String offsetPageSingle(String record) {
        return """
            {
            "records": [ %s ],
            "totalSize": 1,
            "offset": 0,
            "done": true
            }
            """.formatted(record);
    }
}
