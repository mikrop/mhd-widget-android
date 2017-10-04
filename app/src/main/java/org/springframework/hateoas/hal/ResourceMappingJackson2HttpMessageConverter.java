package org.springframework.hateoas.hal;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Extension of {@link MappingJackson2HttpMessageConverter} for use in consuming HAL
 * resources via {@link RestTemplate}.
 *
 * @author Roy Clarkson
 */
public class ResourceMappingJackson2HttpMessageConverter extends TypeConstrainedMappingJackson2HttpMessageConverter {

    /**
     * Construct a new {@code ResourceMappingJackson2HttpMessageConverter} with a
     * customized {@link ObjectMapper} to support HAL resources.
     */
    public ResourceMappingJackson2HttpMessageConverter() {
        super(Resource.class);
        super.setSupportedMediaTypes(Collections.singletonList(new MediaType("application", "hal+json", DEFAULT_CHARSET)));
        ObjectMapper objectMapper = getObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        super.setObjectMapper(objectMapper);
    }

}
