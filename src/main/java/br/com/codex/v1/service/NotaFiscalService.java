package br.com.codex.v1.service;

import br.com.codex.v1.domain.cadastros.AmbienteNotaFiscal;
import br.com.codex.v1.domain.cadastros.ConfiguracaoCertificado;
import br.com.codex.v1.domain.contabilidade.NotaFiscal;
import br.com.codex.v1.domain.contabilidade.XmlNotaFiscal;
import br.com.codex.v1.domain.dto.NotaFiscalDto;
import br.com.codex.v1.domain.repository.*;
import br.com.codex.v1.mapper.NotaFiscalMapper;
import br.com.codex.v1.service.exceptions.NfeCustomException;
import br.com.codex.v1.utilitario.Base64Util;
import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.nfe.Assinar;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.Validar;
import br.com.swconsultoria.nfe.dom.Evento;
import br.com.swconsultoria.nfe.dom.enuns.*;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.exception.NfeValidacaoException;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento;

import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEvento;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.com.swconsultoria.nfe.schema_4.retConsStatServ.TRetConsStatServ;
import br.com.swconsultoria.nfe.util.CancelamentoUtil;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import br.com.swconsultoria.nfe.util.ConstantesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class NotaFiscalService {
        private static final Logger logger = LoggerFactory.getLogger(NotaFiscalService.class);

        @Autowired
        private ConfiguracaoCertificadoRepository certificadoRepository;

        @Autowired
        private AmbienteNotaFiscalRepository ambienteNotaFiscalRepository;

        public ConfiguracoesNfe iniciarConfiguracao(NotaFiscalDto notaFiscalDto) throws Exception {
            logger.info("Iniciando configuração para NF-e, CNPJ: {}", notaFiscalDto.getDocumentoEmitente());
            Optional<ConfiguracaoCertificado> cert = certificadoRepository.findByCnpj(notaFiscalDto.getDocumentoEmitente());
            Optional<AmbienteNotaFiscal> ambienteNotaFiscal = ambienteNotaFiscalRepository.findById(1L);

            if (cert.isEmpty()) {
                throw new NfeException("Certificado não encontrado para CNPJ: " + notaFiscalDto.getDocumentoEmitente());
            }
            if (ambienteNotaFiscal.isEmpty()) {
                throw new NfeException("Configuração de ambiente não encontrada");
            }

            String senhaDecodificada = Base64Util.decode(cert.get().getSenha());
            Certificado certificado = CertificadoService.certificadoPfxBytes(cert.get().getArquivo(), senhaDecodificada);
            return ConfiguracoesNfe.criarConfiguracoes(
                    br.com.swconsultoria.nfe.dom.enuns.EstadosEnum.valueOf(cert.get().getUf()),
                    br.com.swconsultoria.nfe.dom.enuns.AmbienteEnum.valueOf(String.valueOf(ambienteNotaFiscal.get().getCodigoAmbiente())),
                    certificado,
                    "schemas"
            );
        }

        public TEnviNFe montarNotaFiscal(NotaFiscalDto nota) throws Exception {
            logger.info("Montando NF-e para número: {} série: {}", nota.getNumero(), nota.getSerie());
            TNFe tnfe = NotaFiscalMapper.paraTNFe(nota);
            TEnviNFe enviNFe = new TEnviNFe();
            enviNFe.setIdLote("001");
            enviNFe.setVersao(ConstantesUtil.VERSAO.NFE);
            enviNFe.setIndSinc("1"); // Síncrono
            enviNFe.getNFe().add(tnfe);
            return enviNFe;
        }

        public TEnviNFe assinarNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws NfeException, JAXBException {
            logger.info("Assinando NF-e, ID Lote: {}", enviNFe.getIdLote());
            String xml = XmlNfeUtil.objectToXml(enviNFe);
            String xmlAssinado = Assinar.assinaNfe(configuracoes, xml, AssinaturaEnum.NFE);
            return XmlNfeUtil.xmlToObject(xmlAssinado, TEnviNFe.class);
        }

        public void validarNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws NfeException, JAXBException {
            logger.info("Validando NF-e, ID Lote: {}", enviNFe.getIdLote());
            String xml = XmlNfeUtil.objectToXml(enviNFe);
            Validar validador = new Validar();
            boolean valido = validador.isValidXml(configuracoes, xml, ServicosEnum.ENVIO);
            if (!valido) {
                throw new NfeException("Erro na validação do XML da NF-e: " + validador.getErros());
            }
        }

        public TRetEnviNFe enviarNotaFiscal(TEnviNFe enviNFe, ConfiguracoesNfe configuracoes) throws NfeException {
            logger.info("Enviando NF-e, ID Lote: {}", enviNFe.getIdLote());
            TRetEnviNFe retorno = Nfe.enviarNfe(configuracoes, enviNFe, DocumentoEnum.NFE);
            if (!"100".equals(retorno.getProtNFe().getInfProt().getCStat())) {
                throw new NfeException("Erro ao enviar NF-e: " + retorno.getProtNFe().getInfProt().getXMotivo());
            }
            return retorno;
        }

        public void salvarXmlNotaFiscal(String chaveAcesso, String xml) throws NfeException {
            logger.info("Salvando XML da NF-e, chave: {}", chaveAcesso);
            // Implementar armazenamento (ex.: banco de dados ou sistema de arquivos)
            // Exemplo: salvar em banco
            // XmlNotaFiscal xmlNotaFiscal = new XmlNotaFiscal(chaveAcesso, xml, LocalDateTime.now());
            // xmlNotaFiscalRepository.save(xmlNotaFiscal);
        }
}
