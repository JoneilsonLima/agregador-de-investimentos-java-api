package tech.buildrun.agregadorinvestimentos.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfiguration {

    @Bean
    public ModelMapper modalMapper() {
        return new ModelMapper();
    }
}
