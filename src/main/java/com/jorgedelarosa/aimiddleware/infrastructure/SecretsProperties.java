package com.jorgedelarosa.aimiddleware.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author jorge
 */
@Configuration
@PropertySource("classpath:secrets.properties")
public class SecretsProperties {}
