package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.FolhaFeriasDto;
import br.com.codex.v1.domain.dto.FolhaFeriasEventosDto;
import br.com.codex.v1.domain.repository.FolhaFeriasRepository;
import br.com.codex.v1.domain.repository.FolhaFeriasEventosRepository;
import br.com.codex.v1.domain.rh.FolhaFerias;
import br.com.codex.v1.domain.rh.FolhaFeriasEventos;
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
public class FolhaFeriasCalculadaService {

    @Autowired
    private FolhaFeriasRepository folhaFeriasRepository;

    @Autowired
    private FolhaFeriasEventosRepository folhaFeriasEventosRepository;

    @Autowired
    private CalculoDaFolhaFeriasService calculoDaFolhaFeriasService;

    @Autowired
    private CalculoDaFolhaDescontosService calculoDaFolhaDescontosService;

    public FolhaFerias create (FolhaFeriasDto folhaFeriasDto){

        folhaFeriasDto.setId(null);
        FolhaFerias folhaFerias = new FolhaFerias(folhaFeriasDto);
        folhaFerias = folhaFeriasRepository.save(folhaFerias);

        //Salvando eventos
        for(FolhaFeriasEventosDto eventosDto : folhaFeriasDto.getEventos()){
            FolhaFeriasEventos eventos = new FolhaFeriasEventos();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setFolhaFerias(folhaFerias);
            folhaFeriasEventosRepository.save(eventos);
        }
        return folhaFerias;
    }

    @Transactional
    public List<FolhaFerias> processarLote(List<FolhaFeriasDto> folhasDto) {
        List<FolhaFerias> folhasProcessadas = new ArrayList<>();

        for (FolhaFeriasDto folhaDto : folhasDto) {
            FolhaFerias folhaProcessada = processarFolhaIndividual(folhaDto);
            folhasProcessadas.add(folhaProcessada);
        }
        return folhasProcessadas;
    }

    private FolhaFerias processarFolhaIndividual(FolhaFeriasDto folhaDto) {
        // Processa cada evento da folha
        List<FolhaFeriasEventosDto> eventosProcessados = new ArrayList<>();

        for (FolhaFeriasEventosDto eventoDto : folhaDto.getEventos()) {
            FolhaFeriasEventosDto eventoProcessado = processarEvento(folhaDto.getNumeroMatricula(), eventoDto);
            eventosProcessados.add(eventoProcessado);
        }

        folhaDto.setEventos(eventosProcessados);
        calcularTotais(folhaDto);
        return create(folhaDto);
    }

    private FolhaFeriasEventosDto processarEvento(String matricula, FolhaFeriasEventosDto eventoDto) {
        // Configura o cálculo
        calculoDaFolhaFeriasService.setNumeroMatricula(matricula);

        // Processa o evento específico
        Integer codigoEvento = eventoDto.getCodigoEvento();
        Map<String, BigDecimal> resultadoProv = calculoDaFolhaFeriasService.escolheEventos(codigoEvento);
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

    private void calcularTotais(FolhaFeriasDto folhaDto) {
        BigDecimal totalVencimentos = BigDecimal.ZERO;
        BigDecimal totalDescontos = BigDecimal.ZERO;

        for (FolhaFeriasEventosDto evento : folhaDto.getEventos()) {
            if (evento.getVencimentos() != null) {
                totalVencimentos = totalVencimentos.add(evento.getVencimentos());
            }
            if (evento.getDescontos() != null) {
                totalDescontos = totalDescontos.add(evento.getDescontos());
            }
        }

        folhaDto.setTotalBrutReceber(totalVencimentos);
        folhaDto.setTotalDeDescontos(totalDescontos);
        folhaDto.setTotalLiquidoReceber(totalVencimentos.subtract(totalDescontos));
    }

    @Transactional
    public void delete(Long id) {
        FolhaFerias folha = findById(id);

        // Remove os eventos primeiro (devido à constraint de chave estrangeira)
        folhaFeriasEventosRepository.deleteByFolhaFeriasId(id);

        // Remove o cadastro principal
        folhaFeriasRepository.deleteById(id);
    }

    public FolhaFerias findById(Long id) {
        Optional<FolhaFerias> objCalculoFerias = folhaFeriasRepository.findById(id);
        return objCalculoFerias.orElseThrow(() -> new ObjectNotFoundException("Cadastro de folha de pagamento quinzenal não encontrado"));
    }

    public List<FolhaFeriasEventos> findAllEventosByFolhaFeriasId(Long eventoId) {
        return folhaFeriasEventosRepository.findAllEventosByFolhaFeriasId(eventoId);
    }

    public List<FolhaFerias> findAll() {
        return folhaFeriasRepository.findAll();
    }
}
