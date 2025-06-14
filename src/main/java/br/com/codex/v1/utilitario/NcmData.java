package br.com.codex.v1.utilitario;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NcmData {

    private String codigoNcm;
    private String descricaoNcm;
    private Date dataInicio;
    private Date dataFim;
    private String tipoAto;
    private String numeroAto;
    private int anoAto;

    public NcmData() {
        super();
    }

    public NcmData(int anoAto, String numeroAto, String tipoAto, Date dataFim, Date dataInicio, String descricaoNcm, String codigoNcm) {
        this.anoAto = anoAto;
        this.numeroAto = numeroAto;
        this.tipoAto = tipoAto;
        this.dataFim = dataFim;
        this.dataInicio = dataInicio;
        this.descricaoNcm = descricaoNcm;
        this.codigoNcm = codigoNcm;
    }
}
