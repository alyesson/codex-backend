package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import br.com.codex.v1.domain.repository.TabelaCfopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TabelaCfopService {

    @Autowired
    private TabelaCfopRepository tabelaCfopRepository;

    public List<TabelaCfop> findByFluxo(String tipoCfop) {
        return tabelaCfopRepository.findByFluxo(tipoCfop);
    }
}
