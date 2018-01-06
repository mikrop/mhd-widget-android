package cz.mikropsoft.android.mhdwidget.interfaces;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

import cz.mikropsoft.android.mhdwidget.BuildConfig;
import cz.mikropsoft.android.mhdwidget.model.Spoj;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@Rest(rootUrl = BuildConfig.SERVER_URL,
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
