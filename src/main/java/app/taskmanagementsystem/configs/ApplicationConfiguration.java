package app.taskmanagementsystem.configs;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

@Configuration
public class ApplicationConfiguration {


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
