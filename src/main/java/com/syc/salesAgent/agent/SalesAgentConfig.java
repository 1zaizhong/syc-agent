package com.syc.salesAgent.agent;

import com.syc.salesAgent.memory.MysqlChatMemoryStore;
import com.syc.salesAgent.tools.*;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SalesAgentConfig {

    private final ChatModel chatLanguageModel;
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
                .tools(salesQueryTool,
                       salesSummaryTool,
                       salesTrendTool,
                       chartGeneratorTool,
                       anomalyDetectionTool)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.builder()
                                .id(memoryId)
                                .maxMessages(20)         // 保留最近 20 条消息
                                .chatMemoryStore(chatMemoryStore)
                                .build())
                .build();
    }
}