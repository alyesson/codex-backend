package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.NotaEntradaSpedDto;
import br.com.codex.v1.domain.fiscal.spedicms.EfdNota;
import br.com.codex.v1.utilitario.Util;
import br.com.swconsultoria.efd.icms.bo.GerarEfdIcms;
import br.com.swconsultoria.efd.icms.registros.EfdIcms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GerarSpedService {
    private static final Logger logger = LoggerFactory.getLogger(GerarSpedService.class);

    public void gerarBlocos(){

        try {
            logger.info("Extraindo dados das Notas Entrada");
            List<NotaEntradaSpedDto> listaNotasEntrada = EfdNota.getListaNotasEntrada(LocalDate dataInicial, LocalDate dataFinal);

            logger.info("Extraindo dados das Notas Saída");
            List<NotaEntradaSpedDto> listaNotasSaida = EfdNota.getListaNotasSaida(LocalDate dataInicial, LocalDate dataFinal, String documentoEmissor);

            System.out.println("Preenchendo os Blocos...");
            EfdIcms efd = new EfdIcms();
            efd.setBloco0(Bloco0Service.getBloco());
            efd.setBlocoB(BlocoBService.getBloco());
            efd.setBlocoC(BlocoCService.getBloco(listaNotasSaida));
            efd.setBlocoD(BlocoDService.getBloco(listaNotasEntrada));
            efd.setBlocoE(BlocoEService.getBloco());
            efd.setBlocoG(BlocoGService.getBloco());
            efd.setBlocoH(BlocoHService.getBloco());
            efd.setBlocoK(BlocoKService.getBloco());
            efd.setBloco1(Bloco1Service.getBloco());

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
