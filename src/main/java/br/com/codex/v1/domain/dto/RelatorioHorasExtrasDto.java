package br.com.codex.v1.domain.dto;

import br.com.codex.v1.domain.rh.RelatorioHorasExtras;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


public class RelatorioHorasExtrasDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected Integer id;
    @NotNull(message = "Código do funcionário não pode ficar em branco")
    protected Integer codigo;
    @NotNull(message = "Nome do funcionário não pode ficar em branco")
    protected String nome;
    protected String referencia;
    protected BigDecimal valorInformado;
    @NotNull(message = "Quantidade de horas não pode ficar em branco")
    protected Long quantidadeHoras;
    @NotNull(message = "Tipo de hora não pode ficar em branco")
    protected Integer tipoHora;
    @NotNull(message = "Departamento não pode ficar em branco")
    protected String departamento;
    @NotNull(message = "Unidade fabril não pode ficar em branco")
    protected String unidadeFabril;

    public RelatorioHorasExtrasDto() {
        super();
    }

    public RelatorioHorasExtrasDto(RelatorioHorasExtras obj) {
        this.id = obj.getId();
        this.codigo = obj.getCodigo();
        this.nome = obj.getNome();
        this.referencia = obj.getReferencia();
        this.valorInformado = obj.getValorInformado();
        // Corrige a conversão de "HH:mm" para minutos (Long)
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

    private Long converterStringParaMinutos(String horasFormatadas) {
        if (horasFormatadas == null || !horasFormatadas.matches("\\d{2}:\\d{2}")) {
            return 0L; // Se for inválido, retorna 0 minutos
        }
        String[] partes = horasFormatadas.split(":");
        long horas = Long.parseLong(partes[0]);
        long minutos = Long.parseLong(partes[1]);
        return (horas * 60) + minutos;
    }
}
