package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import br.com.codex.v1.domain.repository.XmlNotaFiscalRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class XmlNotaFiscalService {

    @Autowired
    private XmlNotaFiscalRepository xmlNotaFiscalRepository;

    public XmlNotaFiscal findByChaveAcesso(String chave) {
        Optional<XmlNotaFiscal> obj = xmlNotaFiscalRepository.findByChaveAcesso(chave + "-proc");
        return obj.orElseThrow(() -> new ObjectNotFoundException("Nota fiscal não encontrada para a chave "+chave + "-proc"));
    }
}