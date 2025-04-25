package br.com.codex.v1.utilitario;

import br.com.codex.v1.domain.dto.AtendimentosDto;

public interface EmailAtendimentoService {
    String sendSimpleMail(AtendimentosDto atendimentosDto);
}
