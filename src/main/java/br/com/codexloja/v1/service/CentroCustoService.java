package br.com.codexloja.v1.service;


import br.com.codexloja.v1.domain.cadastros.CentroCusto;
import br.com.codexloja.v1.domain.dto.CentroCustoDto;
import br.com.codexloja.v1.domain.repository.CentroCustoRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CentroCustoService {

    @Autowired
    private CentroCustoRepository centroCustoRepository;

    public CentroCusto create(CentroCustoDto centrocustoDto) {
        centrocustoDto.setId(null);
        validaCentroCusto(centrocustoDto);
        CentroCusto centroDeCusto = new CentroCusto(centrocustoDto);
        return centroCustoRepository.save(centroDeCusto);
    }
    public CentroCusto update(Integer id, CentroCustoDto centrocustoDto) {
        centrocustoDto.setId(id);
        CentroCusto obj = findById(id);
        obj = new CentroCusto(centrocustoDto);
        return centroCustoRepository.save(obj);
    }

    public CentroCusto findById(Integer id) {
        Optional<CentroCusto> obj = centroCustoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Centro de custo não encontrado"));
    }

    public void delete(Integer id) {
        centroCustoRepository.deleteById(id);
    }

    public List<CentroCusto> findAll(){
        return centroCustoRepository.findAll();
    }

    private void validaCentroCusto(CentroCustoDto centrocustoDto){
        Optional<CentroCusto> obj = centroCustoRepository.findByCodigo(centrocustoDto.getCodigo());
        if(obj.isPresent() && obj.get().getCodigo().equals(centrocustoDto.getCodigo())){
            throw new DataIntegrityViolationException("Este centro de custo já existe");
        }
    }
}
