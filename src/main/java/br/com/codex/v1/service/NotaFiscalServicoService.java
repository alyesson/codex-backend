package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.NotaFiscalServicoDto;
import br.com.codex.v1.domain.fiscal.NotaFiscalServico;
import br.com.codex.v1.domain.repository.NotaFiscalServicoRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotaFiscalServicoService {

    @Autowired
    private NotaFiscalServicoRepository repository;

    public NotaFiscalServico create(NotaFiscalServicoDto notaServicoDto) {
        notaServicoDto.setId(null);
        NotaFiscalServico obj = new NotaFiscalServico(notaServicoDto);
        return repository.save(obj);
    }

    public void delete(Long id) {
        NotaFiscalServico obj = findById(id);
        repository.delete(obj);
    }

    public NotaFiscalServico findById(Long id) {
        Optional<NotaFiscalServico> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Nota Fiscal de Serviço não encontrada! Id: " + id + ", Tipo: " + NotaFiscalServico.class.getName()));
    }

    public List<NotaFiscalServico> findByYear(Integer year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return repository.findByCompetenciaBetween(startDate, endDate);
    }

    public List<NotaFiscalServico> findByPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return repository.findByCompetenciaBetween(dataInicial, dataFinal);
    }

    // No serviço NotaFiscalServicoService
    public NotaFiscalServico cancelar(Long id, String justificativa) {
        NotaFiscalServico obj = findById(id);
        obj.setSituacao("Cancelada");
        obj.setJustificativaCancelamento(justificativa);
        obj.setDataCancelamento(LocalDateTime.now());
        return notaFiscalServicoRepository.save(obj);
    }
}