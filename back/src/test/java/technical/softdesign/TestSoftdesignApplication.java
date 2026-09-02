package technical.softdesign;

import org.springframework.boot.SpringApplication;

public class TestSoftdesignApplication {

    public static void main(String[] args) {
        SpringApplication.from(SoftdesignApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
