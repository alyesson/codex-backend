package br.com.codex.v1.utilitario;

import br.com.codex.v1.domain.contabilidade.ControleNsu;
import br.com.codex.v1.domain.repository.ControleNsuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ControleNsuService {

    @Autowired
    private ControleNsuRepository controleNsuRepository;


    public void consultarDocumentos(String cnpj, String ambiente) {
        // Busca o último NSU
        Optional<ControleNsu> controleNsuOpt = controleNsuRepository.findByCnpjAndAmbiente(cnpj, ambiente);
        ControleNsu controleNsu = controleNsuOpt.orElseGet(() -> {
            ControleNsu novo = new ControleNsu();
            novo.setCnpj(cnpj);
            novo.setAmbiente(ambiente);
            novo.setUltimoNsu(0L);
            novo.setDataUltimaConsulta(LocalDateTime.now());
            return novo;
        });

        // Chama o Web Service NfeDistribuicaoDFe com o ultimoNsu
        Long ultimoNsu = controleNsu.getUltimoNsu();
        // Simulação: obtém maxNSU e documentos do Web Service
        Long maxNsu = consultarWebService(ultimoNsu, cnpj, ambiente);

        // Atualiza o controle_nsu com o maxNSU
        controleNsu.setUltimoNsu(maxNsu);
        controleNsu.setDataUltimaConsulta(LocalDateTime.now());
        controleNsuRepository.save(controleNsu);
    }

    private Long consultarWebService(Long ultimoNsu, String cnpj, String ambiente) {
        // Implementar chamada ao Web Service NfeDistribuicaoDFe
        // Retorna o <maxNSU> da resposta
        // Exemplo fictício:
        return ultimoNsu + 50; // Simula um novo maxNSU
    }
}
