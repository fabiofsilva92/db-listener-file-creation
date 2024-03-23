package com.itau.dblistenerfilecreation.application;

import com.itau.dblistenerfilecreation.domain.entities.LogVeiculo;
import com.itau.dblistenerfilecreation.framework.adapter.out.persistence.VeiculoLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class VeiculoLogService {

    @Autowired
    private VeiculoLogRepository logVeiculoRepository;

//    @Autowired
//    private ConfigLogProcessamentoRepository configRepository;

    private static final String CHAVE_ULTIMA_DATA_PROCESSAMENTO = "ultima_data_processamento_veiculos";

//    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 60000) // Executa a cada minuto
    public void processarLogs() {
        log.info("PROCESSANDO LOG");
        LocalDateTime ultimaDataProcessada = getUltimaDataProcessada();
        List<LogVeiculo> logsRecentes = logVeiculoRepository.findByDataModificacaoAfter(ultimaDataProcessada);

        for (LogVeiculo logVeiculo : logsRecentes) {

            log.info("Veiculo modificado: " +logVeiculo);
            //TODO ajustar logica para sempre atualizar datas;
//            try {
//                Object dadosVeiculo = objectMapper.readValue(log.getDadosVeiculo(), Object.class);
//                File file = new File("export_" + log.getVeiculoId() + "_" + System.currentTimeMillis() + ".json");
//                objectMapper.writeValue(file, dadosVeiculo);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        atualizarUltimaDataProcessada(LocalDateTime.now());
    }

    // Métodos getUltimaDataProcessada() e atualizarUltimaDataProcessada()...
    public LocalDateTime getUltimaDataProcessada() {
        return LocalDateTime.of(1970, 1, 1, 0, 0);
//        return configRepository.findById(CHAVE_ULTIMA_DATA_PROCESSAMENTO)
//                .map(ConfigLogProcessamento::getValor)
//                .orElse(LocalDateTime.of(1970, 1, 1, 0, 0)); // Valor padrão se não houver registro
    }

    public void atualizarUltimaDataProcessada(LocalDateTime data) {
//        ConfigLogProcessamento config = new ConfigLogProcessamento();
//        config.setChave(CHAVE_ULTIMA_DATA_PROCESSAMENTO);
//        config.setValor(data);
//        configRepository.save(config);
    }
}
