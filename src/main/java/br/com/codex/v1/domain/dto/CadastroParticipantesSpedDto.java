package br.com.codex.v1.domain.dto;

import lombok.Data;

@Data
public class CadastroParticipantesSpedDto {

    private String codPart;
    private String nome;
    private String cnpj;
    private String cpf;
    private String ie;
    private String codMunicipio;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
}
