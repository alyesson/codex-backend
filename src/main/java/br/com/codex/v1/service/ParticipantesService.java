package br.com.codex.v1.service;

import br.com.codex.v1.domain.dto.CadastroParticipantesSpedDto;
import br.com.codex.v1.domain.dto.NotaEntradaSpedDto;
import br.com.codex.v1.domain.dto.NotaSaidaSpedDto;

import java.util.ArrayList;
import java.util.List;

public class ParticipantesService {

    public static List<CadastroParticipantesSpedDto> getListaParticipantesNotaEntrada(List<NotaEntradaSpedDto> listaNotas) {
        List<CadastroParticipantesSpedDto> participantes = new ArrayList<>();

        listaNotas.forEach(
                nota -> {
                    if (participantes.stream().noneMatch(p -> nota.getCpfCnpjDestinatario().equals(p.getCodPart()))) {
                        CadastroParticipantesSpedDto part = new CadastroParticipantesSpedDto();
                        part.setCodPart(nota.getCpfCnpjDestinatario());
                        if (nota.getCpfCnpjDestinatario().length() > 11) {
                            part.setCnpj(nota.getCpfCnpjDestinatario());
                        } else {
                            part.setCpf(nota.getCpfCnpjDestinatario());
                        }
                        part.setNumero(nota.getNumeroEnderecoDestinatario());
                        part.setNome(nota.getNomeDestinatario());
                        part.setIe(nota.getInscricaoEstadualDestinatario());
                        part.setEndereco(nota.getEnderecoDestinatario());
                        part.setComplemento(nota.getComplementoEnderecoDestinatario());
                        part.setCodMunicipio(nota.getCMunicipioDestinatario());
                        part.setBairro(nota.getSetorDestinatario());
                        participantes.add(part);
                    }

                });
        return participantes;
    }

    public static List<CadastroParticipantesSpedDto> getListaParticipantesNotaSaida(List<NotaSaidaSpedDto> listaNotas) {
        List<CadastroParticipantesSpedDto> participantes = new ArrayList<>();

        listaNotas.forEach(
                nota -> {
                    if (participantes.stream().noneMatch(p -> nota.getCpfCnpjDestinatario().equals(p.getCodPart()))) {
                        CadastroParticipantesSpedDto part = new CadastroParticipantesSpedDto();
                        part.setCodPart(nota.getCpfCnpjDestinatario());
                        if (nota.getCpfCnpjDestinatario().length() > 11) {
                            part.setCnpj(nota.getCpfCnpjDestinatario());
                        } else {
                            part.setCpf(nota.getCpfCnpjDestinatario());
                        }
                        part.setNumero(nota.getNumeroEnderecoDestinatario());
                        part.setNome(nota.getNomeDestinatario());
                        part.setIe(nota.getInscricaoEstadualDestinatario());
                        part.setEndereco(nota.getEnderecoDestinatario());
                        part.setComplemento(nota.getComplementoEnderecoDestinatario());
                        part.setCodMunicipio(nota.getCMunicipioDestinatario());
                        part.setBairro(nota.getSetorDestinatario());
                        participantes.add(part);
                    }

                });
        return participantes;
    }
}
