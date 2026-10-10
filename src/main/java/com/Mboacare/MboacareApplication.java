package com.Mboacare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.Mboacare", "com.logonedigital.MBOAcare"})
@EnableJpaRepositories(basePackages = "com.logonedigital.MBOAcare.repositoy")
@EntityScan(basePackages = "com.logonedigital.MBOAcare.entity")
public class MboacareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MboacareApplication.class, args);
    }

}
