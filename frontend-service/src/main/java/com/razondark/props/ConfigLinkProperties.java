package com.razondark.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "rest.frontend.config")
public class ConfigLinkProperties {
    private String categoriesLink;
    private String specificationsLink;
    private String biddTypeLink;
    private String attributesLink;
}
