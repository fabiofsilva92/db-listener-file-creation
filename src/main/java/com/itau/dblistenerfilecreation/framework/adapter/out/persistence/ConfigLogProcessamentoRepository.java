package com.itau.dblistenerfilecreation.framework.adapter.out.persistence;

import com.itau.dblistenerfilecreation.domain.entities.ConfigLogProcessamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigLogProcessamentoRepository  extends JpaRepository<ConfigLogProcessamento, String> {
}
