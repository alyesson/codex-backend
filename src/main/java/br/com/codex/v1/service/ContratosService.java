package br.com.codex.v1.service;

import br.com.codex.v1.domain.compras.Contratos;
import br.com.codex.v1.domain.dto.ContratosDto;
import br.com.codex.v1.domain.repository.ContratosRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContratosService {

    @Autowired
    private ContratosRepository contratosRepository;

    public Contratos findById(Integer id) {
        Optional<Contratos> obj = contratosRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Contrato não encontrado!"));
    }

    public List<Contratos> findAll() {
        return contratosRepository.findAllByOrderByIdDesc();
    }

    public Contratos create(ContratosDto contratosDto) {
        contratosDto.setId(null);
        Contratos objContratos = new Contratos(contratosDto);
        return contratosRepository.save(objContratos);
    }

    public Contratos update(ContratosDto contratoDto) {
        contratoDto.setId(contratoDto.getId());
        Contratos objContrato = findById(contratoDto.getId());
        objContrato = new Contratos(contratoDto);
        return contratosRepository.save(objContrato);
    }

    public void delete(Integer id) {
        findById(id);
        contratosRepository.deleteById(id);
    }
}
