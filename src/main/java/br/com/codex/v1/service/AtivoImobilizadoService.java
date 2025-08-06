package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.AtivoImobilizado;
import br.com.codex.v1.domain.dto.AtivoImobilizadoDto;
import br.com.codex.v1.domain.repository.AtivoImobilizadoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtivoImobilizadoService {

    @Autowired
    private AtivoImobilizadoRepository ativoImobilizadoRepository;

    public AtivoImobilizado create(AtivoImobilizadoDto ativoImobilizadoDto) {
        ativoImobilizadoDto.setId(null);
        validaAtivoImobilizado(ativoImobilizadoDto);
        AtivoImobilizado centroDeCusto = new AtivoImobilizado(ativoImobilizadoDto);
        return ativoImobilizadoRepository.save(centroDeCusto);
    }

    public AtivoImobilizado update(Long id, AtivoImobilizadoDto ativoImobilizadoDto) {
        ativoImobilizadoDto.setId(id);
        AtivoImobilizado obj = findById(id);
        obj = new AtivoImobilizado(ativoImobilizadoDto);
        return ativoImobilizadoRepository.save(obj);
    }

    public AtivoImobilizado findById(Long id) {
        Optional<AtivoImobilizado> obj = ativoImobilizadoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Ativo imobilizado não encontrado"));
    }

    public void delete(Long id) {
        ativoImobilizadoRepository.deleteById(id);
    }

    public List<AtivoImobilizado> findAll(){
        return ativoImobilizadoRepository.findAll();
    }

    private void validaAtivoImobilizado(AtivoImobilizadoDto ativoImobilizadoDto){
        Optional<AtivoImobilizado> obj = ativoImobilizadoRepository.findByCodigoBem(ativoImobilizadoDto.getCodigoBem());
        if(obj.isPresent() && obj.get().getCodigoBem().equals(ativoImobilizadoDto.getCodigoBem())){
            throw new DataIntegrityViolationException("Este ativo já foi cadastrado");
        }
    }
}
