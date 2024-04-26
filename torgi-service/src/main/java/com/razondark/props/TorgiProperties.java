package com.razondark.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "rest.torgi")
public class TorgiProperties {
    private String biddTypeLink;
    private String electronicPlatformLink;
    private String dynamicAttrSearchOptionLink;
    private String ownershipFormLink;
    private String typeTransactionLink;
    private String categoriesLink;
    private String specificationsLink;

    private String searchLotsLink;
    private String lotInfoLink;
}
