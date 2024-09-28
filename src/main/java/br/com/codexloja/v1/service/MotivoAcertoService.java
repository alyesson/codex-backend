package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.estoque.MotivoAcerto;
import br.com.codexloja.v1.domain.dto.MotivoAcertoDto;
import br.com.codexloja.v1.domain.repository.MotivoAcertoRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotivoAcertoService {

    @Autowired
    private MotivoAcertoRepository motivoAcertoRepository;

    public MotivoAcerto create(MotivoAcertoDto motivoacertodto) {
        motivoacertodto.setId(null);
        validaMotivoAcerto(motivoacertodto);
        MotivoAcerto motivoacerto = new MotivoAcerto(motivoacertodto);
        return motivoAcertoRepository.save(motivoacerto);
    }

    public MotivoAcerto update(Integer id, MotivoAcertoDto motivoacertodto) {
        motivoacertodto.setId(id);
        MotivoAcerto obj = findById(id);
        obj = new MotivoAcerto(motivoacertodto);
        return motivoAcertoRepository.save(obj);
    }

    public MotivoAcerto findById(Integer id) {
        Optional<MotivoAcerto> obj = motivoAcertoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("MotivoAcerto não encontrado"));
    }

    public void delete(Integer id) {
        motivoAcertoRepository.deleteById(id);
    }

    public List<MotivoAcerto> findAll(){
        return motivoAcertoRepository.findAll();
    }

    private void validaMotivoAcerto(MotivoAcertoDto motivoacertodto){
        Optional<MotivoAcerto> obj = motivoAcertoRepository.findByCodigo(motivoacertodto.getCodigo());
        if(obj.isPresent() && obj.get().getCodigo().equals(motivoacertodto.getCodigo())){
            throw new DataIntegrityViolationException("Este código já existe");
        }
    }
}
