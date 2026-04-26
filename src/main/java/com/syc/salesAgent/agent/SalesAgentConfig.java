package com.syc.salesAgent.agent;

import com.syc.salesAgent.memory.MysqlChatMemoryStore;
import com.syc.salesAgent.tools.*;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SalesAgentConfig {

    private final ChatModel chatLanguageModel;
    private final StreamingChatModel streamingChatModel;
    private final SalesQueryTool salesQueryTool;
    private final SalesSummaryTool salesSummaryTool;
    private final SalesTrendTool salesTrendTool;
    private final ChartGeneratorTool chartGeneratorTool;
    private final AnomalyDetectionTool anomalyDetectionTool;
    private final MysqlChatMemoryStore chatMemoryStore;   // 注入持久化存储

    @Bean
    public SalesAgent salesAgent() {
        return AiServices.builder(SalesAgent.class)
                .chatModel(chatLanguageModel)
                .streamingChatModel(streamingChatModel)
                .tools(salesQueryTool,
                       salesSummaryTool,
                       salesTrendTool,
                       chartGeneratorTool,
                       anomalyDetectionTool)
                .beforeToolExecution(exec ->
                        log.info("工具调用开始：工具:{}, 参数: {} ",
                                exec.request().name(),
                                exec.request().arguments()))
                .afterToolExecution(exec ->
                        log.info("工具调用完成：工具:{}, 结果长度: {}字符",
                                exec.request().name(),
                                exec.result() != null? exec.result().length() : 0))
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.builder()
                                .id(memoryId)
                                .maxMessages(20)         // 保留最近 20 条消息
                                .chatMemoryStore(chatMemoryStore)
                                .build())

                .build();
    }
}