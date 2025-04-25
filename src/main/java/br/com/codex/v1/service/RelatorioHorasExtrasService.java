package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.RelatorioHorasExtrasDto;
import br.com.codex.v1.domain.repository.RelatorioHorasExtrasRepository;
import br.com.codex.v1.domain.rh.RelatorioHorasExtras;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioHorasExtrasService {

    @Autowired
    private RelatorioHorasExtrasRepository relatorioHorasExtrasRepository;

    public RelatorioHorasExtras create (RelatorioHorasExtrasDto relatorioHorasExtrasDto){
        relatorioHorasExtrasDto.setId(null);
        RelatorioHorasExtras objRelatorioHorasExtras = new RelatorioHorasExtras(relatorioHorasExtrasDto);
        return relatorioHorasExtrasRepository.save(objRelatorioHorasExtras);
    }

    public List<RelatorioHorasExtras> findAll(){
        return relatorioHorasExtrasRepository.findAll();
    }
}
