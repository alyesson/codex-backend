package br.com.codex.v1.service;

import br.com.codex.v1.domain.vendas.VendaItens;
import br.com.codex.v1.domain.dto.VendaItensDto;
import br.com.codex.v1.domain.repository.VendaItensRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VendaItensService {

    @Autowired
    private VendaItensRepository vendaItensRepository;

    public List<VendaItens> create(List<VendaItensDto> vendaItensDtoList) {
        List<VendaItens> vendaItensList = new ArrayList<>();
        for (VendaItensDto vendaItensDto : vendaItensDtoList) {
            VendaItens vendaItens = new VendaItens(vendaItensDto);
            vendaItensList.add(vendaItensRepository.save(vendaItens));
        }
        return vendaItensList;
    }

    public VendaItens findById(Long id) {
        Optional<VendaItens> optionalVendaItens = vendaItensRepository.findById(id);
        return optionalVendaItens.orElseThrow(() -> new ObjectNotFoundException("Item de venda não encontrado"));
    }

    public List<VendaItens> findAll() {
        return vendaItensRepository.findAll();
    }
}
