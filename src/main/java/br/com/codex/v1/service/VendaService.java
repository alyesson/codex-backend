package br.com.codex.v1.service;


import br.com.codex.v1.domain.dto.VendaDto;
import br.com.codex.v1.domain.dto.VendaItensDto;
import br.com.codex.v1.domain.repository.VendaItensRepository;
import br.com.codex.v1.domain.repository.VendaRepository;
import br.com.codex.v1.domain.vendas.Venda;
import br.com.codex.v1.domain.vendas.VendaItens;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;
    
    @Autowired
    private VendaItensRepository vendaItensRepository;

    public Venda create(VendaDto vendaDto){
        vendaDto.setId(null);
        Venda objVenda = new Venda(vendaDto);
        objVenda = vendaRepository.save(objVenda);

        for(VendaItensDto itemDto : vendaDto.getItens()){
            VendaItens item = new VendaItens();
            item.setCodigo(itemDto.getCodigo());
            item.setDescricao(itemDto.getDescricao());
            item.setDesconto(itemDto.getDesconto());
            item.setQuantidade(itemDto.getQuantidade());
            item.setValorUnitario(itemDto.getValorUnitario());
            item.setValorTotal(itemDto.getValorTotal());
            item.setVenda(objVenda);
            vendaItensRepository.save(item);
        }
        return objVenda;
    }

    public Venda findById(Long id){
        Optional<Venda> objVenda = vendaRepository.findById(id);
        return objVenda.orElseThrow(() -> new ObjectNotFoundException("Venda não encontrada"));
    }

    public List<Venda> findAllByYear(Integer ano){
        return vendaRepository.findAllByYear(ano);
    }

    public List<Venda> findAllByYearAndMonth(Integer ano, Integer mes) {
        return vendaRepository.findAllByYearAndMonth(ano, mes);
    }

    public int contaVendasMes(Integer ano, Integer mes) {
        int quantidade = vendaRepository.countByDataVenda(ano, mes);
        return quantidade;
    }

    public List<Venda> findAllVendaPeriodo(Date dataInicial, Date dataFinal) {
        return vendaRepository.findAllVendaPeriodo(dataInicial, dataFinal);
    }

    public List<Object[]> findVendedoresByNumeroVendas(Date dataInicial, Date dataFinal) {
        return vendaRepository.findVendedoresByNumeroVendas(dataInicial, dataFinal);
    }

    public List<Object[]> findByVendasClientes(Date dataInicial, Date dataFinal) {
        return vendaRepository.findByVendasClientes(dataInicial, dataFinal);
    }
}
