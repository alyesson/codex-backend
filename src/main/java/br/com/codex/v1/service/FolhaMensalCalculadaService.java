package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.*;
import br.com.codex.v1.domain.dto.FolhaMensalCalculadaDto;
import br.com.codex.v1.domain.repository.FolhaMensalCalculadaRepository;
import br.com.codex.v1.domain.repository.FolhaMensalEventosCalculadaRepository;
import br.com.codex.v1.domain.rh.*;
import br.com.codex.v1.domain.rh.FolhaMensalCalculada;
import br.com.codex.v1.domain.rh.FolhaMensalCalculada;
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
public class FolhaMensalCalculadaService {

    @Autowired
    private FolhaMensalCalculadaRepository folhaMensalCalculadaRepository;

    @Autowired
    private FolhaMensalEventosCalculadaRepository folhaMensalEventosCalculadaRepository;

    @Autowired
    private CalculoDaFolhaProventosService calculoDaFolhaProventosService;

    @Autowired
    private CalculoDaFolhaDescontosService calculoDaFolhaDescontosService;

    public FolhaMensalCalculada create (FolhaMensalCalculadaDto folhaMensalCalculadaDto){

        folhaMensalCalculadaDto.setId(null);
        FolhaMensalCalculada folhaMensalCalculada = new FolhaMensalCalculada(folhaMensalCalculadaDto);
        folhaMensalCalculada = folhaMensalCalculadaRepository.save(folhaMensalCalculada);

        //Salvando eventos
        for(FolhaMensalEventosCalculadaDto eventosDto : folhaMensalCalculadaDto.getEventos()){
            FolhaMensalEventosCalculada eventos = new FolhaMensalEventosCalculada();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setFolhaMensalCalculada(folhaMensalCalculada);
            folhaMensalEventosCalculadaRepository.save(eventos);
        }
        return folhaMensalCalculada;
    }

    @Transactional
    public List<FolhaMensalCalculada> processarLote(List<FolhaMensalCalculadaDto> folhasDto) {
        List<FolhaMensalCalculada> folhasProcessadas = new ArrayList<>();

        for (FolhaMensalCalculadaDto folhaDto : folhasDto) {
            FolhaMensalCalculada folhaProcessada = processarFolhaIndividual(folhaDto);
            folhasProcessadas.add(folhaProcessada);
        }

        return folhasProcessadas;
    }

    private FolhaMensalCalculada processarFolhaIndividual(FolhaMensalCalculadaDto folhaDto) {
        // Processa cada evento da folha
        List<FolhaMensalEventosCalculadaDto> eventosProcessados = new ArrayList<>();

        for (FolhaMensalEventosCalculadaDto eventoDto : folhaDto.getEventos()) {
            FolhaMensalEventosCalculadaDto eventoProcessado = processarEvento(folhaDto.getMatriculaColaborador(), eventoDto);
            eventosProcessados.add(eventoProcessado);
        }

        // Atualiza a folha com os eventos processados
        folhaDto.setEventos(eventosProcessados);

        // Calcula totais
        calcularTotais(folhaDto);

        // Salva no banco
        return create(folhaDto);
    }

    private FolhaMensalEventosCalculadaDto processarEvento(String matricula, FolhaMensalEventosCalculadaDto eventoDto) {
        // Configura o cálculo
        calculoDaFolhaProventosService.setNumeroMatricula(matricula);

        // Processa o evento específico
        Integer codigoEvento = eventoDto.getCodigoEvento();
        Map<String, BigDecimal> resultadoProv = calculoDaFolhaProventosService.escolheEventos(codigoEvento);
        Map<String, BigDecimal> resultadoDesc = calculoDaFolhaDescontosService.escolheEventos(codigoEvento);


        // Atualiza o evento proventos com os resultados
        if (resultadoProv != null) {
            BigDecimal referencia = resultadoProv.get("referencia");
            BigDecimal vencimentos = resultadoProv.get("vencimentos");
            BigDecimal descontos = resultadoProv.get("descontos");

            eventoDto.setReferencia(referencia != null ? referencia : BigDecimal.valueOf(0));
            eventoDto.setVencimentos(vencimentos != null ? vencimentos : BigDecimal.ZERO);
            eventoDto.setDescontos(descontos != null ? descontos : BigDecimal.ZERO);
        }

        // Atualiza o evento desconto com os resultados
        if (resultadoDesc != null) {
            BigDecimal referencia = resultadoDesc.get("referencia");
            BigDecimal vencimentos = resultadoDesc.get("vencimentos");
            BigDecimal descontos = resultadoDesc.get("descontos");

            eventoDto.setReferencia(referencia != null ? referencia : BigDecimal.valueOf(0));
            eventoDto.setVencimentos(vencimentos != null ? vencimentos : BigDecimal.ZERO);
            eventoDto.setDescontos(descontos != null ? descontos : BigDecimal.ZERO);
        }

        return eventoDto;
    }

    private void calcularTotais(FolhaMensalCalculadaDto folhaDto) {
        BigDecimal totalVencimentos = BigDecimal.ZERO;
        BigDecimal totalDescontos = BigDecimal.ZERO;

        for (FolhaMensalEventosCalculadaDto evento : folhaDto.getEventos()) {
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
        FolhaMensalCalculada folha = findById(id);

        // Remove os eventos primeiro (devido à constraint de chave estrangeira)
        folhaMensalEventosCalculadaRepository.deleteByFolhaMensalCalculadaId(id);

        // Remove o cadastro principal
        folhaMensalCalculadaRepository.deleteById(id);
    }

    public FolhaMensalCalculada findById(Long id) {
        Optional<FolhaMensalCalculada> objFolhaMensalCalculada = folhaMensalCalculadaRepository.findById(id);
        return objFolhaMensalCalculada.orElseThrow(() -> new ObjectNotFoundException("Cadastro de folha de pagamento quinzenal não encontrado"));
    }

    public List<FolhaMensalEventosCalculada> findAllEventosByFolhaMensalCalculadaId(Long eventoId) {
        return folhaMensalEventosCalculadaRepository.findAllEventosByFolhaMensalCalculadaId(eventoId);
    }

    public List<FolhaMensalCalculada> findAll() {
        return folhaMensalCalculadaRepository.findAll();
    }
}
