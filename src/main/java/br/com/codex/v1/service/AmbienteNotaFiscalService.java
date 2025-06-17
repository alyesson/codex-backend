package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.dto.AmbienteNotaFiscalDto;
import br.com.codex.v1.domain.enums.TipoAmbiente;
import br.com.codex.v1.domain.repository.AmbienteNotaFiscalRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AmbienteNotaFiscalService {

    @Autowired
    private AmbienteNotaFiscalRepository ambienteNotaFiscalRepository;

    @Transactional
    public AmbienteNotaFiscal update(Long id, AmbienteNotaFiscalDto dto) {

        if (dto == null || dto.getCodigoAmbiente() == null) {
            throw new IllegalArgumentException("Dados do ambiente não podem ser nulos");
        }

        AmbienteNotaFiscal ambienteExistente = ambienteNotaFiscalRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Ambiente não encontrado com o ID: " + id));

        // 3. Valida o código do ambiente (1-Produção ou 2-Homologação)
        TipoAmbiente novoAmbiente = TipoAmbiente.toEnum(dto.getCodigoAmbiente()); // Lança exceção se código for inválido

        // 5. Atualiza apenas o campo permitido (código do ambiente)
        ambienteExistente.setCodigoAmbiente(novoAmbiente.getCodigo());

        // 6. Salva e retorna
        return ambienteNotaFiscalRepository.save(ambienteExistente);
    }

    public AmbienteNotaFiscal findById(Long id) {
        Optional<AmbienteNotaFiscal> obj = ambienteNotaFiscalRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Ambiente não encontrado"));
    }
}
