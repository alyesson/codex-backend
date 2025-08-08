package br.com.codex.v1.service.spedicms;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import br.com.codex.v1.domain.contabilidade.AtivoImobilizado;
import br.com.codex.v1.domain.dto.*;
import br.com.codex.v1.domain.estoque.Produto;
import br.com.codex.v1.domain.fiscal.InformacaoesAdicionaisFisco;
import br.com.codex.v1.domain.fiscal.InformacaoesComplementares;
import br.com.codex.v1.service.ContasService;
import br.com.codex.v1.service.ParticipantesService;
import br.com.codex.v1.utilitario.Util;
import br.com.swconsultoria.efd.icms.registros.bloco0.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Bloco0Service {

    private static Bloco0 bloco0;

    private Bloco0Service() {}

    //Abertura do Arquivo Digital e Identificação da entidade
    public static Bloco0 getBloco(GerarSpedRequestDto requestDto, List<NotaSaidaSpedDto> listaNotas,
                                  List<String> listaUnidadesMedida, List<Produto> listaProdutos,
                                  List<AtivoImobilizado> listaAtivosImobilizados, List<TabelaCfop> listaCfop,
                                  List<InformacaoesAdicionaisFisco> listaInfoFisco, List<InformacaoesComplementares> listaInfoComp,
                                  ContasService contasService) {
        bloco0 = new Bloco0();
        preencherRegistro0000(requestDto);
        preencherRegistro0001(requestDto);
        preencherRegistro0002(requestDto);
        preencherRegistro0005(requestDto);
        preencheRegistro0015(requestDto);
        preencherRegistro0100(requestDto);

        List<CadastroParticipantesSpedDto> listaParticipantes = ParticipantesService.getListaParticipantesNotaSaida(listaNotas);
        preencherRegistro0150(listaParticipantes);

        preencheRegistro0190(listaUnidadesMedida);
        preencheRegistro0200(listaProdutos);
        preencheRegistro0210(listaProdutos);
        preencheRegistro0220(listaProdutos);
        preencheRegistro0221(listaProdutos);

        preencheRegistro0300(listaAtivosImobilizados);

        preencheRegistro0400(listaCfop);
        preencheRegistro0450(listaInfoComp);
        preencheRegistro0460(listaInfoFisco);

        preencheRegistro0500(listaProdutos, contasService);

        preencheRegistro0600();


        return bloco0;
    }
    //Abertura do Arquivo Digital e Identificação da entidade
    private static void preencherRegistro0000(GerarSpedRequestDto requestDto) {

        Registro0000 registro0000 = new Registro0000();
        registro0000.setCod_fin(requestDto.getFinalidadeArquivo());
        registro0000.setDt_ini(Util.dataSpeed(requestDto.getDataInicio()));
        registro0000.setDt_fin(Util.dataSpeed(requestDto.getDataInicio()));
        registro0000.setNome(requestDto.getEmpresa().getRazaoSocial());
        registro0000.setCnpj(requestDto.getEmpresa().getCnpj());
        registro0000.setUf(requestDto.getEmpresa().getUf());
        registro0000.setIe(requestDto.getEmpresa().getInscricaoEstadual());
        registro0000.setCod_mun(requestDto.getEmpresa().getCodigoCidade());
        registro0000.setIm(requestDto.getEmpresa().getInscricaoMunicipal());
        registro0000.setSuframa(requestDto.getEmpresa().getSuframa());
        registro0000.setInd_perfil(requestDto.getPerfil());
        registro0000.setInd_ativ(requestDto.getAtividade());
        bloco0.setRegistro0000(registro0000);
    }

    //Abertura do Bloco 0
    private static void preencherRegistro0001(GerarSpedRequestDto requestDto) {
        Registro0001 registro0001 = new Registro0001();
        registro0001.setInd_mov(requestDto.getIndicadorMovimento());
        bloco0.setRegistro0001(registro0001);
    }

    //Classificação do Estabelecimento Industrial ou Equiparado a Indústria
    private static void preencherRegistro0002(GerarSpedRequestDto requestDto){
        Registro0002 registro0002 = new Registro0002();
        registro0002.setClass_estab_ind(requestDto.getClassificacaoEstabelecimento());

        bloco0.setRegistro0002(registro0002);
    }

    //Dados Complementares da entidade
    private static void preencherRegistro0005(GerarSpedRequestDto requestDto) {
        Registro0005 registro0005 = new Registro0005();
        registro0005.setFantasia(requestDto.getEmpresa().getNomeFantasia());
        registro0005.setCep(requestDto.getEmpresa().getCep());
        registro0005.setEnd(requestDto.getEmpresa().getEndereco());
        registro0005.setNum(requestDto.getEmpresa().getNumero());
        registro0005.setCompl(requestDto.getEmpresa().getComplemento());
        registro0005.setBairro(requestDto.getEmpresa().getBairro());
        registro0005.setFone(requestDto.getEmpresa().getTelefone());
        registro0005.setEmail(requestDto.getEmpresa().getEmailContato());
        bloco0.setRegistro0005(registro0005);
    }

    //Dados do Contribuinte Substituto ou Responsável pelo ICMS Destino
    public static void preencheRegistro0015(GerarSpedRequestDto requestDto) {
        Registro0015 registro0015 = new Registro0015();
        registro0015.setUf_st(requestDto.getUfContribuinteSubstituto());
        registro0015.setIe_st(requestDto.getInscricaoEstadualContribuinteSubstituto());

        bloco0.getRegistro0015().add(registro0015);
    }

    //Dados do Contador
    private static void preencherRegistro0100(GerarSpedRequestDto requestDto) {
        Registro0100 registro0100 = new Registro0100();
        registro0100.setNome(requestDto.getNomeContador());
        registro0100.setCpf(requestDto.getCpfContador());
        registro0100.setCrc(requestDto.getCrcContador());
        registro0100.setCnpj(requestDto.getCnpjContador());
        registro0100.setCep(requestDto.getCepContador());
        registro0100.setEnd(requestDto.getLogradouroContador());
        registro0100.setBairro(requestDto.getBairroContador());
        registro0100.setFone(requestDto.getTelefoneContador());
        registro0100.setEmail(requestDto.getEmailContador());
        registro0100.setCod_mun(requestDto.getCodigoMunicipioContador());
        bloco0.setRegistro0100(registro0100);
    }

    //Tabela de Cadastro de Participantes
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
    public static void preencheRegistro0190(List<String> listaUnidadesMedida) {

        if (listaUnidadesMedida != null) {
            for (String unidade : listaUnidadesMedida) {
                Registro0190 registro0190 = new Registro0190();
                registro0190.setUnid(obterCodigo(unidade));
                registro0190.setDescr(unidade);
                bloco0.getRegistro0190().add(registro0190);
            }
        }
    }

    //Tabela de Identificação do item (Produtos e Serviços)
    public static void preencheRegistro0200(List<Produto> listaProdutos) {

        for(Produto produto : listaProdutos){

            Registro0200 registro0200 = new Registro0200();
            registro0200.setCod_item(produto.getCodigo());
            registro0200.setDescr_item(produto.getDescricao());
            registro0200.setCod_barra(produto.getEan());
            registro0200.setCod_ant_item(null);
            registro0200.setUnid_inv(produto.getUnidadeComercial());
            registro0200.setTipo_item(produto.getCategoriaProduto());
            registro0200.setCod_ncm(produto.getCodigoNcm());
            registro0200.setEx_ipi(produto.getExtipi());
            registro0200.setCod_gen(produto.getGrupo().substring(0, 2)); //pega os dois primeiros dígitos
            registro0200.setCod_lst(null);
            registro0200.setAliq_icms(String.valueOf(produto.getPercentualIcms()));
            registro0200.setCest(produto.getCodigoCest());

            if(registro0200.getCod_ant_item() == null){
                Registro0205 registro0205 = new Registro0205();
                registro0205.setDesc_ant_item(null);
                registro0205.setDt_ini(null);
                registro0205.setDt_fim(null);
                registro0205.setCod_ant_item(null);

                registro0200.getRegistro0205().add(registro0205);
            }

            if(isProdutoCombustivel(produto)){
                Registro0206 registro0206 = new Registro0206();
                registro0206.setCod_comb(produto.getCodigo());
                registro0200.setRegistro0206(registro0206);

                registro0200.setRegistro0206(registro0206);
            }
            bloco0.getRegistro0200().add(registro0200);
        }
    }

    //Consumo Específico Padronizado
    public static void preencheRegistro0210(List<Produto> listaProdutos) {

        for (Produto composicao : listaProdutos) {
            Registro0210 registro0210 = new Registro0210();
            registro0210.setCod_item_comp(composicao.getCodigo());
            registro0210.setQtd_comp(String.valueOf(composicao.getQuantidadePorUnidade()));
            registro0210.setPerda("0.00");

            // Vincula ao produto pai (Registro 0200)
            bloco0.getRegistro0200().stream()
                    .filter(r -> r.getCod_item().equals(composicao.getCodigo()))
                    .findFirst()
                    .ifPresent(pai -> pai.getRegistro0210().add(registro0210));
        }
    }

    //Fatores de Conversão de Unidade
    public static void preencheRegistro0220(List<Produto> produtos) {
        for (Produto produto : produtos) {
                Registro0220 registro0220 = new Registro0220();
                registro0220.setUnid_conv(null);
                registro0220.setFat_conv(null);
                registro0220.setCod_barra(null);

                // Vincula ao produto pai (Registro 0200)
                bloco0.getRegistro0200().stream()
                        .filter(r -> r.getCod_item().equals(produto.getCodigo()))
                        .findFirst()
                        .ifPresent(pai -> pai.getRegistro0220().add(registro0220));
        }
    }

    //Correlação entre códigos de itens comercializados
    public static void preencheRegistro0221(List<Produto> produtos) {
        for (Produto composicao : produtos) {
            Registro0221 registro0221 = new Registro0221();
            registro0221.setCod_item_atomico(composicao.getCodigo());
            registro0221.setQtd_contida(composicao.getQuantidadePorUnidade().toString());

            // Vincula ao produto pai (Registro 0200)
            bloco0.getRegistro0200().stream()
                    .filter(r -> r.getCod_item().equals(composicao.getCodigo()))
                    .findFirst()
                    .ifPresent(pai -> pai.getRegistro0221().add(registro0221));
        }
    }

    //Cadastro de bens ou componentes do Ativo Imobilizado
    public static void preencheRegistro0300(List<AtivoImobilizado> listaAtivosImobilizados) {

        for (AtivoImobilizado ativo : listaAtivosImobilizados) {
            Registro0300 registro0300 = new Registro0300();
            registro0300.setCod_ind_bem(ativo.getCodigoBem());
            registro0300.setIdent_merc(ativo.getGrupoDeBens().substring(0, 2));
            registro0300.setDescr_item(ativo.getDescricaoDoBem());
            registro0300.setCod_cta(ativo.getCodigoConta());
            registro0300.setNr_parc("1");

            Registro0305 registro0305 = new Registro0305();
            registro0305.setCod_ccus(ativo.getCentroCusto());
            registro0305.setFunc(ativo.getDescricaoDoBem());
            registro0305.setVida_util(String.valueOf(ativo.getVidaUtilMeses()));

            registro0300.setRegistro0305(registro0305);

            bloco0.getRegistro0300().add(registro0300);
        }
    }

    //Tabela de Natureza da Operação/ Prestação
    public static void preencheRegistro0400(List<TabelaCfop> listaCfop){

        for(TabelaCfop cfop : listaCfop) {
            Registro0400 registro0400 = new Registro0400();
            registro0400.setCod_nat(String.valueOf(cfop.getCodigo()));
            registro0400.setDescr_nat(cfop.getDescricao());

            bloco0.getRegistro0400().add(registro0400);
        }
    }

    //Tabela de Informação Complementar do documento fiscal
    public static void preencheRegistro0450(List<InformacaoesComplementares> listaInfoCompl){
        for(InformacaoesComplementares infoAdic : listaInfoCompl) {
            Registro0450 registro0450 = new Registro0450();
            registro0450.setCod_inf(infoAdic.getCodigo());
            registro0450.setTxt(infoAdic.getDescricao());
            bloco0.getRegistro0450().add(registro0450);
        }
    }

    //Tabela de Observações do Lançamento Fiscal
    public static void preencheRegistro0460(List<InformacaoesAdicionaisFisco> listaInfoAdic){
        for(InformacaoesAdicionaisFisco infoAdic : listaInfoAdic) {
            Registro0460 registro0460 = new Registro0460();
            registro0460.setCod_obs(infoAdic.getCodigo());
            registro0460.setTxt(infoAdic.getDescricao());

            bloco0.getRegistro0460().add(registro0460);
        }
    }

    //Plano de contas contábeis
    public static void preencheRegistro0500(List<Produto> listaProdutos, ContasService contaService) {
        Set<String> contasUnicas = new HashSet<>();

        // Extrai todas as contas únicas dos produtos
        for (Produto produto : listaProdutos) {
            if (produto.getContaContabil() != null) {
                contasUnicas.add(produto.getContaContabil());
            }
        }
        int ano = LocalDate.now().getYear();

        // Para cada conta, busca informações e preenche o registro 0500
        for (String contaCodigo : contasUnicas) {
            ContaSpedDto contaDto = contaService.buscarContaParaSped(contaCodigo);
            if (contaDto != null) {
                Registro0500 registro0500 = new Registro0500();
                registro0500.setDt_alt("0101"+ano);
                registro0500.setCod_nat_cc(contaDto.getNatureza());
                registro0500.setInd_cta(contaDto.getTipo());
                registro0500.setNivel(String.valueOf(contaDto.getNivel()));
                registro0500.setCod_cta(contaDto.getCodigo());
                registro0500.setNome_cta(contaDto.getNome());

                bloco0.getRegistro0500().add(registro0500);
            }
        }
    }

    //Centro de custos
    public static void preencheRegistro0600(){
        int ano = LocalDate.now().getYear();

        Registro0600 registro0600 = new Registro0600();
        registro0600.setDt_alt("0101"+ano);
        registro0600.setCod_ccus(null);
        registro0600.setCcus(null);
        bloco0.getRegistro0600().add(registro0600);
    }

    // Métoudo auxiliar para obter a descrição (opcional)
    private static String obterCodigo(String descricao) {
        return switch (descricao) {
            case "Unidade" -> "UN";
            case "Quilograma" -> "KG";
            case "Litro" -> "LT";
            case "Metro" -> "M";
            case "Metro quadrado" -> "M2";
            case "Metro cúbico" -> "M3";
            case "Pares" -> "PR";
            case "Dúzia" -> "DZ";
            case "Caixa" -> "CX";
            case "Saco" -> "SC";
            case "Tonelada" -> "TON";
            case "Folha" -> "FL";
            case "Garrafa" -> "GF";
            case "Rolo" -> "RL";
            case "Kit" -> "KT";
            case "Lata" -> "LA";
            case "Ampola" -> "AM";
            case "Bobina" -> "BO";
            case "Balde" -> "BD";
            case "Barril" -> "BR";
            case "Conjunto" -> "CJ";
            case "Cento" -> "CT";
            case "Milheiro" -> "MIL";
            case "Jogo" -> "JG";
            case "Pacote" -> "PC";
            case "Resma" -> "RM";
            case "Service (Unidade de serviço)" -> "SV";
            case "Tubo" -> "TB";
            default -> descricao; // Retorna o próprio código se não houver descrição
        };
    }

    // Métudo auxiliar para identificar combustíveis
    private static boolean isProdutoCombustivel(Produto produto) {
        return produto.getSubGrupo() != null &&
                (produto.getSubGrupo().equals("COMBUSTIVEL") ||
                        produto.getSubGrupo().equals("LUBRIFICANTE"));
    }

    // Extrai todos os níveis hierárquicos das contas (ex: "1.1.02" → ["1", "1.1", "1.1.02"])
    private Set<String> extrairHierarquiaCompleta(List<String> contasProdutos) {
        Set<String> hierarquia = new TreeSet<>(); // Usamos TreeSet para ordenar naturalmente
        for (String conta : contasProdutos) {
            if (conta == null || conta.isEmpty()) continue;
            String[] partes = conta.split("\\.");
            StringBuilder codigoPai = new StringBuilder();
            for (String parte : partes) {
                codigoPai.append(parte);
                hierarquia.add(codigoPai.toString());
                codigoPai.append(".");
            }
        }
        return hierarquia;
    }

    // Determina se a conta é analítica (último nível)
    private boolean isContaAnalitica(String codigo, Set<String> contas) {
        return contas.stream().noneMatch(c -> c.startsWith(codigo + "."));
    }

    // Define a natureza da conta (COD_NAT) com base no primeiro nível
    private String getCodNat(String codigo) {
        String primeiroNivel = codigo.split("\\.")[0];
        return switch (primeiroNivel) {
            case "1" -> "01"; // Ativo
            case "2" -> "02"; // Passivo
            case "3" -> "04"; // Receitas
            case "4" -> "05"; // Despesas
            default -> "09"; // Outros (ajuste conforme seu plano de contas)
        };
    }
}
