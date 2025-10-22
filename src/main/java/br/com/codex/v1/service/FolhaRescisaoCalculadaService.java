package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.FolhaRescisaoDto;
import br.com.codex.v1.domain.dto.FolhaRescisaoEventosDto;
import br.com.codex.v1.domain.repository.FolhaRescisaoRepository;
import br.com.codex.v1.domain.repository.FolhaRescisaoEventosRepository;
import br.com.codex.v1.domain.rh.FolhaRescisao;
import br.com.codex.v1.domain.rh.FolhaRescisaoEventos;
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
public class FolhaRescisaoCalculadaService {

    @Autowired
    private FolhaRescisaoRepository folhaRescisaoRepository;

    @Autowired
    private FolhaRescisaoEventosRepository folhaRescisaoEventosRepository;

    @Autowired
    private CalculoDaFolhaProventosService calculoDaFolhaProventosService;

    @Autowired
    private CalculoDaFolhaDescontosService calculoDaFolhaDescontosService;

    public FolhaRescisao create (FolhaRescisaoDto folhaRescisaoDto){

        folhaRescisaoDto.setId(null);
        FolhaRescisao folhaRescisao = new FolhaRescisao(folhaRescisaoDto);
        folhaRescisao = folhaRescisaoRepository.save(folhaRescisao);

        //Salvando eventos
        for(FolhaRescisaoEventosDto eventosDto : folhaRescisaoDto.getEventos()){
            FolhaRescisaoEventos eventos = new FolhaRescisaoEventos();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setFolhaRescisao(folhaRescisao);
            folhaRescisaoEventosRepository.save(eventos);
        }
        return folhaRescisao;
    }

    @Transactional
    public List<FolhaRescisao> processarLote(List<FolhaRescisaoDto> folhasDto) {
        List<FolhaRescisao> folhasProcessadas = new ArrayList<>();

        for (FolhaRescisaoDto folhaDto : folhasDto) {
            FolhaRescisao folhaProcessada = processarFolhaIndividual(folhaDto);
            folhasProcessadas.add(folhaProcessada);
        }

        return folhasProcessadas;
    }

    private FolhaRescisao processarFolhaIndividual(FolhaRescisaoDto folhaDto) {
        // Processa cada evento da folha
        List<FolhaRescisaoEventosDto> eventosProcessados = new ArrayList<>();

        for (FolhaRescisaoEventosDto eventoDto : folhaDto.getEventos()) {
            FolhaRescisaoEventosDto eventoProcessado = processarEvento(folhaDto.getNumeroMatricula(), eventoDto);
            eventosProcessados.add(eventoProcessado);
        }

        // Atualiza a folha com os eventos processados
        folhaDto.setEventos(eventosProcessados);

        // Calcula totais
        calcularTotais(folhaDto);

        // Salva no banco
        return create(folhaDto);
    }

    private FolhaRescisaoEventosDto processarEvento(String matricula, FolhaRescisaoEventosDto eventoDto) {
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

    private void calcularTotais(FolhaRescisaoDto folhaDto) {
        BigDecimal totalVencimentos = BigDecimal.ZERO;
        BigDecimal totalDescontos = BigDecimal.ZERO;

        for (FolhaRescisaoEventosDto evento : folhaDto.getEventos()) {
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
        FolhaRescisao folha = findById(id);

        // Remove os eventos primeiro (devido à constraint de chave estrangeira)
        folhaRescisaoEventosRepository.deleteByFolhaRescisaoId(id);

        // Remove o cadastro principal
        folhaRescisaoRepository.deleteById(id);
    }

    public FolhaRescisao findById(Long id) {
        Optional<FolhaRescisao> objFolhaRescisao = folhaRescisaoRepository.findById(id);
        return objFolhaRescisao.orElseThrow(() -> new ObjectNotFoundException("Cadastro de folha de pagamento rescisão não encontrado"));
    }

    public List<FolhaRescisaoEventos> findAllEventosByFolhaRescisaoId(Long eventoId) {
        return folhaRescisaoEventosRepository.findAllEventosByFolhaRescisaoId(eventoId);
    }

    public List<FolhaRescisao> findAll() {
        return folhaRescisaoRepository.findAll();
    }
}
