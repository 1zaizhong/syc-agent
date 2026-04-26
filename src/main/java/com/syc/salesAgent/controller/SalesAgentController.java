package com.syc.salesAgent.controller;

import com.syc.salesAgent.agent.SalesAgent;
import com.syc.salesAgent.memory.MysqlChatMemoryStore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;


import java.time.LocalDate;

@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
@Slf4j
public class SalesAgentController {

    private final SalesAgent salesAgent;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("接收请求: sessionId={}, message={}", request.sessionId(), request.message());
        long start = System.currentTimeMillis();

        String reply = salesAgent.chat(request.sessionId(), request.message(), LocalDate.now().toString());

        long duration = System.currentTimeMillis() - start;
        log.info("请求完成: sessionId={}, durationMs={}", request.sessionId(), duration);

        return ResponseEntity.ok(new ChatResponse(request.sessionId(), reply, duration));
    }

    private final MysqlChatMemoryStore chatMemoryStore;

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> clearSession(@PathVariable String sessionId) {
        chatMemoryStore.deleteMessages(sessionId);
        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> chatStream(@Valid @RequestBody ChatRequest request) {

        log.info("流式请求: sessionId={}", request.sessionId());

        return Flux.create(sink -> {
            salesAgent.chatStream(request.sessionId(), request.message(), LocalDate.now().toString())
                    .onPartialResponse(token -> {
                        // 每个 token（词片）推送一个 SSE 事件
                        sink.next(ServerSentEvent.<String>builder()
                                .event("token")
                                .data(token)
                                .build());
                    })
                    .onCompleteResponse(response -> {
                        // 推送结束信号
                        sink.next(ServerSentEvent.<String>builder()
                                .event("done")
                                .data("[DONE]")
                                .build());
                        sink.complete();
                        log.info("流式响应完成: sessionId={}", request.sessionId());
                    })
                    .onError(error -> {
                        log.error("流式响应出错: sessionId={}", request.sessionId(), error);
                        sink.next(ServerSentEvent.<String>builder()
                                .event("error")
                                .data("服务暂时不可用，请稍后重试")
                                .build());
                        sink.complete();
                    })
                    .start();
        });
    }

}