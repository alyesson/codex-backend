package br.com.codex.v1.service;

import br.com.codex.v1.domain.repository.FolhaMensalRepository;
import br.com.codex.v1.domain.repository.TabelaDeducaoInssRepository;
import br.com.codex.v1.domain.repository.TabelaImpostoRendaRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FolhaMensalDescontosService {

    @Autowired
    private TabelaDeducaoInssRepository tabelaDeducaoInssRepository;

    @Autowired
    private TabelaImpostoRendaRepository tabelaImpostoRendaRepository;

    @Autowired
    private FolhaMensalRepository folhaMensalRepository;

    public FolhaMensal findByMatriculaColaborador(String numeroMatricula) {
        Optional<FolhaMensal> obj = folhaMensalRepository.findByMatriculaColaborador(numeroMatricula);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Informação não encontrada"));
    }

}
