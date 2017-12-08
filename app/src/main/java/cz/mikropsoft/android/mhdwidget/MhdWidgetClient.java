package cz.mikropsoft.android.mhdwidget;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.joda.time.LocalTime;
import org.springframework.hateoas.hal.ResourceMappingJackson2HttpMessageConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

//@Rest(rootUrl = "http://10.0.3.2:8080/api",
@Rest(rootUrl = "http://192.168.88.13:8080/api",
//@Rest(rootUrl = "http://mhdwidgetapi-mikropsoft.rhcloud.com/api",
//@Rest(rootUrl = "https://mhd-widget.herokuapp.com/api",
        converters = { MappingJackson2HttpMessageConverter.class, ResourceMappingJackson2HttpMessageConverter.class },
        interceptors = { MhdWidgetInterceptor.class }
)
public interface MhdWidgetClient {

    @Get(value = "/zastavky")
    ResponseEntity<List<Zastavka>> getZastavky();

    @Get(value = "/zastavka/{id}/aktualni/{time}")
    ResponseEntity<AktualniSpoj> getAktualniSpoj(@Path Integer id, @Path("time") LocalTime clientLocalTime);

}
