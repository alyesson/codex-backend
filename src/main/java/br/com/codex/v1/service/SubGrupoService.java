package br.com.codex.v1.service;

import br.com.codex.v1.domain.estoque.SubGrupo;
import br.com.codex.v1.domain.dto.SubGrupoDto;
import br.com.codex.v1.domain.repository.SubGrupoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubGrupoService {

    @Autowired
    private SubGrupoRepository subgrupoRepository;

    public SubGrupo create(SubGrupoDto subgrupoDto) {
        subgrupoDto.setId(null);
        validaSubGrupo(subgrupoDto);
        SubGrupo grupo = new SubGrupo(subgrupoDto);
        return subgrupoRepository.save(grupo);
    }
    public SubGrupo update(Integer id, SubGrupoDto subgrupoDto) {
        subgrupoDto.setId(id);
        SubGrupo obj = findById(id);
        obj = new SubGrupo(subgrupoDto);
        return subgrupoRepository.save(obj);
    }

    public SubGrupo findById(Integer id) {
        Optional<SubGrupo> obj = subgrupoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Subgrupo não encontrado"));
    }

    public void delete(Integer id) {
        subgrupoRepository.deleteById(id);
    }

    public List<SubGrupo> findAll(){
        return subgrupoRepository.findAll();
    }

    private void validaSubGrupo(SubGrupoDto subgrupoDto){
        Optional<SubGrupo> obj = subgrupoRepository.findByCodigoSubGrupo(subgrupoDto.getCodigoSubGrupo());
        if(obj.isPresent() && obj.get().getCodigoSubGrupo().equals(subgrupoDto.getCodigoSubGrupo())){
            throw new DataIntegrityViolationException("Este subgrupo já existe");
        }
    }

    public List<SubGrupo> findByNomeGrupo(String codigoGrupo) {
        return subgrupoRepository.findByNomeGrupo(codigoGrupo);
    }
}
