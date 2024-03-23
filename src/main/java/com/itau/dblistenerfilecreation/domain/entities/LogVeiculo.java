package com.itau.dblistenerfilecreation.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "log_veiculo")
public class LogVeiculo {
    @Id
    private Long id;
    private Long veiculoId;
    private String operacao;
    private LocalDateTime dataModificacao;
    private String dadosVeiculo;
}
