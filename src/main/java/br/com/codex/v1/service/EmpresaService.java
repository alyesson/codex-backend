package br.com.codex.v1.service;

import br.com.codex.v1.configuration.DatabaseConfig;
import br.com.codex.v1.domain.cadastros.Empresa;
import br.com.codex.v1.domain.dto.EmpresaDto;
import br.com.codex.v1.domain.repository.EmpresaRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;


   @Autowired
   private DatabaseConfig databaseConfig;

    public Empresa create(EmpresaDto empresaDto) {
        empresaDto.setId(null);
        validaEmpresa(empresaDto);
        Empresa empresa = new Empresa(empresaDto);
        return empresaRepository.save(empresa);
    }

    public Empresa update(Long id, EmpresaDto empresaDto) {
        empresaDto.setId(id);
        Empresa obj = findById(id);
        obj = new Empresa(empresaDto);
        return empresaRepository.save(obj);
    }

    public Empresa findById(Long id) {
        Optional<Empresa> obj = empresaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Empresa não encontrada"));
    }

    public void delete(Long id) {
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

    public Empresa cadastrarNovaEmpresa(EmpresaDto empresaDto) {
        empresaDto.setId(null);
        validaEmpresa(empresaDto);

        // Substitui espaços por underscores e remove caracteres especiais
        String nomeBase = normalizeDatabaseName(empresaDto.getNomeFantasia());
        empresaDto.setJdbcUrl(nomeBase);

        Empresa empresa = new Empresa(empresaDto);
        empresa = empresaRepository.save(empresa);

        //Aqui o sistema verifica o perfil que está no "application.properties"
        databaseConfig.criaBaseDadosClienteFilial(nomeBase);
        return empresa;
    }

    private String normalizeDatabaseName(String nome) {
        // Remove acentos, substitui espaços por underscore e remove caracteres especiais
        String normalized = Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^a-zA-Z0-9]", "_")
                .toLowerCase();

        // Garante que o nome não comece com número
        if (normalized.matches("^[0-9].*")) {
            normalized = "db_" + normalized;
        }
        return normalized;
    }

    public List<Empresa> findAllByDataBase() {
        return empresaRepository.findAllByDataBase();
    }
}
