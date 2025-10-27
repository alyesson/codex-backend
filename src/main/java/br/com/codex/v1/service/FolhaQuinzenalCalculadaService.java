package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.FolhaQuinzenalCalculadaDto;
import br.com.codex.v1.domain.dto.FolhaQuinzenalEventosCalculadaDto;
import br.com.codex.v1.domain.repository.FolhaQuinzenalCalculadaRepository;
import br.com.codex.v1.domain.repository.FolhaQuinzenalEventosCalculadaRepository;
import br.com.codex.v1.domain.rh.FolhaQuinzenalCalculada;
import br.com.codex.v1.domain.rh.FolhaQuinzenalEventosCalculada;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FolhaQuinzenalCalculadaService {

    @Autowired
    private FolhaQuinzenalCalculadaRepository folhaQuinzenalCalculadaRepository;

    @Autowired
    private FolhaQuinzenalEventosCalculadaRepository folhaQuinzenalEventosCalculadaRepository;

    @Autowired
    private CalculoDaFolhaProventosService calculoDaFolhaProventosService;

    @Autowired
    private CalculoDaFolhaDescontosService calculoDaFolhaDescontosService;

    public FolhaQuinzenalCalculada create (FolhaQuinzenalCalculadaDto folhaQuinzenalCalculadaDto){

        folhaQuinzenalCalculadaDto.setId(null);
        FolhaQuinzenalCalculada folhaQuinzenalCalculada = new FolhaQuinzenalCalculada(folhaQuinzenalCalculadaDto);
        folhaQuinzenalCalculada = folhaQuinzenalCalculadaRepository.save(folhaQuinzenalCalculada);

        //Salvando eventos
        for(FolhaQuinzenalEventosCalculadaDto eventosDto : folhaQuinzenalCalculadaDto.getEventos()) {
            FolhaQuinzenalEventosCalculada eventos = new FolhaQuinzenalEventosCalculada();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setFolhaQuinzenalCalculada(folhaQuinzenalCalculada);
            folhaQuinzenalEventosCalculadaRepository.save(eventos);
        }
        return folhaQuinzenalCalculada;
    }

    @Transactional
    public List<FolhaQuinzenalCalculada> processarLote(List<FolhaQuinzenalCalculadaDto> folhasDto) {
        List<FolhaQuinzenalCalculada> folhasProcessadas = new ArrayList<>();

        for (FolhaQuinzenalCalculadaDto folhaDto : folhasDto) {
            FolhaQuinzenalCalculada folhaProcessada = processarFolhaIndividual(folhaDto);
            folhasProcessadas.add(folhaProcessada);
        }

        return folhasProcessadas;
    }

    private FolhaQuinzenalCalculada processarFolhaIndividual(FolhaQuinzenalCalculadaDto folhaDto) {
        // Processa cada evento da folha
        List<FolhaQuinzenalEventosCalculadaDto> eventosProcessados = new ArrayList<>();

        for (FolhaQuinzenalEventosCalculadaDto eventoDto : folhaDto.getEventos()) {
            FolhaQuinzenalEventosCalculadaDto eventoProcessado = processarEvento(folhaDto.getMatriculaColaborador(), eventoDto, folhaDto.getTipoSalario());
            eventosProcessados.add(eventoProcessado);
        }

        // Atualiza a folha com os eventos processados
        folhaDto.setEventos(eventosProcessados);

        // Calcula totais
        calcularTotais(folhaDto);

        // Salva no banco
        return create(folhaDto);
    }

    private FolhaQuinzenalEventosCalculadaDto processarEvento(String matricula, FolhaQuinzenalEventosCalculadaDto eventoDto, String tipoSalario) {
        // Configura o cálculo
        calculoDaFolhaProventosService.setNumeroMatricula(matricula);
        calculoDaFolhaDescontosService.setNumeroMatricula(matricula);

        calculoDaFolhaProventosService.setTipoSalario(tipoSalario);
        calculoDaFolhaDescontosService.setTipoSalario(tipoSalario);

        // Processa o evento específico
        Integer codigoEvento = eventoDto.getCodigoEvento();
        Map<String, BigDecimal> resultadoProv = calculoDaFolhaProventosService.escolheEventos(codigoEvento, tipoSalario);
        Map<String, BigDecimal> resultadoDesc = calculoDaFolhaDescontosService.escolheEventos(codigoEvento);

        BigDecimal referenciaFinal = BigDecimal.ZERO;
        BigDecimal vencimentosFinal = BigDecimal.ZERO;
        BigDecimal descontosFinal = BigDecimal.ZERO;


        // Atualiza o evento proventos com os resultados
        if (resultadoProv != null && !resultadoProv.isEmpty()) {
            referenciaFinal = resultadoProv.get("referencia") != null ? resultadoProv.get("referencia") : BigDecimal.ZERO;
            vencimentosFinal = resultadoProv.get("vencimentos") != null ? resultadoProv.get("vencimentos") : BigDecimal.ZERO;
            descontosFinal = resultadoProv.get("descontos") != null ? resultadoProv.get("descontos") : BigDecimal.ZERO;

            System.out.println("💰 Valores do Provento - Ref: " + referenciaFinal + ", Venc: " + vencimentosFinal + ", Desc: " + descontosFinal);
        }

        // Atualiza o evento desconto com os resultados
        if (resultadoDesc != null && !resultadoDesc.isEmpty()) {
            // Para referência, usar a do desconto se a do provento for zero
            if (referenciaFinal.equals(BigDecimal.ZERO)) {
                referenciaFinal = resultadoDesc.get("referencia") != null ? resultadoDesc.get("referencia") : BigDecimal.ZERO;
            }

            // 🔥 IMPORTANTE: SOMAR os valores, não substituir!
            vencimentosFinal = vencimentosFinal.add(resultadoDesc.get("vencimentos") != null ? resultadoDesc.get("vencimentos") : BigDecimal.ZERO);
            descontosFinal = descontosFinal.add(resultadoDesc.get("descontos") != null ? resultadoDesc.get("descontos") : BigDecimal.ZERO);

            System.out.println("💸 Valores do Desconto - Ref: " + referenciaFinal + ", Venc: " + vencimentosFinal + ", Desc: " + descontosFinal);
        }

        eventoDto.setReferencia(referenciaFinal.toString()); // Converter para String se necessário
        eventoDto.setVencimentos(vencimentosFinal);
        eventoDto.setDescontos(descontosFinal);

        System.out.println("✅ Evento final - Ref: " + eventoDto.getReferencia() +
                ", Venc: " + eventoDto.getVencimentos() +
                ", Desc: " + eventoDto.getDescontos());

        return eventoDto;
    }

    private void calcularTotais(FolhaQuinzenalCalculadaDto folhaDto) {
        BigDecimal totalVencimentos = BigDecimal.ZERO;
        BigDecimal totalDescontos = BigDecimal.ZERO;

        for (FolhaQuinzenalEventosCalculadaDto evento : folhaDto.getEventos()) {
            if (evento.getVencimentos() != null) {
                totalVencimentos = totalVencimentos.add(evento.getVencimentos());
            }
            if (evento.getDescontos() != null) {
                totalDescontos = totalDescontos.add(evento.getDescontos());
            }
        }

        folhaDto.setTotalVencimentos(totalVencimentos);
        folhaDto.setTotalDescontos(totalDescontos);
        folhaDto.setValorLiquido(totalVencimentos.subtract(totalDescontos));
    }

    @Transactional
    public void delete(Long id) {
        FolhaQuinzenalCalculada folha = findById(id);

        // Remove os eventos primeiro (devido à constraint de chave estrangeira)
        folhaQuinzenalEventosCalculadaRepository.deleteByFolhaQuinzenalCalculadaId(id);

        // Remove o cadastro principal
        folhaQuinzenalCalculadaRepository.deleteById(id);
    }

    public FolhaQuinzenalCalculada findById(Long id) {
        Optional<FolhaQuinzenalCalculada> objFolhaQuinzenalCalculada = folhaQuinzenalCalculadaRepository.findById(id);
        return objFolhaQuinzenalCalculada.orElseThrow(() -> new ObjectNotFoundException("Cadastro de folha de pagamento quinzenal não encontrado"));
    }

    public List<FolhaQuinzenalEventosCalculada> findAllEventosByFolhaQuinzenalCalculadaId(Long eventoId) {
        return folhaQuinzenalEventosCalculadaRepository.findAllEventosByFolhaQuinzenalCalculadaId(eventoId);
    }

    public List<FolhaQuinzenalCalculada> findAll() {
        return folhaQuinzenalCalculadaRepository.findAll();
    }
}
