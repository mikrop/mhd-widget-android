package cz.mikropsoft.android.mhdwidget;

import org.androidannotations.annotations.EBean;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@EBean
public class MhdWidgetInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        HttpHeaders headers = request.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Client-Local-Time", LocalTime.now().toString(ISODateTimeFormat.hourMinuteSecond()));
        return execution.execute(request, body);
    }

}
