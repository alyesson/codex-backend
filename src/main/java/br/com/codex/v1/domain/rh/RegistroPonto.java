package br.com.codex.v1.domain.rh;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class RegistroPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sequencia", length = 10)
    private String sequencia;

    @Column(name = "data_registro", nullable = false)
    private LocalDate data;

    @Column(name = "hora_registro", nullable = false)
    private LocalTime hora;

    @Column(length = 1)
    private String tipoRegistro;

    @Column(name = "numero_pis", length = 12)
    private String pis;

    @Column(name = "nome_colaborador", length = 100)
    private String nome;

    @Column(length = 1)
    private String tipoMarcacao;

    @Column(length = 10)
    private String nsr;

    @Column(columnDefinition = "TEXT")
    private String linhaOriginal;

    @CreationTimestamp
    private LocalDateTime dataImportacao;

    @Column(length = 50)
    private String loteImportacao;

    // Relacionamento com colaborador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id")
    private CadastroColaboradores colaborador;

    public RegistroPonto() {
    }

    public RegistroPonto(String linha, String loteImportacao) {
        this.linhaOriginal = linha;
        this.loteImportacao = loteImportacao;
        parseLinha(linha);
    }

    private void parseLinha(String linha) {
        if (linha != null && linha.length() >= 50) {
            try {
                this.sequencia = linha.substring(0, 10).trim();

                // Data: DDMMAAAA
                String dataStr = linha.substring(11, 19);
                this.data = LocalDate.of(
                        Integer.parseInt(dataStr.substring(4, 8)), // ano
                        Integer.parseInt(dataStr.substring(2, 4)), // mês
                        Integer.parseInt(dataStr.substring(0, 2))  // dia
                );

                // Hora: HHMM
                String horaStr = linha.substring(19, 23);
                this.hora = LocalTime.of(
                        Integer.parseInt(horaStr.substring(0, 2)), // hora
                        Integer.parseInt(horaStr.substring(2, 4))  // minuto
                );

                this.tipoRegistro = linha.substring(23, 24);

                // Se for registro de identificação (tipo I)
                if ("I".equals(tipoRegistro) && linha.length() > 60) {
                    this.pis = linha.substring(25, 36).trim();
                    this.nome = linha.substring(36, 86).trim();
                    this.tipoMarcacao = linha.substring(86, 87);

                    // NSR (últimos dígitos)
                    if (linha.length() > 100) {
                        this.nsr = linha.substring(linha.length() - 15, linha.length() - 5);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao parsear linha do ponto: " + linha, e);
            }
        }
    }
}