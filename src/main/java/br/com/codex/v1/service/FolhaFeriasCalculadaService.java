package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.CalculoFeriasDto;
import br.com.codex.v1.domain.dto.CalculoFeriasEventosDto;
import br.com.codex.v1.domain.repository.CalculoFeriasRepository;
import br.com.codex.v1.domain.repository.CalculoFeriasEventosRepository;
import br.com.codex.v1.domain.rh.CalculoFerias;
import br.com.codex.v1.domain.rh.CalculoFeriasEventos;
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
    private CalculoFeriasRepository calculoFeriasRepository;

    @Autowired
    private CalculoFeriasEventosRepository calculoFeriasEventosRepository;

    @Autowired
    private CalculoDaFolhaFeriasService calculoDaFolhaFeriasService;

    @Autowired
    private CalculoDaFolhaDescontosService calculoDaFolhaDescontosService;

    public CalculoFerias create (CalculoFeriasDto calculoFeriasDto){

        calculoFeriasDto.setId(null);
        CalculoFerias calculoFerias = new CalculoFerias(calculoFeriasDto);
        calculoFerias = calculoFeriasRepository.save(calculoFerias);

        //Salvando eventos
        for(CalculoFeriasEventosDto eventosDto : calculoFeriasDto.getEventos()){
            CalculoFeriasEventos eventos = new CalculoFeriasEventos();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setCalculoFerias(calculoFerias);
            calculoFeriasEventosRepository.save(eventos);
        }
        return calculoFerias;
    }

    @Transactional
    public List<CalculoFerias> processarLote(List<CalculoFeriasDto> folhasDto) {
        List<CalculoFerias> folhasProcessadas = new ArrayList<>();

        for (CalculoFeriasDto folhaDto : folhasDto) {
            CalculoFerias folhaProcessada = processarFolhaIndividual(folhaDto);
            folhasProcessadas.add(folhaProcessada);
        }

        return folhasProcessadas;
    }

    private CalculoFerias processarFolhaIndividual(CalculoFeriasDto folhaDto) {
        // Processa cada evento da folha
        List<CalculoFeriasEventosDto> eventosProcessados = new ArrayList<>();

        for (CalculoFeriasEventosDto eventoDto : folhaDto.getEventos()) {
            CalculoFeriasEventosDto eventoProcessado = processarEvento(folhaDto.getNumeroMatricula(), eventoDto);
            eventosProcessados.add(eventoProcessado);
        }

        // Atualiza a folha com os eventos processados
        folhaDto.setEventos(eventosProcessados);

        // Calcula totais
        calcularTotais(folhaDto);

        // Salva no banco
        return create(folhaDto);
    }

    private CalculoFeriasEventosDto processarEvento(String matricula, CalculoFeriasEventosDto eventoDto) {
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

    private void calcularTotais(CalculoFeriasDto folhaDto) {
        BigDecimal totalVencimentos = BigDecimal.ZERO;
        BigDecimal totalDescontos = BigDecimal.ZERO;

        for (CalculoFeriasEventosDto evento : folhaDto.getEventos()) {
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
        CalculoFerias folha = findById(id);

        // Remove os eventos primeiro (devido à constraint de chave estrangeira)
        calculoFeriasEventosRepository.deleteByCalculoFeriasId(id);

        // Remove o cadastro principal
        calculoFeriasRepository.deleteById(id);
    }

    public CalculoFerias findById(Long id) {
        Optional<CalculoFerias> objCalculoFerias = calculoFeriasRepository.findById(id);
        return objCalculoFerias.orElseThrow(() -> new ObjectNotFoundException("Cadastro de folha de pagamento quinzenal não encontrado"));
    }

    public List<CalculoFeriasEventos> findAllEventosByCalculoFeriasId(Long eventoId) {
        return calculoFeriasEventosRepository.findAllEventosByCalculoFeriasId(eventoId);
    }

    public List<CalculoFerias> findAll() {
        return calculoFeriasRepository.findAll();
    }
}
