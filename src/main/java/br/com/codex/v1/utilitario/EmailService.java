package br.com.codex.v1.utilitario;

import br.com.codex.v1.domain.dto.OrdemCompraDto;

public interface EmailService {
    String sendSimpleMail(OrdemCompraDto ordemCompraDto);
}
