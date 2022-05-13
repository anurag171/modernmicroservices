package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModelV2;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/documents")
@Slf4j
@RequiredArgsConstructor
public class ElasticDocumentController {

  private final ElasticQueryService elasticQueryService;

  private static final  String MESSAGE = "Elastic search returned {} of documents";

  @GetMapping("/v1")
  public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments(){
    List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
    log.info(MESSAGE,response.size());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/v1/{id}")
  public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel>
        getDocumentById(@PathVariable("id") @NotEmpty String id){
    ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
    log.info(MESSAGE,response);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/v2/{id}")
  public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModelV2>
  getDocumentByIdV2(@PathVariable("id") @NotEmpty String id){
    ElasticQueryServiceResponseModelV2 response = getV2Model(elasticQueryService.getDocumentById(id));
    log.info(MESSAGE,response);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/v1/get-document-by-text")
  public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>>
  getDocumentsByText(@RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel){
    List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentByText(
                                                          elasticQueryServiceRequestModel.getText());
    log.info(MESSAGE,response);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/v2/get-document-by-text")
  public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModelV2>>
  getDocumentsByTextV2(@RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel){
    List<ElasticQueryServiceResponseModelV2> responseV2 = elasticQueryService.getDocumentByText(
                                                          elasticQueryServiceRequestModel.getText())
        .stream().map(this::getV2Model).collect(Collectors.toList());
    log.info(MESSAGE,responseV2);
    return ResponseEntity.ok(responseV2);
  }

  private ElasticQueryServiceResponseModelV2 getV2Model(ElasticQueryServiceResponseModel responseModel ){
    ElasticQueryServiceResponseModelV2 responseModelV2 = ElasticQueryServiceResponseModelV2.builder()
                                      .id(Long.parseLong(responseModel.getId()))
        .text(responseModel.getText()).userId(responseModel.getUserId())
        .createdAt(responseModel.getCreatedAt())
        .build();
    responseModelV2.add(responseModel.getLinks());
    return responseModelV2;
  }
}
