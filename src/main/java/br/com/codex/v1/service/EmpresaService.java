package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.dto.EmpresaDto;
import br.com.codex.v1.domain.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public Empresa create(EmpresaDto empresaDto) {
        empresaDto.setId(null);
        validaEmpresa(empresaDto);
        Empresa empresa = new Empresa(empresaDto);
        return empresaRepository.save(empresa);
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    private void validaEmpresa(EmpresaDto empresaDto){
        Optional<Empresa> objEmpresa = empresaRepository.findByCnpj(empresaDto.getCnpj());
        if(objEmpresa.isPresent() && objEmpresa.get().getCnpj().equals(empresaDto.getCnpj())){
            throw new DataIntegrityViolationException("Já existe uma empresa cadastrada com o cnpj "+empresaDto.getCnpj());
        }
    }
}
