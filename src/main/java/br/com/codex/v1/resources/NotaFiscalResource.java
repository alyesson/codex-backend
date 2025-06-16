package br.com.codex.v1.resources;

import br.com.codex.v1.service.NotaFiscalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/api/nota_fiscal")
public class NotaFiscalResource {

    @Autowired
    private NotaFiscalService notaFiscalService;

    }
