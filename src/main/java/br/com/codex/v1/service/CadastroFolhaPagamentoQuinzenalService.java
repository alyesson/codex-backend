package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.CadastroFolhaPagamentoQuinzenalDto;
import br.com.codex.v1.domain.dto.CadastroFolhaPagamentoQuinzenalEventosDto;
import br.com.codex.v1.domain.repository.CadastroFolhaPagamentoQuinzenalEventosRepository;
import br.com.codex.v1.domain.repository.CadastroFolhaPagamentoQuinzenalRepository;
import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenal;
import br.com.codex.v1.domain.rh.CadastroFolhaPagamentoQuinzenalEventos;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroFolhaPagamentoQuinzenalService {

    @Autowired
    private CadastroFolhaPagamentoQuinzenalRepository cadastroFolhaPagamentoQuinzenalRepository;

    @Autowired
    private CadastroFolhaPagamentoQuinzenalEventosRepository cadastroFolhaPagamentoQuinzenalEventosRepository;

    public CadastroFolhaPagamentoQuinzenal create (CadastroFolhaPagamentoQuinzenalDto cadastroFolhaPagamentoQuinzenalDto){

        cadastroFolhaPagamentoQuinzenalDto.setId(null);
        CadastroFolhaPagamentoQuinzenal folhaPagamentoQuinzenal = new CadastroFolhaPagamentoQuinzenal(cadastroFolhaPagamentoQuinzenalDto);
        folhaPagamentoQuinzenal = cadastroFolhaPagamentoQuinzenalRepository.save(folhaPagamentoQuinzenal);

        //Salvando eventos
        for(CadastroFolhaPagamentoQuinzenalEventosDto eventosDto : cadastroFolhaPagamentoQuinzenalDto.getEventos()) {
            CadastroFolhaPagamentoQuinzenalEventos eventos = new CadastroFolhaPagamentoQuinzenalEventos();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setCadastroFolhaPagamentoQuinzenal(folhaPagamentoQuinzenal);
            cadastroFolhaPagamentoQuinzenalEventosRepository.save(eventos);
        }
        return folhaPagamentoQuinzenal;
    }

    public CadastroFolhaPagamentoQuinzenal update(CadastroFolhaPagamentoQuinzenalDto cadastroFolhaPagamentoQuinzenalDto) {
        CadastroFolhaPagamentoQuinzenal existingFolha = findById(cadastroFolhaPagamentoQuinzenalDto.getId());

        // Atualiza os dados principais
        CadastroFolhaPagamentoQuinzenal folhaPagamentoQuinzenal = new CadastroFolhaPagamentoQuinzenal(cadastroFolhaPagamentoQuinzenalDto);
        folhaPagamentoQuinzenal = cadastroFolhaPagamentoQuinzenalRepository.save(folhaPagamentoQuinzenal);

        // Remove eventos antigos
        cadastroFolhaPagamentoQuinzenalEventosRepository.deleteByCadastroFolhaPagamentoQuinzenalId(folhaPagamentoQuinzenal.getId());

        // Salva os novos eventos
        for (CadastroFolhaPagamentoQuinzenalEventosDto eventosDto : cadastroFolhaPagamentoQuinzenalDto.getEventos()) {
            CadastroFolhaPagamentoQuinzenalEventos eventos = new CadastroFolhaPagamentoQuinzenalEventos();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setCadastroFolhaPagamentoQuinzenal(folhaPagamentoQuinzenal);
            cadastroFolhaPagamentoQuinzenalEventosRepository.save(eventos);
        }
        return folhaPagamentoQuinzenal;
    }

    public void delete(Long id) {
        CadastroFolhaPagamentoQuinzenal folha = findById(id);

        // Remove os eventos primeiro (devido à constraint de chave estrangeira)
        cadastroFolhaPagamentoQuinzenalEventosRepository.deleteByCadastroFolhaPagamentoQuinzenalId(id);

        // Remove o cadastro principal
        cadastroFolhaPagamentoQuinzenalRepository.deleteById(id);
    }

    public CadastroFolhaPagamentoQuinzenal findById(Long id) {
        Optional<CadastroFolhaPagamentoQuinzenal> objFolhaQuinzenal = cadastroFolhaPagamentoQuinzenalRepository.findById(id);
        return objFolhaQuinzenal.orElseThrow(() -> new ObjectNotFoundException("Cadastro de folha de pagamento quinzenal não encontrado"));
    }

    public List<CadastroFolhaPagamentoQuinzenalEventos> findAllEventosByCadastroFolhaPagamentoQuinzenalId(Long eventoId) {
        return cadastroFolhaPagamentoQuinzenalEventosRepository.findAllEventosByCadastroFolhaPagamentoQuinzenalId(eventoId);
    }

    public List<CadastroFolhaPagamentoQuinzenal> findAll() {
        return cadastroFolhaPagamentoQuinzenalRepository.findAll();
    }
}
