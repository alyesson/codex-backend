package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.vendas.Venda;
import br.com.codexloja.v1.domain.dto.VendaDto;
import br.com.codexloja.v1.domain.repository.VendaRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    public Venda create(VendaDto vendaDto){
        vendaDto.setId(null);
        Venda objVenda = new Venda(vendaDto);
        return vendaRepository.save(objVenda);
    }

    public Venda findById(Integer id){
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
