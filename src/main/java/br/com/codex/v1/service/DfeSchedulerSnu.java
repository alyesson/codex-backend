package br.com.codex.v1.service;

import br.com.codex.v1.utilitario.ControleNsuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DfeSchedulerSnu {

    @Autowired
    private ControleNsuService consultarDocumentos;

    @Scheduled(fixedRate = 3600000) // A cada hora (em milissegundos)
    public void consultarDocumentos() {
        // Exemplo: consulta para um CNPJ fixo e ambiente de produção
        consultarDocumentos.consultarDocumentos("12345678901234", "PRODUCAO");
    }
}
