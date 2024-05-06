package com.razondark.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "rest.frontend.data")
public class DataLinkProperties {
    private String allDataLink;
    private String subjectsDataLink;
}
