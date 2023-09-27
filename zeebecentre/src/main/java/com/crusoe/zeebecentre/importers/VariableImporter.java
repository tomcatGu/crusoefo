package com.crusoe.zeebecentre.importers;

import io.zeebe.exporter.proto.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crusoe.zeebecentre.entity.VariableEntity;
import com.crusoe.zeebecentre.repository.VariableRepository;

@Component
public class VariableImporter {

  @Autowired
  private VariableRepository variableRepository;

  public void importVariable(final Schema.VariableRecord record) {
    final VariableEntity newVariable = new VariableEntity();
    newVariable.setPosition(record.getMetadata().getPosition());
    newVariable.setPartitionId(record.getMetadata().getPartitionId());

    if (!variableRepository.existsById(newVariable.getGeneratedIdentifier())) {
      newVariable.setTimestamp(record.getMetadata().getTimestamp());
      newVariable.setProcessInstanceKey(record.getProcessInstanceKey());
      newVariable.setName(record.getName());
      newVariable.setValue(record.getValue());
      newVariable.setScopeKey(record.getScopeKey());
      newVariable.setState(record.getMetadata().getIntent().toLowerCase());
      variableRepository.save(newVariable);
    }
  }

}
