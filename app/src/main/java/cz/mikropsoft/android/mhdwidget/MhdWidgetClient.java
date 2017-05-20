package cz.mikropsoft.android.mhdwidget;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;

@Rest(rootUrl = "http://10.0.3.2:8080/api/zastavka", converters = { MappingJackson2HttpMessageConverter.class },
        interceptors = { MhdWidgetInterceptor.class }
)
public interface MhdWidgetClient {

    @Get(value = "/{id}/aktualni")
    ResponseEntity<AktualniSpoj> getAktualniSpoj(@Path Integer id);

}
