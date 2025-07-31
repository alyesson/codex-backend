package br.com.codex.v1.service;

import br.com.codex.v1.domain.fiscal.ImportarXmlItens;
import br.com.codex.v1.domain.repository.ImportarXmlItensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportarXmlItensService {

    @Autowired
    private ImportarXmlItensRepository importarXmlItensRepository;

    public List<ImportarXmlItens> findByNumero(String numeroNotaFiscal) {
        return importarXmlItensRepository.findByNumero(numeroNotaFiscal);
    }
}
