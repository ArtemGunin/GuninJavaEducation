package com.nix11;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Request {
    private final String ip;
    private final String userAgentHead;
    private final LocalDateTime requestTime;

    public Request(String ip, String userAgentHead) {
        this.requestTime = LocalDateTime.now();
        this.ip = ip;
        this.userAgentHead = userAgentHead;
    }
}
