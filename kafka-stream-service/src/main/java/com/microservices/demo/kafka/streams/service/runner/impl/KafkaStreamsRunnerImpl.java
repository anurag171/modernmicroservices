package com.microservices.demo.kafka.streams.service.runner.impl;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaStreamsConfigData;
import com.microservices.demo.kafka.avro.model.TwitterAnalyticsAvroModel;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.streams.service.runner.StreamsRunner;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KafkaStreams.State;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaStreamsRunnerImpl implements StreamsRunner<String,Long> {

  private static final String REGEX = "\\W+";

  private final KafkaStreamsConfigData kafkaStreamsConfigData;
  private final KafkaConfigData kafkaConfigData;
  private final Properties streamConfiguration;

  private KafkaStreams kafkaStreams;
  private volatile ReadOnlyKeyValueStore<String,Long> keyValueStore;

  @Override
  public void start() {
    final Map<String,String> serdeConfig = Collections.singletonMap(
        kafkaConfigData.getSchemaRegistryUrlKey(),
        kafkaConfigData.getSchemaRegistryUrl());

    final StreamsBuilder streamsBuilder = new StreamsBuilder();

    final KStream<Long, TwitterAvroModel>  twitterAvroModelKStream=
        getTwitterAvroModelKStream(serdeConfig, streamsBuilder);

    createTopology(twitterAvroModelKStream,serdeConfig);

    startStreaming(streamsBuilder);
  }

  private void startStreaming(StreamsBuilder streamsBuilder) {
    final Topology topology = streamsBuilder.build();
    log.info("Defined Topology [{}]",topology.describe());
    kafkaStreams =  new KafkaStreams(topology,streamConfiguration);
    kafkaStreams.start();
    log.info("Kafka stream started");
  }

  @PreDestroy
  public void close(){
    if(kafkaStreams != null){
      kafkaStreams.close();
      log.info("Kafka stream closed");
    }
  }

  private void createTopology(KStream<Long, TwitterAvroModel> twitterAvroModelKStream,
      Map<String, String> serdeConfig) {
    Pattern pattern = Pattern.compile(REGEX,Pattern.UNICODE_CHARACTER_CLASS);
    Serde<TwitterAnalyticsAvroModel> twitterAnalyticsAvroModelSerde =
        getSerdeAnalyticsModel(serdeConfig);

    twitterAvroModelKStream
        .flatMapValues(value-> Arrays.asList(pattern.split(value.getText().toLowerCase())))
        .groupBy((key, word) -> word)
        .count(Materialized.as(kafkaStreamsConfigData.getWordCountStoreName()))
        .toStream()
        .map(mapToAnalysticsModel())
        .to(kafkaStreamsConfigData.getOutputTopicName(),
            Produced.with(Serdes.String(),twitterAnalyticsAvroModelSerde));
  }

  private KeyValueMapper<String,Long, KeyValue<? extends String,? extends TwitterAnalyticsAvroModel>>
  mapToAnalysticsModel() {

    return  (word,count)->{
      return new KeyValue<>(word,TwitterAnalyticsAvroModel
          .newBuilder()
          .setWord(word)
          .setWordCount(count)
          .setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
          .build()
      );
    };
  }

  private Serde<TwitterAnalyticsAvroModel> getSerdeAnalyticsModel(Map<String, String> serdeConfig) {
    final Serde<TwitterAnalyticsAvroModel> serdeTwitterAvroModel = new SpecificAvroSerde<>();
    serdeTwitterAvroModel.configure(serdeConfig,false);
    return serdeTwitterAvroModel;
  }

  private KStream<Long, TwitterAvroModel> getTwitterAvroModelKStream(
      Map<String, String> serdeConfig, StreamsBuilder streamsBuilder) {
    final Serde<TwitterAvroModel> serdeTwitterAvroModel = new SpecificAvroSerde<>();
    serdeTwitterAvroModel.configure(serdeConfig,false);
   return streamsBuilder.stream(kafkaStreamsConfigData.getInputTopicName(), Consumed.with(Serdes.Long(),
        serdeTwitterAvroModel));
  }

  @Override
  public Long getValueByKey(String key) {
    if(kafkaStreams != null && kafkaStreams.state() == State.RUNNING){
      if(keyValueStore == null){
        synchronized (this){
          if(keyValueStore ==null){
            keyValueStore=kafkaStreams.store(StoreQueryParameters.fromNameAndType(kafkaStreamsConfigData.getWordCountStoreName(),
                QueryableStoreTypes.keyValueStore()));
          }
        }
      }
      return keyValueStore.get(key.toLowerCase());
    }
    return 0L;
  }
}
