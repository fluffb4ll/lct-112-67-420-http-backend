package com.fluffb4ll.lct112HttpBackend.client;

import com.fluffb4ll.lct112HttpBackend.dto.ai.AiEvaluationRequestDto;
import com.fluffb4ll.lct112HttpBackend.dto.ai.AiEvaluationResponseDto;
import com.fluffb4ll.lct112HttpBackend.model.enums.IncidentComponent;
import com.fluffb4ll.lct112HttpBackend.model.enums.IncidentSeverity;
import com.fluffb4ll.lct112HttpBackend.service.SystemIncidentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
public class AiEvaluationClient {

    private final RestClient restClient;
    private final String serviceUrl;
    private final SystemIncidentService systemIncidentService;

    public AiEvaluationClient(
            @Value("${app.ai.service-url:http://localhost:8000}") String serviceUrl,
            @Value("${app.ai.connect-timeout-ms:5000}") int connectTimeoutMs,
            @Value("${app.ai.read-timeout-ms:30000}") int readTimeoutMs,
            SystemIncidentService systemIncidentService
    ) {
        this.serviceUrl = serviceUrl;
        this.systemIncidentService = systemIncidentService;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        factory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl(serviceUrl)
                .build();
    }

    public Optional<AiEvaluationResponseDto> requestEvaluation(AiEvaluationRequestDto request) {
        try {
            log.info("Sending operator card {} to AI container for evaluation at {}", request.cardId(), serviceUrl);

            AiEvaluationResponseDto response = restClient.post()
                    .uri("/api/v1/ai/evaluate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(AiEvaluationResponseDto.class);

            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Failed to obtain AI evaluation from AI container for card {}: {}", request.cardId(), e.getMessage(), e);

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            systemIncidentService.logIncident(
                    IncidentSeverity.ERROR,
                    IncidentComponent.AI,
                    "AI evaluation request failed for card " + request.cardId() + ": " + e.getMessage(),
                    sw.toString()
            );

            return Optional.empty();
        }
    }
}
