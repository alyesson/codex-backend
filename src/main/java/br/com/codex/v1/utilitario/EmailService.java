package br.com.codex.v1.utilitario;

import br.com.codex.v1.domain.dto.SolicitacaoCompraDto;

public interface EmailService {
    String sendSimpleMail(SolicitacaoCompraDto solicitacaoCompraDto);
}
