package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.TabelaDeducaoInssDto;
import br.com.codex.v1.domain.repository.TabelaDeducaoInssRepository;
import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TabelaDeducaoInssService {

    @Autowired
    private TabelaDeducaoInssRepository tabelaDeducaoInssRepository;

    public TabelaDeducaoInss create(TabelaDeducaoInssDto tabelaDeducaoInssDto) {
        tabelaDeducaoInssDto.setId(null);
        TabelaDeducaoInss centroDeCusto = new TabelaDeducaoInss(tabelaDeducaoInssDto);
        return tabelaDeducaoInssRepository.save(centroDeCusto);
    }

    public TabelaDeducaoInss update(Long id, TabelaDeducaoInssDto tabelaDeducaoInssDto) {
        tabelaDeducaoInssDto.setId(id);
        TabelaDeducaoInss obj = findById(id);
        obj = new TabelaDeducaoInss(tabelaDeducaoInssDto);
        return tabelaDeducaoInssRepository.save(obj);
    }

    public TabelaDeducaoInss findById(Long id) {
        Optional<TabelaDeducaoInss> obj = tabelaDeducaoInssRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Valor de incidência do inss não encontrado"));
    }

    public void delete(Long id) {
        tabelaDeducaoInssRepository.deleteById(id);
    }

    public List<TabelaDeducaoInss> findAll(){
        return tabelaDeducaoInssRepository.findAll();
    }
}
