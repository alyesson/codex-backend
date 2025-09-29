package br.com.codex.v1.utilitario;


import br.com.codex.v1.domain.estoque.MotivoAcerto;
import br.com.codex.v1.domain.repository.MotivoAcertoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImportaMotivosAcerto {

    @Autowired
    private MotivoAcertoRepository motivoAcertoRepository;

    public void importarMotivosAcerto() {

        List<MotivoAcerto> listaMotivoAcerto = new ArrayList<>();

        listaMotivoAcerto.add(new MotivoAcerto(null, "1000", "Entrada De Material Com Nota Fiscal"));
        listaMotivoAcerto.add(new MotivoAcerto(null, "1001", "Entrada De Material Sem Nota Fiscal"));
        listaMotivoAcerto.add(new MotivoAcerto(null, "1002", "Devolução"));
        listaMotivoAcerto.add(new MotivoAcerto(null, "1003", "Material Danificado"));
        listaMotivoAcerto.add(new MotivoAcerto(null, "1004", "Material Extraviado"));

        motivoAcertoRepository.saveAll(listaMotivoAcerto);
    }
}
