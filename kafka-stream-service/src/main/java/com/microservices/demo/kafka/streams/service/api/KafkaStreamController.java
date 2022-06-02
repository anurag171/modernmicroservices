package com.microservices.demo.kafka.streams.service.api;

import com.microservices.demo.kafka.streams.service.model.KafkaStreamResponseModel;
import com.microservices.demo.kafka.streams.service.runner.StreamsRunner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/",produces = "application/vnd.api.v1+json")
@Slf4j
@RequiredArgsConstructor
public class KafkaStreamController {
  private final StreamsRunner<String,Long> streamsRunner;

  @GetMapping("get-word-count-by-word/{word}")
  @Operation(summary = "get-word-count-by-word")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",description = "success",content = {
          @Content(mediaType = "application/vnd.api.v1+json",schema = @Schema(implementation = KafkaStreamResponseModel.class))
      }),
      @ApiResponse(responseCode = "400",description = "not found "),
      @ApiResponse(responseCode = "500",description = "unexpected error")
  })
  public @ResponseBody
  ResponseEntity<KafkaStreamResponseModel> getWordCountByWord
      (@PathVariable @NotEmpty String word){
    Long wordCount = streamsRunner.getValueByKey(word);
     log.info("Word count [{}] for word [{}]",wordCount,word);
     return ResponseEntity.ok(KafkaStreamResponseModel.builder()
         .wordCount(wordCount).word(word).build());
  }
}
