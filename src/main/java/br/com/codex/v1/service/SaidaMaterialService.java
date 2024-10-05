package br.com.codex.v1.service;

import br.com.codex.v1.domain.estoque.EntradaMaterial;
import br.com.codex.v1.domain.estoque.Produto;
import br.com.codex.v1.domain.estoque.SaidaMaterial;
import br.com.codex.v1.domain.dto.SaidaMaterialDto;
import br.com.codex.v1.domain.repository.EntradaMaterialRepository;
import br.com.codex.v1.domain.repository.ProdutoRepository;
import br.com.codex.v1.domain.repository.SaidaMaterialRepository;
import br.com.codex.v1.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class SaidaMaterialService {

    @Autowired
    private SaidaMaterialRepository saidaMaterialRepository;

    @Autowired
    private EntradaMaterialRepository entradaMaterialRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public SaidaMaterial create(SaidaMaterialDto saidaMaterialDto){

        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataFormatada = dataAtual.format(formatter);
        saidaMaterialDto.setDataSaida(java.sql.Date.valueOf(dataFormatada));

        saidaMaterialDto.setId(null);
        SaidaMaterial objSai = new SaidaMaterial(saidaMaterialDto);
        return saidaMaterialRepository.save(objSai);
    }

    public EntradaMaterial removeSaldo(String codigoProduto, String lote, int quantidade){
        Optional<EntradaMaterial> objSaida = entradaMaterialRepository.findByCodigoProdutoAndLote(codigoProduto, lote);

        if(objSaida.isPresent()){
            EntradaMaterial entradaMaterial = objSaida.get();

            int saldoAtual = entradaMaterial.getQuantidade();

            if(saldoAtual < quantidade){
                throw new DataIntegrityViolationException("Quantidade a ser debitada é maior do que o saldo disponível");
            }
            int novoSaldo = saldoAtual - quantidade;
            entradaMaterial.setQuantidade(novoSaldo);
            return entradaMaterialRepository.save(entradaMaterial);
        }else{
            throw new DataIntegrityViolationException("Produto de lote '" + lote + "' não encontrado");
        }
    }

    public Produto findByCodigoProduto(String codigoProduto){
        Optional<Produto> codigo = produtoRepository.findByCodigo(codigoProduto);
        return codigo.orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado com esse código"));
    }

    public List<SaidaMaterial> findAllByYear(Integer anoAtual){
        return saidaMaterialRepository.findAllByYear(anoAtual);
    }

    public List<SaidaMaterial> findAllSaidaPeriodo(Date dataInicial, Date dataFinal) {
        return saidaMaterialRepository.findAllSaidaPeriodo(dataInicial, dataFinal);
    }
}
