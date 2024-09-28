package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.estoque.NotaFiscalItens;
import br.com.codexloja.v1.domain.repository.NotaFiscalItensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotaFiscalItensService {

    @Autowired
    private NotaFiscalItensRepository notaFiscalItensRepository;

    public List<NotaFiscalItens> findByNumero(String numeroNotaFiscal) {
        return notaFiscalItensRepository.findByNumero(numeroNotaFiscal);
    }
}
