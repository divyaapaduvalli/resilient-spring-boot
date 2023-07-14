package com.divyaa.resilientspringboot.controller;

import com.divyaa.resilientspringboot.communicator.ExternalAPICaller;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Divyaa P
 */
@RestController
@RequestMapping("/api/")
public class ResilientAppController {
    private final ExternalAPICaller externalAPICaller;

    @Autowired
    public ResilientAppController(ExternalAPICaller externalAPICaller) {
        this.externalAPICaller = externalAPICaller;
    }

    @GetMapping("/endpoint")
    @Retry(name = "endpoint", fallbackMethod = "fallbackAfterRetry")
    public String api() {
        return externalAPICaller.callApi();
    }

    public String fallbackAfterRetry(Exception ex) {
        return "all retries have exhausted";
    }
}
