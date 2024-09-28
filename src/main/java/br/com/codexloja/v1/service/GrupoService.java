package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.estoque.Grupo;
import br.com.codexloja.v1.domain.dto.GrupoDto;
import br.com.codexloja.v1.domain.repository.GrupoRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;


    public Grupo create(GrupoDto grupoDto) {
        grupoDto.setId(null);
        validaGrupo(grupoDto);
        Grupo grupo = new Grupo(grupoDto);
        return grupoRepository.save(grupo);
    }

    public Grupo update(Integer id, GrupoDto grupoDto) {
        grupoDto.setId(id);
        Grupo obj = findById(id);
        obj = new Grupo(grupoDto);
        return grupoRepository.save(obj);
    }

    public Grupo findById(Integer id) {
        Optional<Grupo> obj = grupoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Grupo não encontrado"));
    }

    public void delete(Integer id) {
        grupoRepository.deleteById(id);
    }

    public List<Grupo> findAll(){
        return grupoRepository.findAll();
    }

    private void validaGrupo(GrupoDto grupoDto){
        Optional<Grupo> obj = grupoRepository.findByCodigo(grupoDto.getCodigo());
        if(obj.isPresent() && obj.get().getCodigo().equals(grupoDto.getCodigo())){
            throw new DataIntegrityViolationException("Este grupo já existe");
        }
    }
}
