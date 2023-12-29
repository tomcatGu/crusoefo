package com.crusoe.zeebecentre.services;

import com.hazelcast.core.HazelcastInstance;
import io.zeebe.exporter.proto.Schema;
import io.zeebe.hazelcast.connect.java.ZeebeHazelcast;

import com.crusoe.zeebecentre.entity.HazelcastConfig;
import com.crusoe.zeebecentre.repository.HazelcastConfigRepository;
import com.crusoe.zeebecentre.importers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class ZeebeImportService {

  @Autowired private ProcessAndElementImporter processAndElementImporter;
  @Autowired private VariableImporter variableImporter;
  @Autowired private JobImporter jobImporter;
  @Autowired private IncidentImporter incidentImporter;
  @Autowired private MessageImporter messageImporter;
  @Autowired private MessageSubscriptionImporter messageSubscriptionImporter;
  @Autowired private TimerImporter timerImporter;
  @Autowired private ErrorImporter errorImporter;

  @Autowired private HazelcastConfigRepository hazelcastConfigRepository;

  public ZeebeHazelcast importFrom(final HazelcastInstance hazelcast) {

    final var hazelcastConfig =
        hazelcastConfigRepository
            .findById("cfg")
            .orElseGet(
                () -> {
                  final var config = new HazelcastConfig();
                  config.setId("cfg");
                  config.setSequence(-1);
                  return config;
                });

    final var builder =
        ZeebeHazelcast.newBuilder(hazelcast)
            .addProcessListener(
                record -> ifEvent(record, Schema.ProcessRecord::getMetadata, processAndElementImporter::importProcess))
            .addProcessInstanceListener(
                record ->
                    ifEvent(
                        record,
                        Schema.ProcessInstanceRecord::getMetadata,
                        processAndElementImporter::importProcessInstance))
            .addIncidentListener(
                record -> ifEvent(record, Schema.IncidentRecord::getMetadata, incidentImporter::importIncident))
            .addJobListener(
                record -> ifEvent(record, Schema.JobRecord::getMetadata, jobImporter::importJob))
            .addVariableListener(
                record -> ifEvent(record, Schema.VariableRecord::getMetadata, variableImporter::importVariable))
            .addTimerListener(
                record -> ifEvent(record, Schema.TimerRecord::getMetadata, timerImporter::importTimer))
            .addMessageListener(
                record -> ifEvent(record, Schema.MessageRecord::getMetadata, messageImporter::importMessage))
            .addMessageSubscriptionListener(
                record ->
                    ifEvent(
                        record,
                        Schema.MessageSubscriptionRecord::getMetadata,
                            messageSubscriptionImporter::importMessageSubscription))
            .addMessageStartEventSubscriptionListener(
                record ->
                    ifEvent(
                        record,
                        Schema.MessageStartEventSubscriptionRecord::getMetadata,
                            messageSubscriptionImporter::importMessageStartEventSubscription))
            .addErrorListener(errorImporter::importError)
            .postProcessListener(
                sequence -> {
                  hazelcastConfig.setSequence(sequence);
                  hazelcastConfigRepository.save(hazelcastConfig);
                });

    if (hazelcastConfig.getSequence() >= 0) {
      builder.readFrom(hazelcastConfig.getSequence());
    } else {
      builder.readFromHead();
    }

    return builder.build();
  }

  private <T> void ifEvent(
      final T record,
      final Function<T, Schema.RecordMetadata> extractor,
      final Consumer<T> consumer) {
    final var metadata = extractor.apply(record);
    if (isEvent(metadata)) {
      consumer.accept(record);
    }
  }

  private boolean isEvent(final Schema.RecordMetadata metadata) {
    return metadata.getRecordType() == Schema.RecordMetadata.RecordType.EVENT;
  }

}