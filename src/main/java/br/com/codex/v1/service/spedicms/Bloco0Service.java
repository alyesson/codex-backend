package br.com.codex.v1.service.spedicms;

import br.com.codex.v1.domain.dto.CadastroParticipantesSpedDto;
import br.com.codex.v1.domain.dto.NotaEntradaSpedDto;
import br.com.codex.v1.service.ParticipantesService;
import br.com.codex.v1.utilitario.Util;
import br.com.swconsultoria.efd.icms.registros.bloco0.*;

import java.time.LocalDate;
import java.util.List;

public class Bloco0Service {


    private static Bloco0 bloco0;

    private Bloco0Service() {}

    //Abertura do Arquivo Digital e Identificação da entidade
    public static Bloco0 getBloco(List<NotaEntradaSpedDto> listaNotas) {
        bloco0 = new Bloco0();
        preencherRegistro0000();
        preencherRegistro0001();
        preencherRegistro0002();
        preencherRegistro0005();
        preencheRegistro0015();
        preencherRegistro0100();

        List<CadastroParticipantesSpedDto> listaParticipantes = ParticipantesService.getListaParticipantesNotaSaida(listaNotas);
        preencherRegistro0150(listaParticipantes);

        return bloco0;
    }
    //Abertura do Arquivo Digital e Identificação da entidade
    private static void preencherRegistro0000() {

        Registro0000 registro0000 = new Registro0000();
        registro0000.setCod_fin("0");
        registro0000.setDt_ini(Util.dataSpeed(LocalDate.of(2021, 3, 1)));
        registro0000.setDt_fin(Util.dataSpeed(LocalDate.of(2021, 3, 31)));
        registro0000.setNome("Empresa Teste");
        registro0000.setCnpj("22777466000130");
        registro0000.setUf("SP");
        registro0000.setIe("167641160730");
        registro0000.setCod_mun("3550308");
        registro0000.setIm(null);
        registro0000.setSuframa(null);
        registro0000.setInd_perfil("A");
        registro0000.setInd_ativ("1");
        bloco0.setRegistro0000(registro0000);
    }

    //Abertura do Bloco 0
    private static void preencherRegistro0001() {
        Registro0001 registro0001 = new Registro0001();
        registro0001.setInd_mov("0");
        bloco0.setRegistro0001(registro0001);
    }

    //Classificação do Estabelecimento Industrial ou Equiparado a Industria
    private static void preencherRegistro0002(){
        Registro0002 registro0002 = new Registro0002();
        registro0002.setClass_estab_ind();

        bloco0.setRegistro0002(registro0002);
    }

    //Dados Complementares da entidade
    private static void preencherRegistro0005() {
        Registro0005 registro0005 = new Registro0005();
        registro0005.setFantasia("Empresa Teste Fantasia");
        registro0005.setCep("75000000");
        registro0005.setEnd("Rua Teste");
        registro0005.setNum("0");
        registro0005.setCompl("Qd 0 Lote 0");
        registro0005.setBairro("Centro");
        registro0005.setFone("62993066546");
        registro0005.setEmail("samuel@swconsultoria.com.br");
        bloco0.setRegistro0005(registro0005);
    }

    //Dados do Contribuinte Substituto ou Responsável pelo ICMS Destino
    public static void preencheRegistro0015() {
        Registro0015 registro0015 = new Registro0015();
        registro0015.setUf_st(ufContibuinteSubstituto.getText());
        registro0015.setIe_st(ieContibuinteSubstituto.getText());

        bloco0.getRegistro0015().add(registro0015);
    }

    private static void preencherRegistro0100() {
        Registro0100 registro0100 = new Registro0100();
        registro0100.setNome("Contador Ze");
        registro0100.setCpf("07165924019");
        registro0100.setCrc("123456");
        registro0100.setCnpj("73038588000146");
        registro0100.setCep("75000000");
        registro0100.setEnd("Rua Teste");
        registro0100.setBairro("Centro");
        registro0100.setFone("62993066546");
        registro0100.setEmail("samuel@swconsultoria.com.br");
        registro0100.setCod_mun("5201108");
        bloco0.setRegistro0100(registro0100);
    }

    private static void preencherRegistro0150(List<CadastroParticipantesSpedDto> listaParticipantes) {
        Registro0150 registro0150;
        for (CadastroParticipantesSpedDto participante : listaParticipantes) {
            registro0150 = new Registro0150();

            registro0150.setCod_part(participante.getCodPart());
            registro0150.setNome(participante.getNome());
            registro0150.setCod_pais("01058");

            if (!Util.isEmpty(participante.getCnpj())) {
                registro0150.setCnpj(Util.manterApenasNumeros(participante.getCnpj()));
            } else if (!Util.isEmpty(participante.getCpf())) {
                registro0150.setCpf(Util.manterApenasNumeros(participante.getCpf()));
            }

            if (!Util.isEmpty(participante.getIe()) && !participante.getIe().toLowerCase().startsWith("isento")) {
                registro0150.setIe(Util.manterApenasNumeros(participante.getIe()));
            }

            registro0150.setCod_mun(participante.getCodMunicipio());
            registro0150.setEnd(participante.getEndereco());
            registro0150.setNum(participante.getNumero());
            registro0150.setBairro(participante.getBairro());
            if (!Util.isEmpty(participante.getComplemento())) {
                registro0150.setCompl(participante.getComplemento());
            }
            bloco0.getRegistro0150().add(registro0150);
        }
    }

    //Identificação das unidades de medida
    public static void preencheRegistro0190(void bloco0) {

        Registro0190 registro0190 = new Registro0190();
        registro0190.setUnid(rs0190.getString("codigo"));
        registro0190.setDescr(rs0190.getString("descricao"));

        bloco0.getRegistro0190().add(registro0190);
    }

    //Tabela de Identificação do Item (Produtos e Serviços)
    public static void preencheRegistro0200(void bloco0) {

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

                    Registro0205 registro0205 = new Registro0205();
                    registro0205.setDesc_ant_item(rs0202.getString("descricaoAnterior"));
                    registro0205.setDt_ini(rs0202.getString("dataInicialUtilizacao"));
                    registro0205.setDt_fim(rs0202.getString("dataFinalUtilizacao"));
                    registro0205.setCod_ant_item(rs0202.getString("codigoAnterior"));

                    Registro0206 registro0206 = new Registro0206();
                    registro0206.setCod_comb(rs0202.getString("codigoProduto"));
                    registro0200.setRegistro0206(registro0206);
                    bloco0.getRegistro0200().add(registro0200);
    }

    //Consumo Específico Padronizado
    public static void preencheRegistro0210(void bloco0) {

        Registro0210 registro0210 = new Registro0210();
        registro0210.setCod_item_comp(produto);
        registro0210.setQtd_comp(rs0210a.getString("quantdade"));
        registro0210.setPerda("");

        bloco0.getRegistro0210().add(registro0210);
    }

    //Fatores de Conversão de Unidade
    public static void preencheRegistro0220(void bloco0) {

        Registro0220 registro0220 = new Registro0220();
        registro0220.setUnid_conv(rs0220.getString("medida"));
        registro0220.setFat_conv(rs0220.getString("fatorConversao"));
        registro0220.setCod_barra(rs0220.getString("ean"));

        bloco0.getRegistro0220().add(registro0220);
    }

    //Correlação entre códigos de itens comercializados
    public static void preencheRegistro0221() {

        Registro0221 registro0221 = new Registro0221();
        registro0221.setCod_item_atomico(rs0221.getString("codigoProduto"));
        registro0221.setCod_item_atomico(rs0221.getString("fatorConversao"));

        bloco0.getRegistro0221().add(registro0221);
    }

    //Cadastro de bens ou componentes do Ativo Imobilizado
    public static void preencheRegistro0300() {

        Registro0300 registro0300 = new Registro0300();
        registro0300.setCod_ind_bem(rs0300.getString("numeroPatrimonio"));
        registro0300.setIdent_merc(rs0300.getString("grupoDoBens").substring(0, 2));
        registro0300.setDescr_item(rs0300.getString("descricaoDoBem"));
        registro0300.setDescr_item(rs0300.getString(""));
        registro0300.setCod_cta(rs0300.getString("codigoConta"));
        registro0300.setNr_parc("1");

        Registro0305 registro0305 = new Registro0305();
        registro0305.setCod_ccus(rs0300.getString("cCustoBem"));
        registro0305.setFunc(rs0300.getString("descricaoDoBem"));
        registro0305.setVida_util(rs0300.getString("vidaUtil"));
        registro0300.setRegistro0305(registro0305);

        bloco0.getRegistro0300().add(registro0300);
    }

    //Tabela de Natureza da Operação/ Prestação
    public static void preencheRegistro0400(){
        Registro0400 registro0400 = new Registro0400();
        registro0400.setCod_nat(rs0400.getString("codigo"));
        registro0400.setDescr_nat(rs0400.getString("descricao"));

        bloco0.getRegistro0400().add(registro0400);
    }

    //Tabela de Informação Complementar do documento fiscal
    public static void preencheRegistro0450(){
        Registro0450 registro0450 = new Registro0450();
        registro0450.setCod_inf("1");
        registro0450.setTxt(rs0450.getString("infoAdicional"));
        bloco0.getRegistro0450().add(registro0450);
    }

    //Tabela de Observações do Lançamento Fiscal
    public static void preencheRegistro0460(){
        Registro0460 registro0460 = new Registro0460();
        registro0460.setCod_obs(rs0460.getString("codigo"));
        registro0460.setTxt(rs0460.getString("descricao"));

        bloco0.getRegistro0460().add(registro0460);
    }

    //Plano de contas contábeis
    public static void preencheRegistro0500(){

        Registro0500 registro0500 = new Registro0500();
        registro0500.setDt_alt(df.format(rs0500.getDate("dataInclusao")));
        registro0500.setCod_nat_cc(rs0500.getString("naturezaPlanConta").substring(0, 2));
        registro0500.setInd_cta(rs0500.getString("tipo").substring(0,1));
        registro0500.setNivel(rs0500.getString("codigoReduzido"));
        registro0500.setCod_cta(rs0500.getString("codigoExpandido"));
        registro0500.setNome_cta(rs0500.getString("nomePlanoConta"));

        bloco0.getRegistro0500().add(registro0500);
    }

    //Centro de custos
    public static void preencheRegistro0600(){
        
        Registro0600 registro0600 = new Registro0600();
        registro0600.setDt_alt(df.format(rs0600.getDate("dataAtivacao")));
        registro0600.setCod_ccus(rs0600.getString("centroCusto"));
        registro0600.setCod_ccus(rs0600.getString("descricao"));
        bloco0.getRegistro0600().add(registro0600);
        Registro0600 registro0600 = new Registro0600();
        registro0600.setDt_alt(df.format(rs0600.getDate("dataAtivacao")));
        registro0600.setCod_ccus(rs0600.getString("codCCustoSinte"));
        registro0600.setCod_ccus(rs0600.getString("ccSintetico"));
        bloco0.getRegistro0600().add(registro0600);
    }

}
