package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.FolhaMensalDto;
import br.com.codex.v1.domain.dto.FolhaMensalEventosDto;
import br.com.codex.v1.domain.repository.FolhaMensalEventosRepository;
import br.com.codex.v1.domain.repository.FolhaMensalRepository;
import br.com.codex.v1.domain.rh.FolhaMensal;
import br.com.codex.v1.domain.rh.FolhaMensalEventos;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FolhaMensalService {

    @Autowired
    private FolhaMensalRepository folhaMensalRepository;

    @Autowired
    private FolhaMensalEventosRepository folhaMensalEventosRepository;

    public FolhaMensal create (FolhaMensalDto folhaMensalDto){

        folhaMensalDto.setId(null);
        FolhaMensal folhaMensal = new FolhaMensal(folhaMensalDto);
        folhaMensal = folhaMensalRepository.save(folhaMensal);

        //Salvando eventos
        for(FolhaMensalEventosDto eventosDto : folhaMensalDto.getEventos()) {
            FolhaMensalEventos eventos = new FolhaMensalEventos();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setFolhaMensal(folhaMensal);
            folhaMensalEventosRepository.save(eventos);
        }
        return folhaMensal;
    }

    @Transactional
    public FolhaMensal update(Long id, FolhaMensalDto folhaMensalDto) {
        FolhaMensal existingFolha = findById(id);
        folhaMensalDto.setId(id);

        FolhaMensal folhaMensal = new FolhaMensal(folhaMensalDto);
        folhaMensal = folhaMensalRepository.save(folhaMensal);

        // Remove eventos antigos
        folhaMensalEventosRepository.deleteByFolhaMensalId(id);

        // Salva os novos eventos
        for (FolhaMensalEventosDto eventosDto : folhaMensalDto.getEventos()) {
            FolhaMensalEventos eventos = new FolhaMensalEventos();
            eventos.setCodigoEvento(eventosDto.getCodigoEvento());
            eventos.setDescricaoEvento(eventosDto.getDescricaoEvento());
            eventos.setReferencia(eventosDto.getReferencia());
            eventos.setVencimentos(eventosDto.getVencimentos());
            eventos.setDescontos(eventosDto.getDescontos());
            eventos.setFolhaMensal(folhaMensal);
            folhaMensalEventosRepository.save(eventos);
        }
        return folhaMensal;
    }

    @Transactional
    public void delete(Long id) {
        FolhaMensal folha = findById(id);

        // Remove os eventos primeiro (devido à constraint de chave estrangeira)
        folhaMensalEventosRepository.deleteByFolhaMensalId(id);

        // Remove o cadastro principal
        folhaMensalRepository.deleteById(id);
    }

    public FolhaMensal findById(Long id) {
        Optional<FolhaMensal> objFolhaMensal = folhaMensalRepository.findById(id);
        return objFolhaMensal.orElseThrow(() -> new ObjectNotFoundException("Cadastro de folha de pagamento mensal não encontrado"));
    }

    public List<FolhaMensalEventos> findAllEventosByFolhaMensalId(Long eventoId) {
        return folhaMensalEventosRepository.findAllEventosByFolhaMensalId(eventoId);
    }

    public List<FolhaMensal> findAll() {
        return folhaMensalRepository.findAll();
    }
}
