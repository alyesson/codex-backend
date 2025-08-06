package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.InformacaoesAdicionaisFiscoDto;
import br.com.codex.v1.domain.fiscal.InformacaoesAdicionaisFisco;
import br.com.codex.v1.domain.repository.InformacaoesAdicionaisFiscoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InformacaoesAdicionaisFiscoService {
    
    @Autowired
    private InformacaoesAdicionaisFiscoRepository informacaoesAdicionaisFiscoRepository;

    public InformacaoesAdicionaisFisco create(InformacaoesAdicionaisFiscoDto informacaoesAdicionaisFiscoDto) {
        informacaoesAdicionaisFiscoDto.setId(null);
        validaInformacaoesAdicionaisNfe(informacaoesAdicionaisFiscoDto);
        InformacaoesAdicionaisFisco centroDeCusto = new InformacaoesAdicionaisFisco(informacaoesAdicionaisFiscoDto);
        return informacaoesAdicionaisFiscoRepository.save(centroDeCusto);
    }

    public InformacaoesAdicionaisFisco update(Long id, InformacaoesAdicionaisFiscoDto informacaoesAdicionaisFiscoDto) {
        informacaoesAdicionaisFiscoDto.setId(id);
        InformacaoesAdicionaisFisco obj = findById(id);
        obj = new InformacaoesAdicionaisFisco(informacaoesAdicionaisFiscoDto);
        return informacaoesAdicionaisFiscoRepository.save(obj);
    }

    public InformacaoesAdicionaisFisco findById(Long id) {
        Optional<InformacaoesAdicionaisFisco> obj = informacaoesAdicionaisFiscoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Informação adicional não encontrada"));
    }

    public void delete(Long id) {
        informacaoesAdicionaisFiscoRepository.deleteById(id);
    }

    public List<InformacaoesAdicionaisFisco> findAll(){
        return informacaoesAdicionaisFiscoRepository.findAll();
    }

    private void validaInformacaoesAdicionaisNfe(InformacaoesAdicionaisFiscoDto informacaoesAdicionaisFiscoDto){
        Optional<InformacaoesAdicionaisFisco> obj = informacaoesAdicionaisFiscoRepository.findByCodigo(informacaoesAdicionaisFiscoDto.getCodigo());
        if(obj.isPresent() && obj.get().getCodigo().equals(informacaoesAdicionaisFiscoDto.getCodigo())){
            throw new DataIntegrityViolationException("Esta informação adicional já existe");
        }
    }
}
