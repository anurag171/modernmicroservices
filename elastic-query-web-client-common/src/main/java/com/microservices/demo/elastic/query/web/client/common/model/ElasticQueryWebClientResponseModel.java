package com.microservices.demo.elastic.query.web.client.common.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryWebClientResponseModel {
    private String id;
    private Long userId;
    private String text;
    private LocalDateTime createdAt;
}
