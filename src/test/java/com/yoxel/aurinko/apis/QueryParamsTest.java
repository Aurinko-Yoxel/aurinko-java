package com.yoxel.aurinko.apis;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class QueryParamsTest {

    @Test
    void paramWithWhitespace() throws IOException {
        var params = QueryParams.of("param1", "v1 v2 v3");

        assertThat(params.toUrlString()).isEqualTo("?param1=v1%20v2%20v3");
    }

    @Test
    void paramWithAmpersand() throws IOException {
        var params = QueryParams.of("param1", "v1&v2");

        assertThat(params.toUrlString()).isEqualTo("?param1=v1%26v2");
    }

    @Test
    void paramWithEquals() throws IOException {
        var params = QueryParams.of("param1", "v1=v2");

        assertThat(params.toUrlString()).isEqualTo("?param1=v1%3Dv2");
    }
}
