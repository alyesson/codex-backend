package br.com.codex.v1.service;

import br.com.codex.v1.domain.contabilidade.ControleNsu;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.repository.ControleNsuRepository;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.ConsultaDFeEnum;
import br.com.swconsultoria.nfe.dom.enuns.PessoaEnum;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ControleNsuService {
    private static final Logger logger = LoggerFactory.getLogger(ControleNsuService.class);

    @Autowired
    private ControleNsuRepository controleNsuRepository;

    //@Autowired
    //private NotaFiscalService notaFiscalService;

    /**
     * Consulta o último NSU registrado para um CNPJ.
     */
    public BigInteger consultarUltimoNSU(String cnpj) {
        logger.info("Consultando último NSU para CNPJ: {}", cnpj);
        return controleNsuRepository.findTopByCnpjOrderByUltimoNsuDesc(cnpj).orElse(BigInteger.ZERO);
    }

    /**
     * Consulta documentos fiscais na SEFAZ e atualiza o controle de NSU.
     */
    @Transactional
    public void consultarDocumentos(String cnpj, String ambiente) throws NfeException {
        logger.info("Consultando documentos para CNPJ: {}, Ambiente: {}", cnpj, ambiente);

        // Busca o controle de NSU
        Optional<ControleNsu> controleNsuOpt = controleNsuRepository.findByCnpjAndAmbiente(cnpj, ambiente);
        ControleNsu controleNsu = controleNsuOpt.orElseGet(() -> {
            ControleNsu novo = new ControleNsu();
            novo.setCnpj(cnpj);
            novo.setAmbiente(ambiente);
            novo.setUltimoNsu(0L);
            novo.setDataUltimaConsulta(LocalDateTime.now());
            return novo;
        });

        // Configurações da SEFAZ - corrigindo a criação do DTO
        NotaFiscalDto dto = new NotaFiscalDto();
        dto.setDocumentoEmitente(cnpj);
        ConfiguracoesNfe config = notaFiscalService.iniciarConfiguracao(dto);

        String nsu = String.format("%015d", controleNsu.getUltimoNsu());

        // Consulta a SEFAZ
        RetDistDFeInt retorno = Nfe.distribuicaoDfe(config, PessoaEnum.JURIDICA, cnpj, ConsultaDFeEnum.NSU, nsu);

        // Atualiza o NSU
        BigInteger maxNsu = new BigInteger(retorno.getUltNSU());
        controleNsu.setUltimoNsu(maxNsu.longValue());
        controleNsu.setDataUltimaConsulta(LocalDateTime.now());
        controleNsuRepository.save(controleNsu);

        // Salva os documentos retornados
        if (retorno.getLoteDistDFeInt() != null) {
            for (RetDistDFeInt.LoteDistDFeInt.DocZip doc : retorno.getLoteDistDFeInt().getDocZip()) {
                String xmlContent = new String(doc.getValue());
                // Extrai a chave do XML ou usa um fallback
                String chave = extrairChaveDoXml(xmlContent);
                notaFiscalService.salvarXmlNotaFiscal(chave, xmlContent);
            }
        }
    }

    /**
     * Métudo auxiliar para extrair a chave do XML
     */
    private String extrairChaveDoXml(String xmlContent) {
        try {
            // Extrai a chave do XML (exemplo: busca por <chNFe> ou <chave>)
            int startIndex = xmlContent.indexOf("<chNFe>") + 7;
            if (startIndex < 7) {
                startIndex = xmlContent.indexOf("<chave>") + 7;
            }
            if (startIndex >= 7) {
                int endIndex = xmlContent.indexOf("<", startIndex);
                if (endIndex > startIndex) {
                    return xmlContent.substring(startIndex, endIndex);
                }
            }
        } catch (Exception e) {
            logger.warn("Não foi possível extrair a chave do XML", e);
        }
        // Fallback: gera um identificador único
        return "doc-" + System.currentTimeMillis();
    }
}