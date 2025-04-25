package br.com.codex.v1.domain.rh;

import br.com.codex.v1.domain.dto.RelatorioHorasExtrasDto;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class RelatorioHorasExtras implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Column(length = 5)
    protected Integer codigo;
    protected String nome;
    @Column(length = 7)
    protected String referencia;
    protected BigDecimal valorInformado;
    @Column(name = "quantidade_horas") // Armazena a duração em minutos no banco
    protected Long quantidadeHoras;
    @Column(length = 3)
    protected Integer tipoHora;
    protected String departamento;
    protected String unidadeFabril;

    public RelatorioHorasExtras() {
        super();
    }

    public RelatorioHorasExtras(Integer id, Integer codigo, String nome, String referencia, BigDecimal valorInformado, Long quantidadeHoras, Integer tipoHora, String departamento, String unidadeFabril) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.referencia = referencia;
        this.valorInformado = valorInformado;
        this.quantidadeHoras = quantidadeHoras;
        this.tipoHora = tipoHora;
        this.departamento = departamento;
        this.unidadeFabril = unidadeFabril;
    }

    public RelatorioHorasExtras(RelatorioHorasExtrasDto obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.nome = obj.getNome();
        this.referencia = obj.getReferencia();
        this.valorInformado = obj.getValorInformado();
        // Converte a String "HH:mm" do DTO para minutos (Long) na entidade
        this.quantidadeHoras = converterStringParaMinutos(obj.getQuantidadeHoras());
        this.tipoHora = obj.getTipoHora();
        this.departamento = obj.getDepartamento();
        this.unidadeFabril = obj.getUnidadeFabril();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public BigDecimal getValorInformado() {
        return valorInformado;
    }

    public void setValorInformado(BigDecimal valorInformado) {
        this.valorInformado = valorInformado;
    }

    public String getQuantidadeHoras() {
        if (quantidadeHoras == null) return "00:00"; // Evita NullPointerException
        long horas = quantidadeHoras / 60;
        long minutos = quantidadeHoras % 60;
        return String.format("%02d:%02d", horas, minutos);
    }

    public void setQuantidadeHoras(String horasFormatadas) {
        if (horasFormatadas == null || !horasFormatadas.matches("\\d{2}:\\d{2}")) {
            this.quantidadeHoras = 0L; // Valor padrão para evitar erro
            return;
        }
        String[] partes = horasFormatadas.split(":");
        long horas = Long.parseLong(partes[0]);
        long minutos = Long.parseLong(partes[1]);
        this.quantidadeHoras = (horas * 60) + minutos;
    }

    public Integer getTipoHora() {
        return tipoHora;
    }

    public void setTipoHora(Integer tipoHora) {
        this.tipoHora = tipoHora;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getUnidadeFabril() {
        return unidadeFabril;
    }

    public void setUnidadeFabril(String unidadeFabril) {
        this.unidadeFabril = unidadeFabril;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RelatorioHorasExtras that = (RelatorioHorasExtras) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    // Métudo para converter "HH:mm" -> Long (minutos)
    private Long converterStringParaMinutos(String horasFormatadas) {
        if (horasFormatadas == null || !horasFormatadas.matches("\\d{2}:\\d{2}")) {
            return 0L; // Valor padrão caso a string seja inválida
        }
        String[] partes = horasFormatadas.split(":");
        long horas = Long.parseLong(partes[0]);
        long minutos = Long.parseLong(partes[1]);
        return (horas * 60) + minutos;
    }
}
