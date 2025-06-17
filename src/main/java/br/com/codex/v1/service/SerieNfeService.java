package br.com.codex.v1.service;


import br.com.codex.v1.domain.contabilidade.SerieNfe;
import br.com.codex.v1.domain.dto.SerieNfeDto;
import br.com.codex.v1.domain.repository.SerieNfeRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieNfeService {

    @Autowired
    private SerieNfeRepository serieNfeRepository;

    public SerieNfe create(SerieNfeDto serieNfeDto) {
        serieNfeDto.setId(null);
        validaSerieNfe(serieNfeDto);
        SerieNfe serieNfe = new SerieNfe(serieNfeDto);
        return serieNfeRepository.save(serieNfe);
    }

    public SerieNfe update(Long id, SerieNfeDto serieNfeDto) {
        serieNfeDto.setId(id);
        SerieNfe obj = findById(id);
        obj = new SerieNfe(serieNfeDto);
        return serieNfeRepository.save(obj);
    }

    public SerieNfe findById(Long id) {
        Optional<SerieNfe> obj = serieNfeRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Serviço não encontrado"));
    }

    public void delete(Long id) {
        serieNfeRepository.deleteById(id);
    }

    public List<SerieNfe> findAll(){
        return serieNfeRepository.findAll();
    }

    private void validaSerieNfe(SerieNfeDto serieNfeDto, Long... id) {
        Optional<SerieNfe> optional = serieNfeRepository.findByNumeroSerieAndCnpjAndAmbiente(
                serieNfeDto.getNumeroSerie(),serieNfeDto.getCnpj(),serieNfeDto.getAmbiente());
        optional.ifPresent(existing -> {
            if (id.length == 0 || !existing.getId().equals(id[0])) {
                throw new RuntimeException("Série já existe para este CNPJ e ambiente");
            }
        });
    }

}


