package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.FolhaQuinzenalDto;
import br.com.codex.v1.domain.dto.FolhaQuinzenalEventosDto;
import br.com.codex.v1.domain.repository.FolhaQuinzenalEventosRepository;
import br.com.codex.v1.domain.repository.FolhaQuinzenalRepository;
import br.com.codex.v1.domain.rh.FolhaQuinzenal;
import br.com.codex.v1.domain.rh.FolhaQuinzenalEventos;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FolhaQuinzenalService {

    @Autowired
    private FolhaQuinzenalRepository folhaQuinzenalRepository;

    @Autowired
    private FolhaQuinzenalEventosRepository folhaQuinzenalEventosRepository;

    public FolhaQuinzenal create (FolhaQuinzenalDto folhaQuinzenalDto){

        folhaQuinzenalDto.setId(null);
        FolhaQuinzenal folhaQuinzenal = new FolhaQuinzenal(folhaQuinzenalDto);
        folhaQuinzenal = folhaQuinzenalRepository.save(folhaQuinzenal);

        //Salvando eventos
        for(FolhaQuinzenalEventosDto eventosDto : folhaQuinzenalDto.getEventos()) {
            FolhaQuinzenalEventos eventos = new FolhaQuinzenalEventos();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setFolhaQuinzenal(folhaQuinzenal);
            folhaQuinzenalEventosRepository.save(eventos);
        }
        return folhaQuinzenal;
    }

    public FolhaQuinzenal update(Long id, FolhaQuinzenalDto folhaQuinzenalDto) {
        FolhaQuinzenal existingFolha = findById(id);

        // Atualiza os dados principais
        FolhaQuinzenal folhaQuinzenal = new FolhaQuinzenal(folhaQuinzenalDto);
        folhaQuinzenal = folhaQuinzenalRepository.save(folhaQuinzenal);

        // Remove eventos antigos
        folhaQuinzenalEventosRepository.deleteByFolhaQuinzenalId(folhaQuinzenal.getId());

        // Salva os novos eventos
        for (FolhaQuinzenalEventosDto eventosDto : folhaQuinzenalDto.getEventos()) {
            FolhaQuinzenalEventos eventos = new FolhaQuinzenalEventos();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setFolhaQuinzenal(folhaQuinzenal);
            folhaQuinzenalEventosRepository.save(eventos);
        }
        return folhaQuinzenal;
    }

    public void delete(Long id) {
        FolhaQuinzenal folha = findById(id);

        // Remove os eventos primeiro (devido à constraint de chave estrangeira)
        folhaQuinzenalEventosRepository.deleteByFolhaQuinzenalId(id);

        // Remove o cadastro principal
        folhaQuinzenalRepository.deleteById(id);
    }

    public FolhaQuinzenal findById(Long id) {
        Optional<FolhaQuinzenal> objFolhaQuinzenal = folhaQuinzenalRepository.findById(id);
        return objFolhaQuinzenal.orElseThrow(() -> new ObjectNotFoundException("Cadastro de folha de pagamento quinzenal não encontrado"));
    }

    public List<FolhaQuinzenalEventos> findAllEventosByFolhaQuinzenalId(Long eventoId) {
        return folhaQuinzenalEventosRepository.findAllEventosByFolhaQuinzenalId(eventoId);
    }

    public List<FolhaQuinzenal> findAll() {
        return folhaQuinzenalRepository.findAll();
    }
}
