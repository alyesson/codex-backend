package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.dto.HistoricoRemocaoContaReceberDto;
import br.com.codexloja.v1.domain.financeiro.HistoricoRemocaoContaReceber;
import br.com.codexloja.v1.domain.repository.HistoricoRemocaoContaReceberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class HistoricoRemocaoContaReceberService {

    String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));

    @Autowired
    private HistoricoRemocaoContaReceberRepository historicoRemocaoContaReceberRepository;

    public HistoricoRemocaoContaReceber create(HistoricoRemocaoContaReceberDto historicoRemocaoContaDto) {
        historicoRemocaoContaDto.setId(null);
        historicoRemocaoContaDto.setDataRemocao(java.sql.Date.valueOf(dataAtual));
        HistoricoRemocaoContaReceber obj = new HistoricoRemocaoContaReceber(historicoRemocaoContaDto);
        return historicoRemocaoContaReceberRepository.save(obj);
    }
}
