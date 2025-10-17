package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.TabelaDeducaoInssDto;
import br.com.codex.v1.domain.repository.TabelaDeducaoInssRepository;
import br.com.codex.v1.domain.rh.TabelaDeducaoInss;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class TabelaDeducaoInssService {

    @Autowired
    private TabelaDeducaoInssRepository tabelaDeducaoInssRepository;

    public TabelaDeducaoInss create(TabelaDeducaoInssDto tabelaDeducaoInssDto) {
        tabelaDeducaoInssDto.setId(null);
        TabelaDeducaoInss centroDeCusto = new TabelaDeducaoInss(tabelaDeducaoInssDto);
        return tabelaDeducaoInssRepository.save(centroDeCusto);
    }

    public TabelaDeducaoInss update(Long id, TabelaDeducaoInssDto tabelaDeducaoInssDto) {
        tabelaDeducaoInssDto.setId(id);
        TabelaDeducaoInss obj = findById(id);
        obj = new TabelaDeducaoInss(tabelaDeducaoInssDto);
        return tabelaDeducaoInssRepository.save(obj);
    }

    public TabelaDeducaoInss findById(Long id) {
        Optional<TabelaDeducaoInss> obj = tabelaDeducaoInssRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Valor de incidência do inss não encontrado"));
    }

    public void delete(Long id) {
        tabelaDeducaoInssRepository.deleteById(id);
    }

    public List<TabelaDeducaoInss> findAll(){
        return tabelaDeducaoInssRepository.findAll();
    }

    public BigDecimal getSalarioFamilia() {
        return tabelaDeducaoInssRepository.findTopByOrderById()
                .orElseThrow(() -> new RuntimeException("Tabela de dedução Inss não encontrada"))
                .getSalarioFamilia();
    }

    public BigDecimal calcularSalarioFamilia(BigDecimal salarioBase, Integer numeroFilhos) {
        try {
            // Busca a vigência ativa mais recente
            Optional<TabelaDeducaoInss> vigenciaAtiva = tabelaDeducaoInssRepository.findTopByOrderById();

            if (vigenciaAtiva.isEmpty()) {
                throw new RuntimeException("Não há vigência ativa para salário família");
            }

            SalarioFamilia parametros = vigenciaAtiva.get(0);
            BigDecimal valorTotal = BigDecimal.ZERO;

            // Lógica de cálculo (igual à sua, mas com Spring)
            if (salarioBase.compareTo(parametros.getFaixa2()) > 0 &&
                    salarioBase.compareTo(parametros.getFaixa1()) <= 0) {
                valorTotal = parametros.getCota1();
            } else if (salarioBase.compareTo(parametros.getFaixa2()) <= 0) {
                valorTotal = parametros.getCota2();
            }

            // Multiplica pelo número de filhos
            return valorTotal.multiply(BigDecimal.valueOf(numeroFilhos))
                    .setScale(2, RoundingMode.HALF_UP);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular salário família: " + e.getMessage());
        }
    }
}
