package br.com.codex.v1.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GerarSpedRequestDto {

    private EmpresaDto empresa; // Dados da empresa (CNPJ, nome, endereço, etc.)
    private List<NotaFiscalDto> notasFiscais; // Notas do período
    private LocalDate dataInicio; // Período do SPED
    private LocalDate dataFim;
    private String periodo;
    private String finalidadeArquivo;
    private String perfil;
    private String atividade;
    private String apropriacaoCredito;
    private String indicadorMovimento;
    private String descricaoProcesso;
    private String descricaoComplementarObrigacoes;
    private String nomeContador;
    private String cpfContador;
    private String crcContador;
    private String logradouroContador;
    private String numeroContador;
    private String complementoContador;
    private String bairroContador;
    private String cidadeContador;
    private String cepContador;
    private String telefoneContador;
    private String emailContador;
    private String codigoMunicipioContador;
}
