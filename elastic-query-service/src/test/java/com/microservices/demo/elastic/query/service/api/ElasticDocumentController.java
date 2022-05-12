package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import java.util.ArrayList;
import java.util.List;
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
public class ElasticDocumentController {

  @GetMapping("/")
  public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments(){
    List<ElasticQueryServiceResponseModel> response = new ArrayList<>();
    log.info("Elastic search returned {} of documents",response.size());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel>
        getDocumentById(@PathVariable("id") String id){
    ElasticQueryServiceResponseModel response = ElasticQueryServiceResponseModel.builder().id(id).build();
    log.info("Elastic search returned {} of documents",response);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/get-document-by-text")
  public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>>
  getDocumentsByText(@RequestBody ElasticQueryServiceRequestModel elasticQueryServiceRequestModel){
    List<ElasticQueryServiceResponseModel> response = new ArrayList<>();
    ElasticQueryServiceResponseModel model = ElasticQueryServiceResponseModel.builder()
                                              .text(elasticQueryServiceRequestModel.getText()).build();
    response.add(model);
    log.info("Elastic search returned {} of documents",response);
    return ResponseEntity.ok(response);
  }
}
