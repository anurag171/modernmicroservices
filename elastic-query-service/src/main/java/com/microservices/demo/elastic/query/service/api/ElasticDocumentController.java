package com.microservices.demo.elastic.query.service.api;

import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModelV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
@Slf4j
@RequiredArgsConstructor
public class ElasticDocumentController {

  private final ElasticQueryService elasticQueryService;

  private static final String MESSAGE = "Elastic search returned {} of documents";

  @Value("${server.port}")
  private String port;

  @PostAuthorize("hasPermission(returnObject, 'READ')")
  @Operation(description = "Get all elastic documents for v1")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful response", content = {
          @Content(mediaType = "application/vnd.api.v1+json", schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
      }),
      @ApiResponse(responseCode = "400", description = "Not found "),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  @GetMapping("/")
  public @ResponseBody
  ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
    List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
    log.info(MESSAGE, response.size());
    return ResponseEntity.ok(response);
  }

  @Operation(description = "Get all elastic documents for v2")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful response", content = {
          @Content(mediaType = "application/vnd.api.v2+json", schema = @Schema(implementation = ElasticQueryServiceResponseModelV2.class))
      }),
      @ApiResponse(responseCode = "400", description = "Not found "),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  @GetMapping(value = "/", produces = "application/vnd.api.v2+json")
  public @ResponseBody
  ResponseEntity<List<ElasticQueryServiceResponseModelV2>> getAllDocumentsV2() {
    List<ElasticQueryServiceResponseModelV2> response = elasticQueryService.getAllDocuments()
        .stream().map(this::getModelV2).collect(Collectors.toList());
    log.info(MESSAGE, response.size());
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasPermission(#id, 'ElasticQueryServiceResponseModel','READ')")
  @Operation(description = "Get v1 elastic document for id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful response", content = {
          @Content(mediaType = "application/vnd.api.v1+json", schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
      }),
      @ApiResponse(responseCode = "400", description = "Not found "),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  @GetMapping("/{id}")
  public @ResponseBody
  ResponseEntity<ElasticQueryServiceResponseModel>
  getDocumentById(@PathVariable("id") @NotEmpty String id) {
    ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
    log.info(MESSAGE, response);
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasPermission(#id, 'ElasticQueryServiceResponseModel','READ')")
  @Operation(description = "Get v2 elastic document for id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful response", content = {
          @Content(mediaType = "application/vnd.api.v2+json", schema = @Schema(implementation = ElasticQueryServiceResponseModelV2.class))
      }),
      @ApiResponse(responseCode = "400", description = "Not found "),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
  public @ResponseBody
  ResponseEntity<ElasticQueryServiceResponseModelV2>
  getDocumentByIdV2(@PathVariable("id") @NotEmpty String id) {
    ElasticQueryServiceResponseModelV2 response = getModelV2(
        elasticQueryService.getDocumentById(id));
    log.info(MESSAGE, response);
    return ResponseEntity.ok(response);
  }


  private ElasticQueryServiceResponseModelV2 getModelV2(
      ElasticQueryServiceResponseModel responseModel) {
    ElasticQueryServiceResponseModelV2 responseModelV2 = ElasticQueryServiceResponseModelV2.builder()
        .id(Long.valueOf(responseModel.getId())).createdAt(responseModel.getCreatedAt())
        .userId(responseModel.getUserId()).text(responseModel.getText()).build();
    responseModelV2.add(responseModel.getLinks());
    return responseModelV2;
  }

  @PreAuthorize("hasRole('APP_USER_ROLE') || hasRole('APP_SUPER_USER_ROLE') || hasAuthority('SCOPE_APP_USER_ROLE')")
  @PostAuthorize("hasPermission(returnObject, 'READ')")
  @Operation(description = "Get v1 elastic documents for text")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful response", content = {
          @Content(mediaType = "application/vnd.api.v1+json", schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
      }),
      @ApiResponse(responseCode = "400", description = "Not found "),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  @PostMapping("/get-document-by-text")
  public @ResponseBody
  ResponseEntity<List<ElasticQueryServiceResponseModel>>
  getDocumentsByText(
      @RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel) {
    List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentByText(
        elasticQueryServiceRequestModel.getText());
    log.info(MESSAGE, response);
    return ResponseEntity.ok(response);
  }

  @Operation(description = "Get v2 elastic documents for text")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful response", content = {
          @Content(mediaType = "application/vnd.api.v2+json", schema = @Schema(implementation = ElasticQueryServiceResponseModelV2.class))
      }),
      @ApiResponse(responseCode = "400", description = "Not found "),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  @PostMapping(value = "/get-document-by-text", produces = "application/vnd.api.v2+json")
  public @ResponseBody
  ResponseEntity<List<ElasticQueryServiceResponseModelV2>>
  getDocumentsByTextV2(
      @RequestBody @Valid ElasticQueryServiceRequestModel elasticQueryServiceRequestModel) {
    List<ElasticQueryServiceResponseModelV2> response = elasticQueryService.getDocumentByText(
            elasticQueryServiceRequestModel.getText()).stream().map(this::getModelV2)
        .collect(Collectors.toList());
    log.info(MESSAGE, response);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping(value = "/get-document-by-text", produces = "application/vnd.api.v2+json")
  public @ResponseBody
  ResponseEntity<String>
  flushDocumentsV2() {
    String response = elasticQueryService.deleteAll("");
    log.info(MESSAGE, response);
    return ResponseEntity.ok(response);
  }
}
