package com.crusoe.zeebecentre.importers;

import io.camunda.zeebe.protocol.record.intent.JobIntent;
import io.zeebe.exporter.proto.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crusoe.zeebecentre.entity.JobEntity;
import com.crusoe.zeebecentre.repository.JobRepository;

@Component
public class JobImporter {

  @Autowired private JobRepository jobRepository;

  public void importJob(final Schema.JobRecord record) {

    final JobIntent intent = JobIntent.valueOf(record.getMetadata().getIntent());
    final long key = record.getMetadata().getKey();
    final long timestamp = record.getMetadata().getTimestamp();

    final JobEntity entity =
        jobRepository
            .findById(key)
            .orElseGet(
                () -> {
                  final JobEntity newEntity = new JobEntity();
                  newEntity.setKey(key);
                  newEntity.setProcessInstanceKey(record.getProcessInstanceKey());
                  newEntity.setElementInstanceKey(record.getElementInstanceKey());
                  newEntity.setJobType(record.getType());
                  return newEntity;
                });

    entity.setState(intent.name().toLowerCase());
    entity.setTimestamp(timestamp);
    entity.setWorker(record.getWorker());
    entity.setRetries(record.getRetries());
    jobRepository.save(entity);
  }

}
