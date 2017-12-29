package cz.mikropsoft.android.mhdwidget.interfaces;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import cz.mikropsoft.android.mhdwidget.model.Spoj;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

//@Rest(rootUrl = "http://10.0.3.2:8080/api",
//@Rest(rootUrl = "http://192.168.88.13:8080/api",
//@Rest(rootUrl = "http://172.16.10.63:8080/api",
//@Rest(rootUrl = "http://mhdwidgetapi-mikropsoft.rhcloud.com/api",
@Rest(rootUrl = "https://mhd-widget.herokuapp.com/api",
        converters = { MappingJackson2HttpMessageConverter.class }
)
public interface MhdRestClient extends RestClientErrorHandling {

    @Get(value = "/zastavky")
    ResponseEntity<List<Zastavka>> getZastavky();

    @Get(value = "/zastavka/{zastavkaId}/spoje")
    ResponseEntity<List<Spoj>> getSpoje(@Path("zastavkaId") Integer zastavkaId);

//    @Get(value = "/zastavka/{id}/aktualni/{time}")
//    ResponseEntity<AktualniSpoj> getAktualniSpoj(@Path Integer id, @Path("time") LocalTime clientLocalTime);

}
