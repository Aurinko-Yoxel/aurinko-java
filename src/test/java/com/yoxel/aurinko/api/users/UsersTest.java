package com.yoxel.aurinko.api.users;

import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.api.FakeHttpImpl;
import com.yoxel.aurinko.api.Users;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurEndUserDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UsersTest implements FakeHttpImpl {

    @Test
    void page() throws IOException {
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
        AurEndUserDto.Page r = new Users(buildFakeHttp(mockTransport))
                .loadPage(10, 0, QueryParams.EMPTY);

        assertThat(mockTransport.getLowLevelHttpRequest().getUrl())
                .isEqualTo("https://api.aurinko.io/v1/users?limit=10&offset=0");

        assertThat(r).isNotNull();
        assertThat(r.getRecords().length).isEqualTo(1);
        assertThat(r.getRecords()[0].getId()).isEqualTo("id");
    }
}
