package docshare.com.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="docshare.com")
public class CaTicketManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaTicketManagementApplication.class, args);
    }

}
