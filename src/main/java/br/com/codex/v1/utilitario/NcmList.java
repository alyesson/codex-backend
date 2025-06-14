package br.com.codex.v1.utilitario;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NcmList {
    private List<NcmData> ncmList;

    public NcmList() {
        super();
    }

    public NcmList(List<NcmData> ncmList) {
        this.ncmList = ncmList;
    }
}
