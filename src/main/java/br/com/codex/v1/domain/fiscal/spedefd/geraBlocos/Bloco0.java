package br.com.codex.v1.domain.fiscal.spedefd.geraBlocos;

import br.com.codex.v1.domain.dto.EmpresaDto;
import br.com.codex.v1.domain.dto.GerarSpedRequestDto;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Alyesson Sousa
 *
 */
public class Bloco0 {

    static SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");

    public static Bloco0 preencheBloco0(GerarSpedRequestDto gerarSpedRequestDto, List<NotaFiscalDto> notasFiscais) {
        Bloco0 bloco0 = new Bloco0();
        bloco0 = preencheRegistro0000(bloco0, gerarSpedRequestDto);
        bloco0 = preencheRegistro0001(bloco0);
        bloco0 = preencheRegistro0005(bloco0, gerarSpedRequestDto);
        bloco0 = preencheRegistro0015(bloco0);
        bloco0 = preencheRegistro0100(bloco0);
        bloco0 = preencheRegistro0150(bloco0);
        bloco0 = preencheRegistro0190(bloco0);
        bloco0 = preencheRegistro0200(bloco0);
        bloco0 = preencheRegistro0210(bloco0);
        bloco0 = preencheRegistro0220(bloco0);
        bloco0 = preencheRegistro0221(bloco0);
        bloco0 = preencheRegistro0300(bloco0);
        bloco0 = preencheRegistro0400(bloco0);
        bloco0 = preencheRegistro0450(bloco0);
        bloco0 = preencheRegistro0460(bloco0);
        bloco0 = preencheRegistro0500(bloco0);
        bloco0 = preencheRegistro0600(bloco0);
        bloco0 = preencheRegistro0900(bloco0);

        return bloco0;
    }

    //Abertura do Arquivo Digital e Identificação da entidade
    public static Bloco0 preencheRegistro0000(Bloco0 bloco0, GerarSpedRequestDto gerarSpedRequestDto) {

        Registro0000 registro0000 = new Registro0000();
        registro0000.setCod_fin(gerarSpedRequestDto.getFinalidadeArquivo());
        registro0000.setDt_ini(String.valueOf(gerarSpedRequestDto.getDataInicio()));
        registro0000.setDt_fin(String.valueOf(gerarSpedRequestDto.getDataFim()));
        registro0000.setNome(gerarSpedRequestDto.getEmpresa().getNomeFantasia());
        registro0000.setCnpj(gerarSpedRequestDto.getEmpresa().getCnpj());
        registro0000.setUf(gerarSpedRequestDto.getEmpresa().getUf());
        registro0000.setIe(gerarSpedRequestDto.getEmpresa().getInscricaoEstadual());
        registro0000.setCod_mun(gerarSpedRequestDto.getEmpresa().getCodigoCidade());
        registro0000.setSuframa(gerarSpedRequestDto.getEmpresa().getSuframa());
        registro0000.setInd_perfil(gerarSpedRequestDto.getPerfil());
        registro0000.setInd_ativ(gerarSpedRequestDto.getAtividade());

        bloco0.setRegistro0000(registro0000);

        return bloco0;
    }

    //Abertura do Bloco 0
    public static Bloco0 preencheRegistro0001(Bloco0 bloco0, GerarSpedRequestDto gerarSpedRequestDto) {
        Registro0001 registro0001 = new Registro0001();
        registro0001.setInd_mov(gerarSpedRequestDto.getIndicadorMovimento());

        bloco0.setRegistro0001(registro0001);

        return bloco0;
    }

    //Classificação do Estabelecimento Industrial ou Equiparado a Industria
    public static Bloco0 preencheRegistro0002(Bloco0 bloco0, GerarSpedRequestDto gerarSpedRequestDto) {
        Registro0002 registro0002 = new Registro0002();
        registro0002.setClass_estab_ind(gerarSpedRequestDto.getAtividade());

        bloco0.setRegistro0002(registro0002);

        return bloco0;
    }

    //Dados Complementares da entidade 
    public static Bloco0 preencheRegistro0005(Bloco0 bloco0, GerarSpedRequestDto gerarSpedRequestDto) {
        Registro0005 registro0005 = new Registro0005();
        registro0005.setFantasia(gerarSpedRequestDto.getEmpresa().getNomeFantasia());
        registro0005.setCep(gerarSpedRequestDto.getEmpresa().getCep());
        registro0005.setEnd(gerarSpedRequestDto.getEmpresa().getEndereco());
        registro0005.setNum(gerarSpedRequestDto.getEmpresa().getNumero());
        registro0005.setCompl(gerarSpedRequestDto.getEmpresa().getComplemento());
        registro0005.setBairro(gerarSpedRequestDto.getEmpresa().getBairro());
        registro0005.setFone(gerarSpedRequestDto.getEmpresa().getTelefone());
        registro0005.setFax(null);
        registro0005.setEmail(gerarSpedRequestDto.getEmpresa().getEmailContato());

        bloco0.setRegistro0005(registro0005);

        return bloco0;
    }

    //Dados do Contribuinte Substituto ou Responsável pelo ICMS Destino
    public static Bloco0 preencheRegistro0015(Bloco0 bloco0) {
        Registro0015 registro0015 = new Registro0015();
        registro0015.setUf_st(ufContibuinteSubstituto.getText());
        registro0015.setIe_st(ieContibuinteSubstituto.getText());

        bloco0.getRegistro0015().add(registro0015);

        return bloco0;
    }

    //Dados do Contabilista
    public static Bloco0 preencheRegistro0100(Bloco0 bloco0, GerarSpedRequestDto gerarSpedRequestDto) {
        Registro0100 registro0100 = new Registro0100();
        registro0100.setNome(gerarSpedRequestDto.getNomeContador());
        registro0100.setCpf(gerarSpedRequestDto.getCpfContador());
        registro0100.setCrc(gerarSpedRequestDto.getCrcContador());
        registro0100.setCnpj(null);
        registro0100.setCep(gerarSpedRequestDto.getCepContador());
        registro0100.setEnd(gerarSpedRequestDto.getLogradouroContador()));
        registro0100.setNum(gerarSpedRequestDto.getNumeroContador());
        registro0100.setBairro(gerarSpedRequestDto.getBairroContador());
        registro0100.setFone(gerarSpedRequestDto.getTelefoneContador());
        registro0100.setFax(null);
        registro0100.setEmail(gerarSpedRequestDto.getEmailContador());
        registro0100.setCod_mun(gerarSpedRequestDto.getCodigoMunicipioContador());

        bloco0.setRegistro0100(registro0100);

        return bloco0;
    }

    //Tabela de Cadastro do Participante
    public static Bloco0 preencheRegistro0150(Bloco0 bloco0) {

        String cpfCnpj, alteracao;
        String valorCpf = null;
        String valorCnpj = null;
        int valor;
        Date dataDaAlteracao;

        ConexaoBD conecta0150 = new ConexaoBD();
        conecta0150.conecta();

        try {
            ResultSet rs0150;
            try ( PreparedStatement ps0150 = conecta0150.conexao.prepareStatement("Select codEmpresa,nomeFantasia,codPais,cpfCnpj,iEstadual,codMunicipio,suframa,nomeLogradouro,numero,complemento,bairro,alterado,dataAlteracao,numeroCampo,conteudoAnterior from con_for01 where tipoEmpresa=? and tipoEmpresa=?")) {
                ps0150.setString(1, "Fornecedor");
                ps0150.setString(2, "Cliente");
                rs0150 = ps0150.executeQuery();

                while (rs0150.next()) {

                    cpfCnpj = rs0150.getString("cpfCnpj").replace(".", "").replace("/", "").replace("-", "");
                    valor = StringUtils.length(cpfCnpj);//aqui verifica o tamanho da String, ou seja, quantos caracteres ele tem.

                    if (valor == 14) {
                        valorCpf = "";
                        valorCnpj = cpfCnpj;
                    }
                    if (valor == 11) {
                        valorCpf = cpfCnpj;
                        valorCnpj = "";
                    }

                    Registro0150 registro0150 = new Registro0150();
                    registro0150.setCod_part(rs0150.getString("codEmpresa"));
                    registro0150.setNome(rs0150.getString("nomeFantasia"));
                    registro0150.setCod_pais(rs0150.getString("codPais"));
                    registro0150.setCnpj(valorCnpj);
                    registro0150.setCpf(valorCpf);
                    registro0150.setIe(rs0150.getString("iEstadual"));
                    registro0150.setCod_mun(rs0150.getString("codMunicipio"));
                    registro0150.setSuframa(rs0150.getString("suframa"));
                    registro0150.setEnd(rs0150.getString("nomeLogradouro"));
                    registro0150.setNum(rs0150.getString("numero"));
                    registro0150.setCompl(rs0150.getString("complemento"));
                    registro0150.setBairro(rs0150.getString("bairro"));

                    alteracao = rs0150.getString("alterado");
                    dataDaAlteracao = rs0150.getDate("dataAlteracao");

                    Date dataI = sdf.parse(dataDe.getDate().toString());
                    Date dataF = sdf.parse(dataAte.getDate().toString());

                    //Aqui verifica se a data recebida está entre o intervalor de emissão do EFD
                    ValidaIntervaloDatas verifica = new ValidaIntervaloDatas(dataI, dataF);

                    if (alteracao.equals("Sim") && verifica.isWithinRange(dataDaAlteracao)) {

                        //Alteração da Tabela de Cadastro de Participante
                        Registro0175 registro0175 = new Registro0175();
                        registro0175.setDt_alt(rs0150.getString("dataAlteracao"));
                        registro0175.setNr_campo(rs0150.getString("numeroCampo"));
                        registro0175.setCont_ant(rs0150.getString("conteudoAnterior"));
                        registro0150.getRegistro0175().add(registro0175);
                    }

                    bloco0.getRegistro0150().add(registro0150);
                }
                rs0150.close();
            }
        } catch (SQLException | ParseException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0150 ou bloco 0175 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conecta0150.desconecta();

        return bloco0;
    }

    //Identificação das unidades de medida
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0190(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0) {

        ConexaoBD conecta0190 = new ConexaoBD();
        conecta0190.conecta();

        try {
            ResultSet rs0190;
            try ( PreparedStatement ps0190 = conecta0190.conexao.prepareStatement("Select codigo, descricao from cmt_cdum01")) {
                rs0190 = ps0190.executeQuery();

                while (rs0190.next()) {

                    Registro0190 registro0190 = new Registro0190();
                    registro0190.setUnid(rs0190.getString("codigo"));
                    registro0190.setDescr(rs0190.getString("descricao"));

                    bloco0.getRegistro0190().add(registro0190);
                }
                rs0190.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0190 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conecta0190.desconecta();

        return bloco0;
    }

    //Tabela de Identificação do Item (Produtos e Serviços)
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0200(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0) {

        ConexaoBD conecta0200 = new ConexaoBD();
        conecta0200.conecta();
        try {
            ResultSet rs0202;
            try ( PreparedStatement ps0200 = conecta0200.conexao.prepareStatement("Select codigoProduto,nomeProduto,ean,medida,categoria,codNcm,exTipi,generoItem,porcentIcms,codCest,alterado,descricaoAnterior,dataInicialUtilizacao,dataFinalUtilizacao,codigoAnterior from cmt_cmat01")) {
                rs0202 = ps0200.executeQuery();

                while (rs0202.next()) {

                    Registro0200 registro0200 = new Registro0200();
                    registro0200.setCod_item(rs0202.getString("codigoProduto"));
                    registro0200.setDescr_item(rs0202.getString("nomeProduto"));
                    registro0200.setCod_barra(rs0202.getString("ean"));
                    registro0200.setCod_ant_item(rs0202.getString(""));
                    registro0200.setUnid_inv(rs0202.getString("medida"));
                    registro0200.setTipo_item(rs0202.getString("categoria").substring(0, 2));
                    registro0200.setCod_ncm(rs0202.getString("codNcm"));
                    registro0200.setEx_ipi(rs0202.getString("exTipi"));
                    registro0200.setCod_gen(rs0202.getString("generoItem").substring(0, 2));
                    registro0200.setCod_lst("");
                    registro0200.setAliq_icms(rs0202.getString("porcentIcms"));
                    registro0200.setCest(rs0202.getString("codCest"));

                    if (rs0202.getString("alterado").equals("Sim")) {

                        Registro0205 registro0205 = new Registro0205();
                        registro0205.setDesc_ant_item(rs0202.getString("descricaoAnterior"));
                        registro0205.setDt_ini(rs0202.getString("dataInicialUtilizacao"));
                        registro0205.setDt_fim(rs0202.getString("dataFinalUtilizacao"));
                        registro0205.setCod_ant_item(rs0202.getString("codigoAnterior"));
                        registro0200.getRegistro0205().add(registro0205);
                    }

                    if (rs0202.getString("codCest").replace(".", "").startsWith("060")) {

                        Registro0206 registro0206 = new Registro0206();
                        registro0206.setCod_comb(rs0202.getString("codigoProduto"));
                        registro0200.setRegistro0206(registro0206);
                    }

                    bloco0.getRegistro0200().add(registro0200);
                }
                rs0202.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0200 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conecta0200.desconecta();

        return bloco0;
    }

    //Consumo Específico Padronizado
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0210(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0) {

        ConexaoBD conecta0210 = new ConexaoBD();
        conecta0210.conecta();
        try {
            ResultSet rs0210, rs0210a;
            try ( PreparedStatement ps0210 = conecta0210.conexao.prepareStatement("Select codigoProduto,categoria from cmt_cmat01 where categoria=? || categoria=?")) {
                ps0210.setString(1, "03 - Produto em Processo");
                ps0210.setString(2, "04 - Produto Acabado");
                rs0210 = ps0210.executeQuery();

                while (rs0210.next()) {

                    String categoria = rs0210.getString("categoria").substring(0, 2);
                    String produto = rs0210.getString("codigoProduto");

                    if (categoria.equals("03") || categoria.equals("04")) {

                        ConexaoBD conecta0210a = new ConexaoBD();
                        try {
                            try ( PreparedStatement ps0210a = conecta0210a.conexao.prepareStatement("Select codigoItem,quantdade from man_cafm02 where codigoItem=?")) {
                                ps0210a.setString(1, produto);
                                rs0210a = ps0210a.executeQuery();

                                Registro0210 registro0210 = new Registro0210();
                                registro0210.setCod_item_comp(produto);
                                registro0210.setQtd_comp(rs0210a.getString("quantdade"));
                                registro0210.setPerda("");

                                bloco0.getRegistro0210().add(registro0210);
                            }
                        } catch (SQLException error) {
                            tarefaGeraSped.setOpaque(true);
                            tarefaGeraSped.setText("Erro ao ler informações do produto e na subcategoria do bloco 0210 - Bloco 0: " + error);
                            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
                        }
                    }
                }
                rs0210.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0210 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }

        conecta0210.desconecta();

        return bloco0;
    }

    //Fatores de Conversão de Unidade
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0220(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0) {

        ConexaoBD conecta0220 = new ConexaoBD();
        conecta0220.conecta();
        try {
            ResultSet rs0220;
            try ( PreparedStatement ps0220 = conecta0220.conexao.prepareStatement("Select ean,medida,fatorConversao from cmt_cmat01")) {
                rs0220 = ps0220.executeQuery();

                while (rs0220.next()) {

                    Registro0220 registro0220 = new Registro0220();
                    registro0220.setUnid_conv(rs0220.getString("medida"));
                    registro0220.setFat_conv(rs0220.getString("fatorConversao"));
                    registro0220.setCod_barra(rs0220.getString("ean"));

                    bloco0.getRegistro0220().add(registro0220);
                }
                rs0220.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0220 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conecta0220.desconecta();

        return bloco0;
    }

    //Correlação entre códigos de itens comercializados
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0221(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0) {

        ConexaoBD conecta0221 = new ConexaoBD();
        conecta0221.conecta();
        try {
            ResultSet rs0221;
            try ( PreparedStatement ps0221 = conecta0221.conexao.prepareStatement("Select codigoProduto,categoria,fatorConversao from cmt_cmat01 where categoria = ?")) {
                ps0221.setString(1, "00 - Mercadoria para Revenda");
                rs0221 = ps0221.executeQuery();

                while (rs0221.next()) {

                    Registro0221 registro0221 = new Registro0221();
                    registro0221.setCod_item_atomico(rs0221.getString("codigoProduto"));
                    registro0221.setCod_item_atomico(rs0221.getString("fatorConversao"));

                    bloco0.getRegistro0221().add(registro0221);
                }
                rs0221.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0221 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }

        conecta0221.desconecta();

        return bloco0;
    }

    //Cadastro de bens ou componentes do Ativo Imobilizado
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0300(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0) {

        ConexaoBD conecta0300 = new ConexaoBD();
        conecta0300.conecta();

        try {
            ResultSet rs0300;
            try(PreparedStatement ps0300 = conecta0300.conexao.prepareStatement("Select numeroPatrimonio,descricaoDoBem,cCustoBem,grupoDoBens,codigoConta, timestampdiff(month, dataAqusicaoBem,fimGarantiaBem) as 'vidaUtil' from con_cpat01")) {
                rs0300 = ps0300.executeQuery();
                
                while(rs0300.next()){
                    
                    Registro0300 registro0300 = new Registro0300();
                    registro0300.setCod_ind_bem(rs0300.getString("numeroPatrimonio"));
                    registro0300.setIdent_merc(rs0300.getString("grupoDoBens").substring(0, 2));
                    registro0300.setDescr_item(rs0300.getString("descricaoDoBem"));
                    registro0300.setDescr_item(rs0300.getString(""));
                    registro0300.setCod_cta(rs0300.getString("codigoConta"));
                    registro0300.setNr_parc("1");
                    
                    if(!rs0300.getString("grupoDoBens").equals("99")){
                        Registro0305 registro0305 = new Registro0305();
                        registro0305.setCod_ccus(rs0300.getString("cCustoBem"));
                        registro0305.setFunc(rs0300.getString("descricaoDoBem"));
                        registro0305.setVida_util(rs0300.getString("vidaUtil"));
                        registro0300.setRegistro0305(registro0305);
                    }
                    
                    bloco0.getRegistro0300().add(registro0300);
                }
                rs0300.close();
            }
        } catch (SQLException erro) {
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0300 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conecta0300.desconecta();

        return bloco0;
    }
    
    //Tabela de Natureza da Operação/ Prestação
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0400(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0){
        
        ConexaoBD conecta0400 = new ConexaoBD();
        conecta0400.conecta();
        try{
            ResultSet rs0400;
            try(PreparedStatement ps0400 = conecta0400.conexao.prepareStatement("Select codigo, descricao from fat_finf01")){
                rs0400 = ps0400.executeQuery();
                
                while(rs0400.next()){
                    Registro0400 registro0400 = new Registro0400();
                    registro0400.setCod_nat(rs0400.getString("codigo"));
                    registro0400.setDescr_nat(rs0400.getString("descricao"));
                    
                    bloco0.getRegistro0400().add(registro0400);
                }
                rs0400.close();
            }
        }catch(SQLException erro){
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0400 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conecta0400.desconecta();
        return bloco0;
    }

    //Tabela de Informação Complementar do documento fiscal
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0450(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0){
                    
        ConexaoBD conecta0450 = new ConexaoBD();
        conecta0450.conecta();
            try{
                ResultSet rs0450;
                
                Date dataIni = sdf.parse(dataDe.getDate().toString());
                Date dataFin = sdf.parse(dataAte.getDate().toString());
                
                try(PreparedStatement ps0450 = conecta0450.conexao.prepareStatement("Select infoAdicional from fat_emnf01 where dataEmissao between ? and ?")){
                    
                    ps0450.setDate(1,java.sql.Date.valueOf(dataIni.toString()));
                    ps0450.setDate(2,java.sql.Date.valueOf(dataFin.toString()));
                    rs0450 = ps0450.executeQuery();
                    
                    while(rs0450.next()){
                        Registro0450 registro0450 = new Registro0450();
                        registro0450.setCod_inf("1");
                        registro0450.setTxt(rs0450.getString("infoAdicional"));
                        bloco0.getRegistro0450().add(registro0450);
                    }
                    rs0450.close();
                }
            }catch(SQLException |ParseException erro){
                tarefaGeraSped.setOpaque(true);
                tarefaGeraSped.setText("Erro ao ler informações do bloco 0450 - Bloco 0: " + erro);
                tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
            }
        conecta0450.desconecta();
        
        return bloco0;
    }
    
    //Tabela de Observações do Lançamento Fiscal
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0460(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0){
        
        ConexaoBD conecta0460 = new ConexaoBD();
        conecta0460.conecta();
        
        try{
            ResultSet rs0460;
            try(PreparedStatement ps0460 = conecta0460.conexao.prepareStatement("Select codigo, descricao from con_rglf01")){
                rs0460 = ps0460.executeQuery();
                
                while(rs0460.next()){
                    Registro0460 registro0460 = new Registro0460();
                    registro0460.setCod_obs(rs0460.getString("codigo"));
                    registro0460.setTxt(rs0460.getString("descricao"));
                    
                    bloco0.getRegistro0460().add(registro0460);
                }
                rs0460.close();
            }
        }catch(SQLException erro){
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0460 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conecta0460.desconecta();
        return bloco0;
    }
    
    //Plano de contas contábeis
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0500(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0){

        ConexaoBD conecta0500 = new ConexaoBD();
        conecta0500.conecta();
        
        try{
            ResultSet rs0500;
            try(PreparedStatement ps0500 = conecta0500.conexao.prepareStatement("Select dataInclusao,naturezaPlanConta,tipo,codigoReduzido,codigoExpandido,nomePlanoConta from con_pcc01")){
                rs0500 = ps0500.executeQuery();
                
                while(rs0500.next()){
                    Registro0500 registro0500 = new Registro0500();
                    registro0500.setDt_alt(df.format(rs0500.getDate("dataInclusao")));
                    registro0500.setCod_nat_cc(rs0500.getString("naturezaPlanConta").substring(0, 2));
                    registro0500.setInd_cta(rs0500.getString("tipo").substring(0,1));
                    registro0500.setNivel(rs0500.getString("codigoReduzido"));
                    registro0500.setCod_cta(rs0500.getString("codigoExpandido"));
                    registro0500.setNome_cta(rs0500.getString("nomePlanoConta"));
                    
                    bloco0.getRegistro0500().add(registro0500);
                }
                rs0500.close();
            }
        }catch(SQLException erro){
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0500 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conecta0500.desconecta();
        
        return bloco0;
    }
    
    //Centro de custos
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0600(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0){

        ConexaoBD conecta0600 = new ConexaoBD();
        conecta0600.conecta();
        
        try{
            ResultSet rs0600;
            try(PreparedStatement ps0600 = conecta0600.conexao.prepareStatement("Select centroCusto,classe,descricao,codCCustoSinte,ccSintetico,dataAtivacao from con_ccto01")){
                rs0600 = ps0600.executeQuery();
                
                while(rs0600.next()){
                    
                    if(rs0600.getString("classe").equals("Analítico")){
                        Registro0600 registro0600 = new Registro0600();
                        registro0600.setDt_alt(df.format(rs0600.getDate("dataAtivacao")));
                        registro0600.setCod_ccus(rs0600.getString("centroCusto"));
                        registro0600.setCod_ccus(rs0600.getString("descricao"));
                        bloco0.getRegistro0600().add(registro0600);
                    }
                    
                    if(rs0600.getString("classe").equals("Sintético")){
                        Registro0600 registro0600 = new Registro0600();
                        registro0600.setDt_alt(df.format(rs0600.getDate("dataAtivacao")));
                        registro0600.setCod_ccus(rs0600.getString("codCCustoSinte"));
                        registro0600.setCod_ccus(rs0600.getString("ccSintetico"));
                        bloco0.getRegistro0600().add(registro0600);
                    }
                }
                rs0600.close();
            }
        }catch(SQLException erro){
            tarefaGeraSped.setOpaque(true);
            tarefaGeraSped.setText("Erro ao ler informações do bloco 0600 - Bloco 0: " + erro);
            tarefaGeraSped.setBackground(new java.awt.Color(255, 91, 91));
        }
        conecta0600.desconecta();
        
        return bloco0;
    }
    
    //Encerramento do Bloco 0
    public static br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 preencheRegistro0900(br.com.codex.v1.domain.fiscal.spedefd.registros.bloco0.Bloco0 bloco0){
    
        Registro0990 registro0990 = new Registro0990();
        registro0990.setQtd_lin_0("");
        
        bloco0.setRegistro0990(registro0990);
        
        return bloco0;
    }
}
