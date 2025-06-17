package br.com.codex.v1.service;


import br.com.codex.v1.domain.dto.CadastroJornadaDto;
import br.com.codex.v1.domain.repository.CadastroJornadaRepository;
import br.com.codex.v1.domain.rh.CadastroJornada;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroJornadaService {

    @Autowired
    CadastroJornadaRepository cadastroJornadaRepository;

        public CadastroJornada create(CadastroJornadaDto CadastroJornadaDto) {
            CadastroJornadaDto.setId(null);
            CadastroJornada objJornada = new CadastroJornada(CadastroJornadaDto);
            return cadastroJornadaRepository.save(objJornada);
        }

        public CadastroJornada update(Long id, CadastroJornadaDto CadastroJornadaDto) {
            CadastroJornadaDto.setId(id);
            CadastroJornada obj = findById(id);
            obj = new CadastroJornada(CadastroJornadaDto);
            return cadastroJornadaRepository.save(obj);
        }

        public CadastroJornada findById(Long id) {
            Optional<CadastroJornada> obj = cadastroJornadaRepository.findById(id);
            return obj.orElseThrow(() -> new ObjectNotFoundException("Jornada não encontrada"));
        }

        public void deleteByCodigoJornada(String codigoJornada) {
            cadastroJornadaRepository.deleteByCodigoJornada(codigoJornada);
        }

        public List<CadastroJornada> findAll(){
            return cadastroJornadaRepository.findAll();
        }

    public List<CadastroJornada> findAllByCodigoJornada(String codigoJornada) {
        return cadastroJornadaRepository.findByCodigoJornada(codigoJornada);
    }
}
