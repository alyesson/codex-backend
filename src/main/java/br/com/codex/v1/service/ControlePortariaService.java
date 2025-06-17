package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.ControlePortaria;
import br.com.codex.v1.domain.dto.ControlePortariaDto;
import br.com.codex.v1.domain.repository.ControlePortariaRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ControlePortariaService {

    @Autowired
    private ControlePortariaRepository controlePortariaRepository;

    public ControlePortaria create(ControlePortariaDto controlePortariaDto) {

        LocalDate dataAtual = LocalDate.now();
        LocalDateTime horAtual = LocalDateTime.now();
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
        String horaFormatada = horAtual.format(formatterHora);

        controlePortariaDto.setDataEntrada(dataAtual);
        controlePortariaDto.setHoraEntrada(horaFormatada);
        controlePortariaDto.setId(null);
        ControlePortaria centroDeCusto = new ControlePortaria(controlePortariaDto);
        return controlePortariaRepository.save(centroDeCusto);
    }
    public ControlePortaria update(Long id, ControlePortariaDto controlePortariaDto) {
        controlePortariaDto.setId(id);
        ControlePortaria obj = findById(id);
        obj = new ControlePortaria(controlePortariaDto);
        return controlePortariaRepository.save(obj);
    }

    public ControlePortaria findById(Long id) {
        Optional<ControlePortaria> obj = controlePortariaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Visitante não encontrado"));
    }

    public void delete(Long id) {
        controlePortariaRepository.deleteById(id);
    }

    public List<ControlePortaria> findAll(){
        return controlePortariaRepository.findAll();
    }

    public List<ControlePortaria> findAllControlePeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return controlePortariaRepository.findAllControlePeriodo(dataInicial, dataFinal);
    }
}
