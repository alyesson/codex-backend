package br.com.codex.v1.service.spedicms;

import br.com.codex.v1.domain.dto.CadastroParticipantesSpedDto;
import br.com.codex.v1.domain.dto.NotaEntradaSpedDto;
import br.com.codex.v1.service.ParticipantesService;
import br.com.codex.v1.utilitario.Util;
import br.com.swconsultoria.efd.icms.registros.bloco0.*;

import java.time.LocalDate;
import java.util.List;

public class Bloco0Service {


    private static Bloco0 bloco0;

    private Bloco0Service() {}

    public static Bloco0 getBloco(List<NotaEntradaSpedDto> listaNotas) {
        bloco0 = new Bloco0();
        preencherRegistro0000();
        preencherRegistro0001();
        preencherRegistro0005();
        preencherRegistro0100();

        List<CadastroParticipantesSpedDto> listaParticipantes = ParticipantesService.getListaParticipantesNotaSaida(listaNotas);
        preencherRegistro0150(listaParticipantes);

        return bloco0;
    }

    private static void preencherRegistro0000() {

        Registro0000 registro0000 = new Registro0000();
        registro0000.setCod_fin("0");
        registro0000.setDt_ini(Util.dataSpeed(LocalDate.of(2021, 3, 1)));
        registro0000.setDt_fin(Util.dataSpeed(LocalDate.of(2021, 3, 31)));
        registro0000.setNome("Empresa Teste");
        registro0000.setCnpj("22777466000130");
        registro0000.setUf("SP");
        registro0000.setIe("167641160730");
        registro0000.setCod_mun("3550308");
        registro0000.setIm(null);
        registro0000.setSuframa(null);
        registro0000.setInd_perfil("A");
        registro0000.setInd_ativ("1");
        bloco0.setRegistro0000(registro0000);
    }

    private static void preencherRegistro0001() {
        Registro0001 registro0001 = new Registro0001();
        registro0001.setInd_mov("0");
        bloco0.setRegistro0001(registro0001);
    }

    private static void preencherRegistro0005() {
        Registro0005 registro0005 = new Registro0005();
        registro0005.setFantasia("Empresa Teste Fantasia");
        registro0005.setCep("75000000");
        registro0005.setEnd("Rua Teste");
        registro0005.setNum("0");
        registro0005.setCompl("Qd 0 Lote 0");
        registro0005.setBairro("Centro");
        registro0005.setFone("62993066546");
        registro0005.setEmail("samuel@swconsultoria.com.br");
        bloco0.setRegistro0005(registro0005);
    }

    private static void preencherRegistro0100() {
        Registro0100 registro0100 = new Registro0100();
        registro0100.setNome("Contador Ze");
        registro0100.setCpf("07165924019");
        registro0100.setCrc("123456");
        registro0100.setCnpj("73038588000146");
        registro0100.setCep("75000000");
        registro0100.setEnd("Rua Teste");
        registro0100.setBairro("Centro");
        registro0100.setFone("62993066546");
        registro0100.setEmail("samuel@swconsultoria.com.br");
        registro0100.setCod_mun("5201108");
        bloco0.setRegistro0100(registro0100);
    }

    private static void preencherRegistro0150(List<CadastroParticipantesSpedDto> listaParticipantes) {
        Registro0150 registro0150;
        for (CadastroParticipantesSpedDto participante : listaParticipantes) {
            registro0150 = new Registro0150();

            registro0150.setCod_part(participante.getCodPart());
            registro0150.setNome(participante.getNome());
            registro0150.setCod_pais("01058");

            if (!Util.isEmpty(participante.getCnpj())) {
                registro0150.setCnpj(Util.manterApenasNumeros(participante.getCnpj()));
            } else if (!Util.isEmpty(participante.getCpf())) {
                registro0150.setCpf(Util.manterApenasNumeros(participante.getCpf()));
            }

            if (!Util.isEmpty(participante.getIe()) && !participante.getIe().toLowerCase().startsWith("isento")) {
                registro0150.setIe(Util.manterApenasNumeros(participante.getIe()));
            }

            registro0150.setCod_mun(participante.getCodMunicipio());
            registro0150.setEnd(participante.getEndereco());
            registro0150.setNum(participante.getNumero());
            registro0150.setBairro(participante.getBairro());
            if (!Util.isEmpty(participante.getComplemento())) {
                registro0150.setCompl(participante.getComplemento());
            }
            bloco0.getRegistro0150().add(registro0150);
        }
    }

}
