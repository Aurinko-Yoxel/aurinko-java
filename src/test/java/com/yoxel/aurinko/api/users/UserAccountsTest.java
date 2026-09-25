package com.yoxel.aurinko.api.users;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.api.FakeHttpImpl;
import com.yoxel.aurinko.api.Users;
import com.yoxel.aurinko.api.user.User;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurAccount;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserAccountsTest implements FakeHttpImpl {

    @Test
    void page() throws IOException {
        String data = """
                {
                  "length": 1,
                  "records": [
                    {
                        "id": 1
                    }
                  ]
                }
                """;
        MockLowLevelHttpResponse mockResponse = successJsonResponse(data);
        MockHttpTransport mockTransport = buildFakeTransport(mockResponse);
        AurAccount.Page r = new Users(buildFakeHttp(mockTransport))
                .accounts("userId")
                .loadPage(10, 0, QueryParams.EMPTY);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/users/userId/accounts?limit=10&offset=0");

        assertThat(r).isNotNull();
        assertThat(r.getRecords().length).isEqualTo(1);
        assertThat(r.getRecords()[0].getId()).isEqualTo(1L);
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
        new Users(buildFakeHttp(mockTransport))
                .accounts("userId")
                .delete(id);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/users/userId/accounts/" + id);
    }
}
