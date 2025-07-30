package br.com.codex.v1.service;


import br.com.codex.v1.domain.dto.EmissorBoletoDto;
import br.com.codex.v1.domain.financeiro.EmissorBoleto;
import br.com.codex.v1.domain.repository.EmissorBoletoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.banco.*;
import org.jrimum.domkee.pessoa.CEP;
import org.jrimum.domkee.pessoa.Endereco;
import org.jrimum.domkee.pessoa.UnidadeFederativa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmissorBoletoService {
    private static final Logger logger = LoggerFactory.getLogger(EmissorBoletoService.class);

    @Autowired
    private EmissorBoletoRepository emissorBoletoRepository;

    @Autowired
    private NossoNumeroCalculator nossoNumeroCalculator;

    public File emitirBoleto(EmissorBoletoDto dto) throws ParseException {

        //1. calcula o nosso número
        calcularNossoNumero(dto);

        //2. Gera o número do documento
        dto.setNumeroDocumento(geradorNumeroDocumento());

        //3. Emite o boleto
        File boletoPdf = emitirBoletoPorBanco(dto);

        //4. salva as informações
        create(dto);

        // Mostrando o boleto gerado na tela.
        mostrarBoletoNaTela(boletoPdf);

        return boletoPdf;
    }

    private File emitirBoletoPorBanco(EmissorBoletoDto dto) throws ParseException {
        BancosSuportados banco = BancosSuportados.valueOf(dto.getBancoCedente());

        switch (banco) {
            case BANCO_DO_BRASIL:
                return emitirBancoDoBrasil(dto);
            case BANCO_DO_NORDESTE_DO_BRASIL:
                return emitirBancoDoNordeste(dto);
            case CAIXA_ECONOMICA_FEDERAL:
                return emitirCaixaEconomica(dto);
            case BANCO_BRADESCO:
                return emitirBradesco(dto);
            case BANCO_ITAU:
                return emitirItau(dto);
            case BANCO_SAFRA:
                return emitirSafra(dto);
            case BANCO_SANTANDER:
                return emitirSantander(dto);
            default:
                throw new IllegalArgumentException("Banco não suportado: " + banco);
        }
    }

    public List<EmissorBoleto> findAllBoletosPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return emissorBoletoRepository.findAllBoletosPeriodo(dataInicial, dataFinal);
    }

    public EmissorBoleto findById(Long id) {
        Optional<EmissorBoleto> objBoleto = emissorBoletoRepository.findById(id);
        return objBoleto.orElseThrow(() -> new ObjectNotFoundException("Boleto não encontrado"));
    }

    public EmissorBoleto create(@Valid EmissorBoletoDto emissorBoletoDto) {
        emissorBoletoDto.setId(null);
        String dataAtual = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
        emissorBoletoDto.setEmissao(LocalDate.parse(dataAtual));
        EmissorBoleto emissorBoleto = new EmissorBoleto(emissorBoletoDto);
        return emissorBoletoRepository.save(emissorBoleto);
    }

    private static void mostrarBoletoNaTela(File arquivoBoleto) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (arquivoBoleto.exists()) {
                    desktop.open(arquivoBoleto);
                }
            }
        } catch (IOException e) {
            logger.error("Erro ao abrir o boleto: " + e.getMessage(), e);
        }
    }

    private File emitirBoletoGenerico(EmissorBoletoDto dto, BancosSuportados banco) throws ParseException {
        // Configuração comum a todos os bancos
        Cedente cedente = new Cedente(dto.getCedente(), dto.getDocumentoCedente());
        Sacado sacado = new Sacado(dto.getSacado(), dto.getDocumentoSacado());

        // Configura endereços (exemplo simplificado)
        Endereco enderecoSac = configurarEndereco(dto, true);
        sacado.addEndereco(enderecoSac);

        SacadorAvalista sacadorAvalista = new SacadorAvalista(dto.getCedente(), dto.getDocumentoCedente());
        Endereco enderecoSacAval = configurarEndereco(dto, false);
        sacadorAvalista.addEndereco(enderecoSacAval);

        // Configura conta bancária específica
        ContaBancaria contaBancaria = new ContaBancaria(banco.create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(Integer.parseInt(dto.getConta()), dto.getDigitoConta()));
        contaBancaria.setCarteira(new Carteira(Integer.parseInt(dto.getCarteira())));
        contaBancaria.setAgencia(new Agencia(Integer.parseInt(dto.getAgencia(), Integer.parseInt(dto.getDigitoAgencia()))));

        // Configura título
        Titulo titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
        configurarTitulo(titulo, dto);

        // Cria e configura boleto
        Boleto boleto = new Boleto(titulo);
        boleto.setLocalPagamento("Pagável preferencialmente no banco " + dto.getBancoCedente() + " ou em qualquer Banco até o Vencimento");
        boleto.setInstrucaoAoSacado(dto.getDemonstrativo());
        boleto.setInstrucao1(dto.getInstrucao());

        // Gera PDF
        BoletoViewer boletoViewer = new BoletoViewer(boleto);
        String nomeArquivo = "Boleto_" + dto.getSacado() + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";
        return boletoViewer.getPdfAsFile(System.getProperty("user.home") + "/Downloads/" + nomeArquivo);
    }

    private Endereco configurarEndereco(EmissorBoletoDto dto, boolean isSacado) {
        Endereco endereco = new Endereco();
        endereco.setUF(UnidadeFederativa.valueOfSigla(isSacado ? dto.getUfSacado() : dto.getUfCedente()));
        endereco.setLocalidade(isSacado ? dto.getCidadeSacado() : dto.getCidadeCedente());
        endereco.setCep(new CEP(isSacado ? dto.getCepSacado() : dto.getCepCedente()));
        endereco.setBairro(isSacado ? dto.getBairroSacado() : dto.getBairroCedente());
        endereco.setLogradouro(isSacado ? dto.getLogradouroSacado() : dto.getLogradouroCedente());
        endereco.setNumero("");
        return endereco;
    }

    private void configurarTitulo(Titulo titulo, EmissorBoletoDto dto) throws ParseException {
        titulo.setNumeroDoDocumento(dto.getNumeroDocumento());
        titulo.setNossoNumero(dto.getNossoNumero());
        titulo.setDigitoDoNossoNumero(dto.getDigitoNossoNumero());
        titulo.setValor(new BigDecimal(String.valueOf(dto.getValorBoleto())));
        titulo.setDataDoDocumento(new Date());
        titulo.setDataDoVencimento(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(dto.getDataVencimento())));
        titulo.setTipoDeDocumento(TipoDeTitulo.valueOf(dto.getEspecieDocumento()));
        titulo.setTipoDeMoeda(TipoDeMoeda.valueOf(dto.getEspecieMoeda()));
        titulo.setAceite(Titulo.Aceite.valueOf(dto.getAceite()));
        titulo.setDesconto(BigDecimal.ZERO);
        titulo.setDeducao(BigDecimal.ZERO);
        titulo.setMora(BigDecimal.ZERO);
        titulo.setAcrecimo(BigDecimal.ZERO);
        titulo.setValorCobrado(BigDecimal.ZERO);
    }

    private void calcularNossoNumero(EmissorBoletoDto dto) throws ParseException {
        try {
            // Converte a data de vencimento para Date
            Date dataVencimento = new SimpleDateFormat("yyyy-MM-dd").parse(dto.getDataVencimento().toString());

            // Calcula o nosso número
            String nossoNumeroCompleto = nossoNumeroCalculator.calcularNossoNumeroCompleto(
                    dto.getNumeroDocumento(),
                    dataVencimento,
                    dto.getBancoCedente(),
                    dto.getConvenio(),
                    dto.getCarteira(),
                    dto.getAgencia(),
                    dto.getConta(),
                    dto.getDigitoConta(),
                    "SEQUENCIAL"
            );

            // Separa o número do dígito verificador (dependendo do banco)
            if (nossoNumeroCompleto.length() > 1) {
                dto.setNossoNumero(nossoNumeroCompleto.substring(0, nossoNumeroCompleto.length() - 1));
                dto.setDigitoNossoNumero(nossoNumeroCompleto.substring(nossoNumeroCompleto.length() - 1));
            } else {
                dto.setNossoNumero(nossoNumeroCompleto);
                dto.setDigitoNossoNumero("");
            }
        } catch (Exception e) {
            logger.error("Erro ao calcular nosso número", e);
            throw new RuntimeException("Erro ao calcular nosso número", e);
        }
    }

    private File emitirBancoDoBrasil(EmissorBoletoDto dto) throws ParseException {
        return emitirBoletoGenerico(dto, BancosSuportados.BANCO_DO_BRASIL);
    }

    private File emitirBancoDoNordeste(EmissorBoletoDto dto) throws ParseException {
        return emitirBoletoGenerico(dto, BancosSuportados.BANCO_DO_NORDESTE_DO_BRASIL);
    }

    private File emitirCaixaEconomica(EmissorBoletoDto dto) throws ParseException {
        return emitirBoletoGenerico(dto, BancosSuportados.CAIXA_ECONOMICA_FEDERAL);
    }

    private File emitirBradesco(EmissorBoletoDto dto) throws ParseException {
        return emitirBoletoGenerico(dto, BancosSuportados.BANCO_BRADESCO);
    }

    private File emitirItau(EmissorBoletoDto dto) throws ParseException {
        return emitirBoletoGenerico(dto, BancosSuportados.BANCO_ITAU);
    }

    private File emitirSafra(EmissorBoletoDto dto) throws ParseException {
        return emitirBoletoGenerico(dto, BancosSuportados.BANCO_SAFRA);
    }

    private File emitirSantander(EmissorBoletoDto dto) throws ParseException {
        return emitirBoletoGenerico(dto, BancosSuportados.BANCO_SANTANDER);
    }

    public String geradorNumeroDocumento() {

        // Obtém a data/hora atual
        LocalDateTime agora = LocalDateTime.now();

        // Formata no padrão hhmmddMMyy (24h, minuto, dia, mês, ano com 2 dígitos)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmddMMyy");
        return agora.format(formatter);
    }

}
