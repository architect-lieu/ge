package com.huhuhu.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/10 10:08
 */

@SpringBootApplication
public class DaTiTongApplication {
    public static void main(String[] args) {
        Locale newLocale = Locale.ROOT;
        Locale.setDefault(newLocale);
        SpringApplication.run(DaTiTongApplication.class, args);
    }
}
