package com.targa.labs.models;

import java.time.Instant;

import jakarta.persistence.PrePersist;

public class AuditingEntityListener {

  @PrePersist
  void preCreate(AbstractEntity auditable){
    Instant now = Instant.now();
    auditable.setCreatedDate(now);
    auditable.setLastModifiedDate(now);
  }

  void preUpdate(AbstractEntity auditable){
    auditable.setLastModifiedDate(Instant.now());
  }
}
