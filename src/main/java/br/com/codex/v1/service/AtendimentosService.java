package br.com.codex.v1.service;


import br.com.codex.v1.domain.dto.AtendimentosDto;
import br.com.codex.v1.domain.enums.Prioridade;
import br.com.codex.v1.domain.enums.Situacao;
import br.com.codex.v1.domain.repository.AtendimentosRepository;
import br.com.codex.v1.domain.ti.Atendimentos;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AtendimentosService {

    String horaFormatada;

    @Autowired
    private AtendimentosRepository atendimentosRepository;

    @Autowired
    private EnviaEmailChamadoConcluidoService enviaEmailChamadoConcluidoService;

    public Atendimentos create(AtendimentosDto atendimentosDto) {
        return atendimentosRepository.save(novoAtendimento(atendimentosDto));
    }

    public Atendimentos update(Integer id, AtendimentosDto atendimentosDto) {
        atendimentosDto.setId(id);
        Atendimentos obj = findById(id);
        obj = novoAtendimento(atendimentosDto);
        return atendimentosRepository.save(obj);
    }

    public Atendimentos findById(Integer id) {
        Optional<Atendimentos> obj = atendimentosRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Chamado não encontrado"));
    }

    public void delete(Integer id) {
        atendimentosRepository.deleteById(id);
    }

    public List<Atendimentos> findAllYearAndMonth(Integer ano, Integer mes, String solicitante) {
        return atendimentosRepository.findAllYearAndMonth(ano, mes, solicitante);
    }

    private Atendimentos novoAtendimento(AtendimentosDto atendimentosDto) {

        LocalDate dataAtual = LocalDate.now();
        Atendimentos atendimentos = new Atendimentos();

        if (atendimentosDto.getId() != null) {
            atendimentos.setId(atendimentosDto.getId());
              // Não altera a data de abertura durante a atualização
            atendimentos.setDataAbertura(atendimentosDto.getDataAbertura() != null ?
                    atendimentosDto.getDataAbertura() : atendimentosDto.getDataAbertura());
            atendimentos.setHoraInicio(atendimentosDto.getHoraInicio() != null ?
                    atendimentosDto.getHoraInicio() : atendimentosDto.getHoraInicio());
        }else{
            LocalDateTime horAtual = LocalDateTime.now();
            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
            String horaFormatada = horAtual.format(formatterHora);

            atendimentos.setDataAbertura(dataAtual);
            atendimentos.setHoraInicio(horaFormatada);
            atendimentos.setSituacao(Situacao.toEnum(0));
        }

        if(atendimentosDto.getSituacao().equals(3)){
            LocalDateTime horAtual = LocalDateTime.now();
            DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
            String horaFormatada = horAtual.format(formatterHora);

            LocalDate dataAbertura = atendimentosDto.getDataAbertura();
            LocalDate dataFechamento = dataAtual;
            long diasAtuacao = ChronoUnit.DAYS.between(dataAbertura, dataFechamento);
            atendimentos.setDiasAtuacao(String.valueOf(diasAtuacao));
            atendimentos.setDataFechamento(dataAtual);
            atendimentos.setHoraFim(horaFormatada);

            //Envia e-mail quando chamado é fechado
            String enviaEmailConclusao = enviaEmailChamadoConcluidoService.sendSimpleMail(atendimentosDto);

            // Retornar o resultado do envio de e-mail
            System.out.println(enviaEmailConclusao); // Para log ou monitoramento
        }

        atendimentos.setSolicitante(atendimentosDto.getSolicitante());
        atendimentos.setEmail(atendimentosDto.getEmail());
        atendimentos.setTelefone(atendimentosDto.getTelefone());
        atendimentos.setDepartamento(atendimentosDto.getDepartamento());
        atendimentos.setTitulo(atendimentosDto.getTitulo());
        atendimentos.setProblema(atendimentosDto.getProblema());
        atendimentos.setCategoria(atendimentosDto.getCategoria());
        atendimentos.setTipo(atendimentosDto.getTipo());
        atendimentos.setPrioridade(Prioridade.toEnum(atendimentosDto.getPrioridade()));
        atendimentos.setResolucao(atendimentosDto.getResolucao());
        atendimentos.setImagem(atendimentosDto.getImagem());
        atendimentos.setNomeTecnico(atendimentosDto.getNomeTecnico());
        atendimentos.setSituacao(Situacao.toEnum(atendimentosDto.getSituacao()));

        return atendimentos;
    }

    public List<Atendimentos> findAllYear(Integer ano) {
        return atendimentosRepository.findAllYear(ano);
    }

    public List<Atendimentos> findAllAtendimentosPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return atendimentosRepository.findAllAtendimentosPeriodo(dataInicial, dataFinal);
    }

    public int somaAtendimentosAbertos() {
        int quantidadeAberto = atendimentosRepository.countBySituacao(Situacao.toEnum(0));
        return quantidadeAberto;
    }

    public int somaAtendimentosEmAndamento() {
        int quantidadeEmAndamento = atendimentosRepository.countBySituacao(Situacao.toEnum(2));
        return quantidadeEmAndamento;
    }
}
