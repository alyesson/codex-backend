package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.TabelaCfop;
import br.com.codex.v1.domain.contabilidade.AtivoImobilizado;
import br.com.codex.v1.domain.dto.GerarSpedRequestDto;
import br.com.codex.v1.domain.dto.NotaEntradaSpedDto;
import br.com.codex.v1.domain.dto.NotaSaidaSpedDto;
import br.com.codex.v1.domain.estoque.Produto;
import br.com.codex.v1.domain.fiscal.InformacaoesAdicionaisFisco;
import br.com.codex.v1.domain.fiscal.InformacaoesComplementares;
import br.com.codex.v1.domain.fiscal.spedicms.EfdNota;
import br.com.codex.v1.domain.repository.AtivoImobilizadoRepository;
import br.com.codex.v1.domain.repository.InformacaoesAdicionaisFiscoRepository;
import br.com.codex.v1.domain.repository.InformacaoesComplementaresRepository;
import br.com.codex.v1.domain.repository.ProdutoRepository;
import br.com.codex.v1.service.spedicms.Bloco0Service;
import br.com.codex.v1.utilitario.Util;
import br.com.swconsultoria.efd.icms.bo.GerarEfdIcms;
import br.com.swconsultoria.efd.icms.registros.EfdIcms;
import br.com.swconsultoria.efd.icms.registros.blocoB.BlocoB;
import br.com.swconsultoria.efd.icms.registros.blocoC.BlocoC;
import br.com.swconsultoria.efd.icms.registros.blocoD.BlocoD;
import br.com.swconsultoria.efd.icms.registros.blocoE.BlocoE;
import br.com.swconsultoria.efd.icms.registros.blocoG.BlocoG;
import br.com.swconsultoria.efd.icms.registros.blocoH.BlocoH;
import br.com.swconsultoria.efd.icms.registros.blocoK.BlocoK;
import br.com.swconsultoria.efd.icms.registros.bloco1.Bloco1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GerarSpedService {
    private static final Logger logger = LoggerFactory.getLogger(GerarSpedService.class);

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private AtivoImobilizadoRepository ativoImobilizadoRepository;

    @Autowired
    private TabelaCfopService tabelaCfopService;

    @Autowired
    private InformacaoesAdicionaisFiscoRepository informacaoesAdicionaisFiscoRepository;

    @Autowired
    private InformacaoesComplementaresRepository informacaoesComplementaresRepository;

    @Autowired
    private ContasService contasService;

    @Autowired
    private EfdNota efdNota;

    @Autowired
    private Bloco0Service bloco0Service;

    public void gerarBlocos(GerarSpedRequestDto requestDto){

        LocalDate dataInicial = requestDto.getDataInicio();
        LocalDate dataFinal = requestDto.getDataFim();

        try {
            logger.info("Extraindo dados das Notas Entrada");
            List<NotaEntradaSpedDto> listaNotasEntrada = efdNota.getListaNotasEntrada(dataInicial, dataFinal);

            logger.info("Extraindo dados das Notas Saída");
            List<NotaSaidaSpedDto> listaNotasSaida = efdNota.getListaNotasSaida(dataInicial, dataFinal);

            logger.info(("Extraindo unidades de medida"));
            List<String> listaUnidadesMedida = produtoService.findByUnidadeComercial();

            logger.info(("Extraindo informações dos produtos"));
            List<Produto> listaProdutos = produtoService.findAll();

            logger.info(("Extraindo informações dos ativos imobilizados"));
            List<AtivoImobilizado> listaAtivosImobilizados = ativoImobilizadoRepository.findAll();

            logger.info("Extraindo CFOPs utilizados no período");
            List<TabelaCfop> listaCfop = tabelaCfopService.findCfopsUtilizadosNoPeriodo(requestDto.getDataInicio(), requestDto.getDataFim());

            logger.info("Extraindo informações fisco utilizados no período");
            List<InformacaoesAdicionaisFisco> listaInfoFisco = informacaoesAdicionaisFiscoRepository.findAll();

            logger.info("Extraindo informações adicionais utilizados no período");
            List<InformacaoesComplementares> listaInfoComp = informacaoesComplementaresRepository.findAll();

            System.out.println("Preenchendo os Blocos...");
            EfdIcms efd = new EfdIcms();

            // Bloco 0 - já implementado
            efd.setBloco0(bloco0Service.getBloco(requestDto, listaNotasSaida, listaUnidadesMedida,
                    listaProdutos, listaAtivosImobilizados, listaCfop, listaInfoFisco, listaInfoComp, contasService));

            // Bloco B - Registro de Apuração do ISS
            // Você precisa criar o serviço para preencher este bloco
            BlocoB blocoB = new BlocoB();
            // blocoB = seuServiceBlocoB.preencherBlocoB(dados);
            efd.setBlocoB(blocoB);

            // Bloco C - Documentos Fiscais I - Mercadorias (ICMS/IPI)
            BlocoC blocoC = new BlocoC();
            // Implementar preenchimento com notas de saída
            efd.setBlocoC(blocoC);

            // Bloco D - Documentos Fiscais II - Serviços (ICMS)
            BlocoD blocoD = new BlocoD();
            // Implementar preenchimento com notas de entrada
            efd.setBlocoD(blocoD);

            // Bloco E - Apuração do ICMS e do IPI
            BlocoE blocoE = new BlocoE();
            // Implementar preenchimento
            efd.setBlocoE(blocoE);

            // Bloco G - Controle do Crédito de ICMS do Ativo Permanente (CIAP)
            BlocoG blocoG = new BlocoG();
            // Implementar preenchimento com ativos imobilizados
            efd.setBlocoG(blocoG);

            // Bloco H - Inventário Físico
            BlocoH blocoH = new BlocoH();
            // Implementar preenchimento
            efd.setBlocoH(blocoH);

            // Bloco K - Controle da Produção e do Estoque
            BlocoK blocoK = new BlocoK();
            // Implementar preenchimento
            efd.setBlocoK(blocoK);

            // Bloco 1 - Outras Informações
            Bloco1 bloco1 = new Bloco1();
            // Implementar preenchimento
            efd.setBloco1(bloco1);

            System.out.println("Gerando contadores e conteudo...");
            StringBuilder sb = new StringBuilder();
            GerarEfdIcms.gerar(efd, sb);

            System.out.println("Salvar Arquivo...");
            String spedConteudo = sb.toString();
            String caminhoArquivo = Util.criarArquivo("/tmp/efd", "sped-icms.txt", spedConteudo);
            System.out.println("Processo Finalizado. Arquivo Gerado em: "+caminhoArquivo);

        } catch (Exception e) {
            logger.error("Erro ao gerar SPED EFD ICMS", e);
            e.printStackTrace();
        }
    }
}