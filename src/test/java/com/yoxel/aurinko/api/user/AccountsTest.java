package com.yoxel.aurinko.api.user;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.api.FakeHttpImpl;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurAccount;
import com.yoxel.aurinko.bean.AurEndUserAccountDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AccountsTest implements FakeHttpImpl {

    @Test
    void page() throws IOException {
        String data = """
                {
                  "records":[
                    {
                      "id": 2,
                      "parentId": 123
                    }
                  ]
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEndUserAccountDto.Page r = new User(buildFakeHttp(mockTransport))
                .accounts
                .loadPage();

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/user/accounts");

        assertThat(r).isNotNull();
        assertThat(r.getRecords().length).isEqualTo(1);
        assertThat(r.getRecords()[0].getId()).isEqualTo(2);
        assertThat(r.getRecords()[0].getParentId()).isEqualTo(123);
    }

    @Test
    void read() throws IOException {
        Long id = 1L;
        String data = """
                {
                  "id": 2,
                  "parentId": 123
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEndUserAccountDto r = new User(buildFakeHttp(mockTransport))
                .accounts
                .read(id);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/user/accounts/" + id);

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo(2);
        assertThat(r.getParentId()).isEqualTo(123);
    }

    @Test
    void delete() throws IOException {
        Long id = 1L;
        String data = """
                {
                  "status": "ok"
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        new User(buildFakeHttp(mockTransport))
                .accounts
                .delete(id);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/user/accounts/" + id);
    }

    @Test
    void makeManaged() throws IOException {
        Long id = 1L;
        String data = """
                {
                  "id": 2,
                  "parentId": 123
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurEndUserAccountDto r = new User(buildFakeHttp(mockTransport))
                .accounts
                .makeManaged(id, QueryParams.EMPTY);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/user/accounts/" + id + "/managed");

        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo(2);
        assertThat(r.getParentId()).isEqualTo(123);
    }
}
