package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.dto.EmpresaDto;
import br.com.codex.v1.domain.repository.EmpresaRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Empresa create(EmpresaDto empresaDto) {
        empresaDto.setId(null);
        validaEmpresa(empresaDto);
        Empresa empresa = new Empresa(empresaDto);
        return empresaRepository.save(empresa);
    }

    public Empresa update(Integer id, EmpresaDto empresaDto) {
        empresaDto.setId(id);
        Empresa obj = findById(id);
        obj = new Empresa(empresaDto);
        return empresaRepository.save(obj);
    }

    public Empresa findById(Integer id) {
        Optional<Empresa> obj = empresaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Empresa não encontrada"));
    }

    public void delete(Integer id) {
        empresaRepository.deleteById(id);
    }

    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    private void validaEmpresa(EmpresaDto empresaDto){
        Optional<Empresa> objEmpresa = empresaRepository.findByCnpj(empresaDto.getCnpj());
        if(objEmpresa.isPresent() && objEmpresa.get().getCnpj().equals(empresaDto.getCnpj())){
            throw new DataIntegrityViolationException("Já existe uma empresa cadastrada com o cnpj "+empresaDto.getCnpj());
        }
    }

    public Empresa cadastrarNovaEmpresa(Empresa empresa) {
        // Exemplo: nome do banco "empresa_123"
        String nomeBase = empresa.getNomeFantasia();
        empresa.setNomeFantasia(nomeBase);
        empresaRepository.save(empresa);

        criarBaseDeDados(nomeBase);
        return empresa;
    }

    public void criarBaseDeDados(String nomeBanco) {
        jdbcTemplate.execute("CREATE DATABASE " + nomeBanco);
    }
}
