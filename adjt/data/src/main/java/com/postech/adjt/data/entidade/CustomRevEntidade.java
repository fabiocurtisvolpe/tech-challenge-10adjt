package com.postech.adjt.data.entidade;

import java.time.LocalDateTime;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@RevisionEntity
@Table(name = "custom_rev_model")
public class CustomRevEntidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    public Long rev;

    @RevisionTimestamp
    public LocalDateTime revtstmp;
}
