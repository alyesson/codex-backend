package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Visitantes;
import br.com.codex.v1.domain.dto.VisitantesDto;
import br.com.codex.v1.domain.repository.VisitantesRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.text.SimpleDateFormat;

@Service
public class VisitantesService {

    @Autowired
    private VisitantesRepository visitantesRepository;

    public Visitantes create(VisitantesDto visitantesDto) {
        visitantesDto.setId(null);
        String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        visitantesDto.setDataCadastro(LocalDate.parse(dataAtual));

        //validaVisitantes(visitantesDto);
        Visitantes visitantes = new Visitantes(visitantesDto);
        return visitantesRepository.save(visitantes);
    }

    public Visitantes update(Integer id, VisitantesDto visitantesDto) {
        visitantesDto.setId(id);
        Visitantes obj = findById(id);
        obj = new Visitantes(visitantesDto);
        return visitantesRepository.save(obj);
    }

    public Visitantes findById(Integer id) {
        Optional<Visitantes> obj = visitantesRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Centro de custo não encontrado"));
    }

    public void delete(Integer id) {
        visitantesRepository.deleteById(id);
    }

    public List<Visitantes> findAll(){
        return visitantesRepository.findAll();
    }

    private void validaVisitantes(VisitantesDto visitantesDto){
        Optional<Visitantes> obj = visitantesRepository.findByCpf(visitantesDto.getCpf());
        if(obj.isPresent() && obj.get().getCpf().equals(visitantesDto.getCpf())){
            throw new DataIntegrityViolationException("Este CPF já existe");
        }
    }

    public List<Visitantes> findAllVisitantesPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return visitantesRepository.findAllVisitantesPeriodo(dataInicial, dataFinal);
    }
}
