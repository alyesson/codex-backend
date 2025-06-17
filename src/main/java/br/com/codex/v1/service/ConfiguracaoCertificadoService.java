package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.dto.ConfiguracaoCertificadoDto;
import br.com.codex.v1.domain.repository.ConfiguracaoCertificadoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import br.com.codex.v1.utilitario.Base64Util;
import br.com.codex.v1.utilitario.CertificadoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.util.List;
import java.util.Optional;

@Service
public class ConfiguracaoCertificadoService {

    @Autowired
    private ConfiguracaoCertificadoRepository configuracaoCertificadoRepository;

    public ConfiguracaoCertificado findById(Long id){
        Optional<ConfiguracaoCertificado> certificado = configuracaoCertificadoRepository.findById(id);
        return certificado.orElseThrow(() -> new ObjectNotFoundException("Certificado não encontrado!"));
    }

    public List<ConfiguracaoCertificado> findAll(){
       return configuracaoCertificadoRepository.findAll();
    }

    public ConfiguracaoCertificado create(MultipartFile file, String senha) throws Exception {

        try{
            byte[] arquivoBytes = file.getBytes();
            CertificadoUtils.CertificadoInfo info = CertificadoUtils.extrairInfoCertificado(arquivoBytes, senha);

            ConfiguracaoCertificadoDto dto = new ConfiguracaoCertificadoDto();
            dto.setNome(file.getOriginalFilename());
            dto.setArquivo(arquivoBytes);
            dto.setRazaoSocial(info.getRazaoSocial());
            dto.setCnpj(info.getCnpj());
            dto.setDataValidade(new java.sql.Date(info.getDataValidade().getTime()));
            dto.setTipo("A1"); // Ou detecte automaticamente
            dto.setSenha(Base64Util.encode(senha)); // 🔒 Criptografa antes de salvar
            dto.setUf(info.getUf());

            ConfiguracaoCertificado certificado = new ConfiguracaoCertificado(dto);
            return configuracaoCertificadoRepository.save(certificado);

        }catch (Exception erro){
            throw new Exception("Erro ao processar certificado: "+ erro.getMessage());
        }
    }

    public void delete(Long id){
        configuracaoCertificadoRepository.deleteById(id);
    }
}
