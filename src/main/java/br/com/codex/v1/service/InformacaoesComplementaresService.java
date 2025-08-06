package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.InformacaoesComplementaresDto;
import br.com.codex.v1.domain.fiscal.InformacaoesComplementares;
import br.com.codex.v1.domain.repository.InformacaoesComplementaresRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InformacaoesComplementaresService {
    
    @Autowired
    private InformacaoesComplementaresRepository informacaoesComplementaresRepository;

    public InformacaoesComplementares create(InformacaoesComplementaresDto informacaoesComplementaresDto) {
        informacaoesComplementaresDto.setId(null);
        validaInformacaoesAdicionaisNfe(informacaoesComplementaresDto);
        InformacaoesComplementares centroDeCusto = new InformacaoesComplementares(informacaoesComplementaresDto);
        return informacaoesComplementaresRepository.save(centroDeCusto);
    }

    public InformacaoesComplementares update(Long id, InformacaoesComplementaresDto informacaoesComplementaresDto) {
        informacaoesComplementaresDto.setId(id);
        InformacaoesComplementares obj = findById(id);
        obj = new InformacaoesComplementares(informacaoesComplementaresDto);
        return informacaoesComplementaresRepository.save(obj);
    }

    public InformacaoesComplementares findById(Long id) {
        Optional<InformacaoesComplementares> obj = informacaoesComplementaresRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Informação complementar não encontrada"));
    }

    public void delete(Long id) {
        informacaoesComplementaresRepository.deleteById(id);
    }

    public List<InformacaoesComplementares> findAll(){
        return informacaoesComplementaresRepository.findAll();
    }

    private void validaInformacaoesAdicionaisNfe(InformacaoesComplementaresDto informacaoesComplementaresDto){
        Optional<InformacaoesComplementares> obj = informacaoesComplementaresRepository.findByCodigo(informacaoesComplementaresDto.getCodigo());
        if(obj.isPresent() && obj.get().getCodigo().equals(informacaoesComplementaresDto.getCodigo())){
            throw new DataIntegrityViolationException("Esta informação complementar já existe");
        }
    }
}
