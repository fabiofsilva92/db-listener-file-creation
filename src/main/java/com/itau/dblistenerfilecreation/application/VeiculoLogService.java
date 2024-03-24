package com.itau.dblistenerfilecreation.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itau.dblistenerfilecreation.domain.entities.ConfigLogProcessamento;
import com.itau.dblistenerfilecreation.domain.entities.LogVeiculo;
import com.itau.dblistenerfilecreation.framework.adapter.out.persistence.ConfigLogProcessamentoRepository;
import com.itau.dblistenerfilecreation.framework.adapter.out.persistence.VeiculoLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class VeiculoLogService {

    @Autowired
    private VeiculoLogRepository logVeiculoRepository;

    @Autowired
    private ConfigLogProcessamentoRepository configRepository;

    @Autowired
//    private S3UploadFileService s3UploadFileService;

    private static final String CHAVE_ULTIMA_DATA_PROCESSAMENTO = "ultima_data_processamento_veiculos";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 60000) // Executa a cada minuto
    public void processarLogs() {
        log.info("PROCESSANDO LOG");
        LocalDateTime ultimaDataProcessada = getUltimaDataProcessada();
        List<LogVeiculo> logsRecentes = logVeiculoRepository.findByDataModificacaoAfter(ultimaDataProcessada);

        for (LogVeiculo logVeiculo : logsRecentes) {

            log.info("Veiculo modificado: " +logVeiculo);
            try {
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                Object dadosVeiculo = objectMapper.readValue(logVeiculo.getDadosVeiculo(), Object.class);
//                s3UploadFileService.uploadFile(logVeiculo, dadosVeiculo, objectMapper);
                File file = new File("C:/tmp/export_" + logVeiculo.getVeiculoId() +"_"+logVeiculo.getOperacao()+ "_" + System.currentTimeMillis() + ".json");

                objectMapper.writeValue(file, dadosVeiculo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        atualizarUltimaDataProcessada(LocalDateTime.now());
    }

    public LocalDateTime getUltimaDataProcessada() {
        return configRepository.findById(CHAVE_ULTIMA_DATA_PROCESSAMENTO)
                .map(ConfigLogProcessamento::getValor)
                .orElse(LocalDateTime.of(1970, 1, 1, 0, 0)); // Valor padrão se não houver registro
    }

    public void atualizarUltimaDataProcessada(LocalDateTime data) {
        ConfigLogProcessamento config = new ConfigLogProcessamento();
        config.setChave(CHAVE_ULTIMA_DATA_PROCESSAMENTO);
        config.setValor(data);
        configRepository.save(config);
    }
}
