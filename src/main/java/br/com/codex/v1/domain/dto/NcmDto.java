package br.com.codex.v1.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NcmDto {

    private String codigo;
    private String descricao;

    public NcmDto(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }
}
