package br.com.codex.v1.configuration;

import br.com.codex.v1.domain.dto.EmpresaDto;
import br.com.codex.v1.service.DBService;
import br.com.codex.v1.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("padrao")
public class PadraoConfig {

    @Autowired
    private DBService dbService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String value;

    @Bean
    public  boolean instanciaDB(){
        if(value.equals("create")){
            this.dbService.instanciaDB();
        }
        return false;
    }
}
