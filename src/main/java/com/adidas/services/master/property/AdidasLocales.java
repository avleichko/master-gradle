package com.adidas.services.master.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "adidas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdidasLocales {
    private Map<String, String> locales = new HashMap<>();
}
