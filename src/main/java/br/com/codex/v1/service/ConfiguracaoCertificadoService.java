package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.dto.ConfiguracaoCertificadoDto;
import br.com.codex.v1.domain.repository.ConfiguracaoCertificadoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfiguracaoCertificadoService {

    @Autowired
    private ConfiguracaoCertificadoRepository configuracaoCertificadoRepository;

    public ConfiguracaoCertificado findById(Integer id){
        Optional<ConfiguracaoCertificado> certificado = configuracaoCertificadoRepository.findById(id);
        return certificado.orElseThrow(() -> new ObjectNotFoundException("Certificado não encontrado!"));
    }

    public List<ConfiguracaoCertificado> findAll(){
       return configuracaoCertificadoRepository.findAll();
    }

    public ConfiguracaoCertificado create(ConfiguracaoCertificadoDto configuracaoCertificadoDto){
        configuracaoCertificadoDto.setId(null);
        ConfiguracaoCertificado certificado = new ConfiguracaoCertificado(configuracaoCertificadoDto);
        return configuracaoCertificadoRepository.save(certificado);
    }

    public void delete(Integer id){
        configuracaoCertificadoRepository.deleteById(id);
    }
}
