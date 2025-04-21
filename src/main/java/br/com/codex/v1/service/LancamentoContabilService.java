package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.ContasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LancamentoContabilService {

    @Autowired
    private ContasRepository contasRepository;


}
