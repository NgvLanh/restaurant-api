package org.edu.restaurantapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // Load .env file and set environment variables
        Dotenv dotenv = Dotenv.configure().directory(".").load();
        String[] keys = {
                "APPLICATION_NAME",
                "DB_URL",
                "DB_PASSWORD",
                "SIGNER_KEY",
                "VNP_TMN_CODE",
                "VNP_HASH_SECRET",
                "VNP_URL"
        };
        for (String key : keys) {
            System.setProperty(key, dotenv.get(key));
        }

        SpringApplication.run(Main.class, args);
    }

}
