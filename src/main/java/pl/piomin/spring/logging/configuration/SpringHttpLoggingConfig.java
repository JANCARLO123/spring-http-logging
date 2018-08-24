package pl.piomin.spring.logging.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.logging.http")
public class SpringHttpLoggingConfig {

    private static final String PATTERN_REQUEST = "{\"request\":{\"method\":\"%s\",\"path\":\"%s\",\"payload\":\"%s\"}}";
    private static final String PATTERN_RESPONSE = "{\"response\":{\"method\":\"%s\",\"path\":\"%s\",\"payload\":\"%s\"}}";

    @Value("${pattern.request:" + PATTERN_REQUEST + "}")
    String patternRequest;
    @Value("${pattern.response:" + PATTERN_RESPONSE + "}")
    String patternResponse;
    @Value("${emptyPayloadPattern:---}")
    String emptyPayloadPattern;
    @Value("${includeHeaders:}")
    String includeHeaders;

    public String getPatternRequest() {
        return patternRequest;
    }

    public void setPatternRequest(String patternRequest) {
        this.patternRequest = patternRequest;
    }

    public String getPatternResponse() {
        return patternResponse;
    }

    public void setPatternResponse(String patternResponse) {
        this.patternResponse = patternResponse;
    }

    public String getEmptyPayloadPattern() {
        return emptyPayloadPattern;
    }

    public void setEmptyPayloadPattern(String emptyPayloadPattern) {
        this.emptyPayloadPattern = emptyPayloadPattern;
    }

    public String getIncludeHeaders() {
        return includeHeaders;
    }

    public void setIncludeHeaders(String includeHeaders) {
        this.includeHeaders = includeHeaders;
    }
}
