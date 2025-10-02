package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.AlteraSalarioLoteDto;
import br.com.codex.v1.domain.repository.AlteraSalarioLoteRepository;
import br.com.codex.v1.domain.rh.AlteraSalarioLote;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AlteraSalarioLoteService {

    @Autowired
    private AlteraSalarioLoteRepository alteraSalarioLoteRepository;

    public AlteraSalarioLote create(AlteraSalarioLoteDto alteraSalarioLoteDto) {
        alteraSalarioLoteDto.setId(null);
        AlteraSalarioLote centroDeCusto = new AlteraSalarioLote(alteraSalarioLoteDto);
        return alteraSalarioLoteRepository.save(centroDeCusto);
    }

    public void delete(Long id) {
        alteraSalarioLoteRepository.deleteById(id);
    }

    public List<AlteraSalarioLote> findAllByDataAlteracao(Date dataInicial, Date dataFinal){
        return alteraSalarioLoteRepository.findByDataAlteracao(dataInicial, dataFinal);
    }
}
