package br.com.codexloja.v1.configuration;

import br.com.codexloja.v1.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("teste")
public class TestConfig {

    @Autowired
    private DBService dbService;

    @Bean
    public void instanciaDB(){
            this.dbService.instanciaDB();
    }
}
