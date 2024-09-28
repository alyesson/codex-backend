package br.com.codexloja.v1.service;

import br.com.codexloja.v1.domain.estoque.HistoricoMovimentacaoMaterial;
import br.com.codexloja.v1.domain.dto.HistoricoMovimentacaoMaterialDto;
import br.com.codexloja.v1.domain.repository.HistoricoMovimentacaoMaterialRepository;
import br.com.codexloja.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class HistoricoMovimentacaoMaterialService {

    @Autowired
    private HistoricoMovimentacaoMaterialRepository historicoMovimentacaoMaterialRepository;

    public HistoricoMovimentacaoMaterial create (HistoricoMovimentacaoMaterialDto historicoMovimentacaoMaterialDto){

        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataFormatada = dataAtual.format(formatter);
        historicoMovimentacaoMaterialDto.setDataEntrada(java.sql.Date.valueOf(dataFormatada));

        historicoMovimentacaoMaterialDto.setId(null);
        HistoricoMovimentacaoMaterial objEntra = new HistoricoMovimentacaoMaterial(historicoMovimentacaoMaterialDto);
        return historicoMovimentacaoMaterialRepository.save(objEntra);
    }

    public HistoricoMovimentacaoMaterial findById(Integer id){
        Optional<HistoricoMovimentacaoMaterial> objEntrada = historicoMovimentacaoMaterialRepository.findById(id);
        return objEntrada.orElseThrow(() -> new ObjectNotFoundException("Material não encontrado"));
    }

    public void delete(Integer id){
        historicoMovimentacaoMaterialRepository.deleteById(id);
    }

    public List<HistoricoMovimentacaoMaterial> findAll(){
        return historicoMovimentacaoMaterialRepository.findAllByOrderByIdDesc();
    }

    public List<HistoricoMovimentacaoMaterial> findAllByNota(Integer ano) {
        return historicoMovimentacaoMaterialRepository.findAllByNota(ano);
    }

    public List<HistoricoMovimentacaoMaterial> findAllByYearAndMonth(Integer ano, Integer mes) {
        return historicoMovimentacaoMaterialRepository.findAllByYearAndMonth(ano, mes);
    }

    public List<BigDecimal> findAllByYear(Integer ano) {
        List<Object[]> resultList = historicoMovimentacaoMaterialRepository.findAllByYear(ano);
        List<BigDecimal> values = new ArrayList<>();

        // Inicializa um array de BigDecimal com 12 elementos (para representar os 12 meses)
        BigDecimal[] monthlyValues = new BigDecimal[12];
        Arrays.fill(monthlyValues, BigDecimal.ZERO);

        // Preencha o array com os valores retornados pela consulta
        for (Object[] result : resultList) {
            int month = (int) result[0] - 1; // Mês retornado pela consulta (1 a 12)
            BigDecimal total = (BigDecimal) result[1]; // Valor total retornado pela consulta
            monthlyValues[month] = total;
        }
        // Adicione os valores mensais à lista
        values.addAll(Arrays.asList(monthlyValues));
        return values;
    }
}
