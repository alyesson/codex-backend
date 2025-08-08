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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GerarSpedService {
    private static final Logger logger = LoggerFactory.getLogger(GerarSpedService.class);

    @Autowired
    private EfdNota efdNota;

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

    public void gerarBlocos(GerarSpedRequestDto requestDto){

        LocalDate dataInicial = requestDto.getDataInicio();
        LocalDate dataFinal = requestDto.getDataFim();

        LocalDateTime dataInicial1 = requestDto.getDataInicio().atStartOfDay();
        LocalDateTime dataFinal1 = requestDto.getDataFim().atStartOfDay();

        try {
            logger.info("Extraindo dados das Notas Entrada");
            List<NotaEntradaSpedDto> listaNotasEntrada = EfdNota.getListaNotasEntrada(dataInicial, dataFinal);

            logger.info("Extraindo dados das Notas Saída");
            List<NotaSaidaSpedDto> listaNotasSaida = EfdNota.getListaNotasSaida(dataInicial1, dataFinal1);

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
            efd.setBloco0(Bloco0Service.getBloco(requestDto, listaNotasSaida, listaUnidadesMedida,
                    listaProdutos, listaAtivosImobilizados, listaCfop, listaInfoFisco, listaInfoComp, contasService));

            /*efd.setBlocoB(BlocoBService.getBloco());
            efd.setBlocoC(BlocoCService.getBloco(listaNotasSaida));
            efd.setBlocoD(BlocoDService.getBloco(listaNotasEntrada));
            efd.setBlocoE(BlocoEService.getBloco());
            efd.setBlocoG(BlocoGService.getBloco());
            efd.setBlocoH(BlocoHService.getBloco());
            efd.setBlocoK(BlocoKService.getBloco());
            efd.setBloco1(Bloco1Service.getBloco());*/

            System.out.println("Gerando contadores e conteudo...");
            StringBuilder sb = new StringBuilder();
            GerarEfdIcms.gerar(efd, sb);

            System.out.println("Salvar Arquivo...");
            String spedConteudo = sb.toString();
            String caminhoArquivo = Util.criarArquivo("/tmp/efd", "sped-icms.txt", spedConteudo);
            System.out.println("Processo Finalizado. Arquivo Gerado em: "+caminhoArquivo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
