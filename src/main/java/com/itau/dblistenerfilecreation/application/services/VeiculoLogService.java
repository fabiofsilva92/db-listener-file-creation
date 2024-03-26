package com.itau.dblistenerfilecreation.application.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itau.dblistenerfilecreation.application.services.AwsSnsService;
import com.itau.dblistenerfilecreation.domain.entities.ConfigLogProcessamento;
import com.itau.dblistenerfilecreation.domain.entities.LogVeiculo;
import com.itau.dblistenerfilecreation.framework.adapter.out.persistence.ConfigLogProcessamentoRepository;
import com.itau.dblistenerfilecreation.framework.adapter.out.persistence.VeiculoLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private AwsSnsService snsService;

    @Value("${local.export-file-path}")
    private String exportPath;

    private static final String CHAVE_ULTIMA_DATA_PROCESSAMENTO = "ultima_data_processamento_veiculos";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 6000) // Executa a cada minuto
    public void processarLogs() {
        log.info("PROCESSANDO LOG");
        log.info("PATH de exportação : "+exportPath);
        LocalDateTime ultimaDataProcessada = getUltimaDataProcessada();
        List<LogVeiculo> logsRecentes = logVeiculoRepository.findByDataModificacaoAfter(ultimaDataProcessada);

        for (LogVeiculo logVeiculo : logsRecentes) {

            log.info("Veiculo modificado: " +logVeiculo);
            Object dadosVeiculo = prepareObjectToSnsAndWriteLocally(logVeiculo);
            publishOnAwsSns(dadosVeiculo);
            writeFileLocally(dadosVeiculo, logVeiculo);

        }

        atualizarUltimaDataProcessada(LocalDateTime.now());
    }

    private void publishOnAwsSns(Object dadosVeiculo){
        try {
            log.info("Publicando no AWS SNS");
            String s = objectMapper.writeValueAsString(dadosVeiculo);
            snsService.publish(s);
            log.info("Publicado no topico com sucesso");
        } catch (Exception e) {
            log.error("Erro ao publicar na AWS SNS -> "+e.getMessage());
        }
    }

    private void writeFileLocally(Object dadosVeiculo, LogVeiculo logVeiculo){
        try {
            //Codigo abaixo deixado apenas para teste local
            log.info("Escrevendo arquivo localmente");
            File file = new File(exportPath+"/export_" + logVeiculo.getVeiculoId() +"_"+logVeiculo.getOperacao()+ "_" + System.currentTimeMillis() + ".json");
            objectMapper.writeValue(file, dadosVeiculo);
            log.info("Arquivo escrito localmente com sucesso -> "+file.getAbsolutePath());
        } catch (Exception e) {
            log.error("Error escrevendo localmente -> "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private Object prepareObjectToSnsAndWriteLocally(LogVeiculo logVeiculo){
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return  objectMapper.readValue(logVeiculo.getDadosVeiculo(), Object.class);
        }catch (Exception e){
            log.error("Error parsing JSON -> "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
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
