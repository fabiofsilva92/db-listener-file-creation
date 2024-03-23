package com.itau.dblistenerfilecreation.framework.adapter.out.persistence;

import com.itau.dblistenerfilecreation.domain.entities.LogVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VeiculoLogRepository extends JpaRepository<LogVeiculo, Long> {
    List<LogVeiculo> findByDataModificacaoAfter(LocalDateTime data);
}
