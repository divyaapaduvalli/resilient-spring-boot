package com.divyaa.resilientspringboot.controller;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Divyaa P
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResilientAppControllerUnitTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @RegisterExtension
    static WireMockExtension EXTERNAL_SERVICE = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig()
                    .port(9090))
            .build();

    @Test
    public void testRetry() {
        EXTERNAL_SERVICE.stubFor(get("/api/external")
                .willReturn(ok()));
        ResponseEntity<String> response1 = restTemplate.getForEntity("/api/endpoint", String.class);
        EXTERNAL_SERVICE.verify(1, getRequestedFor(urlEqualTo("/api/external")));

        EXTERNAL_SERVICE.resetRequests();

        EXTERNAL_SERVICE.stubFor(get("/api/external")
                .willReturn(serverError()));
        ResponseEntity<String> response2 = restTemplate.getForEntity("/api/endpoint", String.class);
        assertThat(response2.getBody()).isEqualTo("all retries have exhausted");
        EXTERNAL_SERVICE.verify(3, getRequestedFor(urlEqualTo("/api/external")));
    }
}
