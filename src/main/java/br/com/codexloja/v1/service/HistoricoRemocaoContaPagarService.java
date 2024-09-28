package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.dto.HistoricoRemocaoContaPagarDto;
import br.com.codexloja.v1.domain.financeiro.HistoricoRemocaoContaPagar;
import br.com.codexloja.v1.domain.repository.HistoricoRemocaoContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class HistoricoRemocaoContaPagarService {

    String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));

    @Autowired
    private HistoricoRemocaoContaPagarRepository historicoRemocaoContaPagarRepository;

    public HistoricoRemocaoContaPagar create(HistoricoRemocaoContaPagarDto historicoRemocaoContaDto) {
        historicoRemocaoContaDto.setId(null);
        historicoRemocaoContaDto.setDataRemocao(java.sql.Date.valueOf(dataAtual));
        HistoricoRemocaoContaPagar obj = new HistoricoRemocaoContaPagar(historicoRemocaoContaDto);
        return historicoRemocaoContaPagarRepository.save(obj);
    }
}
