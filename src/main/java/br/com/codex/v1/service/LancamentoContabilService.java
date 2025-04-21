package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.LancamentoContabil;
import br.com.codex.v1.domain.dto.LancamentoContabilDto;
import br.com.codex.v1.domain.repository.LancamentoContabilRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LancamentoContabilService {

    @Autowired
    private LancamentoContabilRepository lancamentoContabilRepository;


    public List<LancamentoContabil> findAllByYearAndMonth(Integer ano, Integer mes) {
        return lancamentoContabilRepository.findAllByYearAndMonth(ano, mes);
    }

    public List<LancamentoContabil> findAllByYearRange(Date anoInicio, Date anoFim) {
        return lancamentoContabilRepository.findAllByYearRange(anoInicio, anoFim);
    }

    public LancamentoContabil findById(Integer id) {
        Optional<LancamentoContabil> objLancamento = lancamentoContabilRepository.findById(id);
        return objLancamento.orElseThrow(() -> new ObjectNotFoundException("Lançamento não encontrado"));
    }

    public void delete(Integer id) {
        lancamentoContabilRepository.deleteById(id); ;
    }

    public LancamentoContabil create(@Valid LancamentoContabilDto lancamentoContabilDto) {
        lancamentoContabilDto.setId(null);
        LancamentoContabil lancamentoContabil = new LancamentoContabil(lancamentoContabilDto);
        return lancamentoContabilRepository.save(lancamentoContabil);
    }
}
