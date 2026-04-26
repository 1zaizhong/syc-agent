// ChatResponse.java
package com.syc.salesAgent.controller;

public record ChatResponse(
        String sessionId,
        String reply,
        long durationMs
) {}