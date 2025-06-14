package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.NcmDto;
import br.com.codex.v1.utilitario.NcmList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NcmService {

    @Autowired
    private NcmFetcher ncmFetcher;

    public List<NcmDto> buscarPorCodigo(String codigo) {
        String codigoLimpo = codigo.replace(".", "");
        NcmList todosNcms = ncmFetcher.getAll(false);

        return todosNcms.getNcmList().stream()
                .filter(n -> n.getCodigoNcm().startsWith(codigoLimpo))
                .map(n -> new NcmDto(
                        formatarCodigoNcm(n.getCodigoNcm()),
                        n.getDescricaoNcm()
                ))
                .limit(10)
                .collect(Collectors.toList());
    }

    private String formatarCodigoNcm(String codigo) {
        if (codigo.length() >= 8) {
            return codigo.substring(0, 4) + "." +
                    codigo.substring(4, 6) + "." +
                    codigo.substring(6, 8);
        }
        return codigo;
    }
}
