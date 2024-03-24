package com.itau.dblistenerfilecreation.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConfigLogProcessamento {
    @Id
    private String chave;
    private LocalDateTime valor;

}