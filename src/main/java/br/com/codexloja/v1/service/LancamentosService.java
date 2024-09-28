package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.contabilidade.Lancamentos;
import br.com.codexloja.v1.domain.contabilidade.LancamentosContas;
import br.com.codexloja.v1.domain.dto.LancamentosContasDto;
import br.com.codexloja.v1.domain.dto.LancamentosDto;
import br.com.codexloja.v1.domain.repository.LancamentosContasRepository;
import br.com.codexloja.v1.domain.repository.LancamentosRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LancamentosService {

    String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));

    @Autowired
    private LancamentosRepository lancamentosRepository;

    @Autowired
    private LancamentosContasRepository lancamentosContasRepository;

    public Lancamentos create(LancamentosDto lancamentosDto) {
        lancamentosDto.setId(null);
        Lancamentos objLancamentos = new Lancamentos(lancamentosDto);
        objLancamentos = lancamentosRepository.save(objLancamentos);

        for (LancamentosContasDto contasDto : lancamentosDto.getContas()) {
            LancamentosContas item = new LancamentosContas();
            item.setDataLancamento(contasDto.getDataLancamento());
            item.setContaDebito(contasDto.getContaDebito());
            item.setContaCredito(contasDto.getContaCredito());
            item.setCentroCustoDebito(contasDto.getCentroCustoDebito());
            item.setCentroCustoCredito(contasDto.getCentroCustoCredito());
            item.setValorDebito(contasDto.getValorDebito());
            item.setValorCredito(contasDto.getValorCredito());
            item.setLancamentos(objLancamentos);
            lancamentosContasRepository.save(item);
        }
        return objLancamentos;
    }

    public List<LancamentosContas> findAllItensByLancamentoId(Integer lancamentoId) {
        return lancamentosContasRepository.findByLancamentosId(lancamentoId);
    }

    public Lancamentos findById(Integer id){
        Optional<Lancamentos> objLancamentos = lancamentosRepository.findById(id);
        return objLancamentos.orElseThrow(() -> new ObjectNotFoundException("Lançamento não encontrado"));
    }

    public List<LancamentosContas> findAllLancamentos(Date dataInicio, Date dataFim){
        return lancamentosContasRepository.findAllByYearAndMonth(dataInicio,dataFim);
    }

    public void delete(Integer id) {
        lancamentosContasRepository.deleteById(id);
    }
}
