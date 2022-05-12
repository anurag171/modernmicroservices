package com.microservices.demo.elastic.query.service.model.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.microservices.demo.elastic.model.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.api.ElasticDocumentController;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ElasticQueryResponseModelAssembler
    extends
    RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponseModel> {

  private final ElasticToResponseModelTransformer responseModelTransformer;

  public ElasticQueryResponseModelAssembler(
      ElasticToResponseModelTransformer responseModelTransformer) {
    super(ElasticDocumentController.class, ElasticQueryServiceResponseModel.class);
    this.responseModelTransformer = responseModelTransformer;
  }

  @Override
  public ElasticQueryServiceResponseModel toModel(TwitterIndexModel entity) {
    ElasticQueryServiceResponseModel elasticQueryServiceResponseModel =
        responseModelTransformer.getResponseModel(entity);
    elasticQueryServiceResponseModel.add(
        linkTo(methodOn(ElasticDocumentController.class)
            .getDocumentById(entity.getId())).withSelfRel());

    elasticQueryServiceResponseModel.add(
        linkTo(ElasticDocumentController.class)
            .withRel("documents")
    );
    return elasticQueryServiceResponseModel;
  }


  public List<ElasticQueryServiceResponseModel> toModels(List<TwitterIndexModel> entity) {
   return  entity.stream()
       .map(this::toModel).collect(Collectors.toList());
  }
}
