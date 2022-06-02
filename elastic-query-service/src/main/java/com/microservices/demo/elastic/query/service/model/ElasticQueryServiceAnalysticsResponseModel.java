package com.microservices.demo.elastic.query.service.model;

import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQueryServiceAnalysticsResponseModel {
    private List<ElasticQueryServiceResponseModel> queryResponseModel;
    private Long wordCount;

}
