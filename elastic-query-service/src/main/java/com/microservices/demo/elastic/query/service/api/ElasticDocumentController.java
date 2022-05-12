package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import java.util.ArrayList;
import java.util.List;
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

  @GetMapping("/")
  public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments(){
    List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
    log.info(MESSAGE,response.size());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel>
        getDocumentById(@PathVariable("id") @NotEmpty String id){
    ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
    log.info(MESSAGE,response);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/get-document-by-text")
  public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>>
  getDocumentsByText(@RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel){
    List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentByText(
                                                          elasticQueryServiceRequestModel.getText());
    log.info(MESSAGE,response);
    return ResponseEntity.ok(response);
  }
}
