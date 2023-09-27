package com.crusoe.zeebecentre.importers;

import io.zeebe.exporter.proto.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crusoe.zeebecentre.entity.ErrorEntity;
import com.crusoe.zeebecentre.repository.ErrorRepository;

@Component
public class ErrorImporter {

  @Autowired private ErrorRepository errorRepository;

  public void importError(final Schema.ErrorRecord record) {

    final var metadata = record.getMetadata();
    final var position = metadata.getPosition();

    final var entity =
    
        errorRepository
            .findById(position)
            .orElseGet(
                () -> {
                  final var newEntity = new ErrorEntity();
                  newEntity.setPosition(position);
                  newEntity.setErrorEventPosition(record.getErrorEventPosition());
                  newEntity.setProcessInstanceKey(record.getProcessInstanceKey());
                  newEntity.setExceptionMessage(record.getExceptionMessage());
                  newEntity.setStacktrace(record.getStacktrace());
                  newEntity.setTimestamp(metadata.getTimestamp());
                  return newEntity;
                });

    errorRepository.save(entity);
  }

}
