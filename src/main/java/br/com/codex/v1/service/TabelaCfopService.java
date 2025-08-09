package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import br.com.codex.v1.domain.repository.ImportarXmlItensRepository;
import br.com.codex.v1.domain.repository.ImportarXmlRepository;
import br.com.codex.v1.domain.repository.NotaFiscalRepository;
import br.com.codex.v1.domain.repository.TabelaCfopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TabelaCfopService {

    @Autowired
    private TabelaCfopRepository tabelaCfopRepository;

    @Autowired
    private ImportarXmlRepository importarXmlRepository;

    @Autowired
    private NotaFiscalRepository notaFiscalRepository;

    public List<TabelaCfop> findByFluxo(String fluxo) {
        return tabelaCfopRepository.findByFluxo(fluxo);
    }

    // 1. Crie um métudo no seu service para buscar CFOPs utilizados
    public List<TabelaCfop> findCfopsUtilizadosNoPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        // Busca CFOPs distintos das notas de entrada
        Set<Integer> cfopsEntrada = importarXmlRepository.findDistinctCfopEntradaByPeriodo(dataInicio, dataFim);

        // Busca CFOPs distintos das notas de saída (agora usando LocalDate diretamente)
        Set<Integer> cfopsSaida = notaFiscalRepository.findDistinctCfopSaidaByPeriodo(dataInicio, dataFim);

        // Unifica os conjuntos
        Set<Integer> todosCfops = new HashSet<>();
        todosCfops.addAll(cfopsEntrada);
        todosCfops.addAll(cfopsSaida);

        // Busca as descrições na tabela_cfop
        return tabelaCfopRepository.findByCodigos(todosCfops);
    }
}
