package cz.mikropsoft.android.mhdwidget;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import cz.mikropsoft.android.mhdwidget.model.Zastavka;

/**
 * Loader pro načtení zastávek.
 */
public class ZastavkyLoader extends AsyncTaskLoader<List<Zastavka>> {

//    @RestService
//    MhdWidgetClient restClient;
//    @Pref
//    MainPreference_ mainPreference;

    private List<Zastavka> mZastavky;
    private String mQuery;
//    private Integer zastavkaId;
    private MhdWidgetClient restClient;

    /**
     * @param context aplikační kontext
     * @param bundle řetězec, proti kterému se budou zastávky hledat
     * @param restClient
     */
    public ZastavkyLoader(Context context, Bundle bundle, MhdWidgetClient restClient) {
        super(context);
        this.mQuery = bundle.getString("queryFiler");
//        this.zastavkaId = bundle.getInt("zastavkaId");
        this.restClient = restClient;
    }

//    public RestTemplate getRestTemplateWithHalMessageConverter() {
//        RestTemplate restTemplate = new RestTemplate();
//        List<HttpMessageConverter<?>> existingConverters = restTemplate.getMessageConverters();
//        List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
//        newConverters.add(getHalMessageConverter());
//        newConverters.addAll(existingConverters);
//        restTemplate.setMessageConverters(newConverters);
//        return restTemplate;
//    }
//
//    private HttpMessageConverter getHalMessageConverter() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new Jackson2HalModule());
//        MappingJackson2HttpMessageConverter halConverter = new TypeConstrainedMappingJackson2HttpMessageConverter(ResourceSupport.class);
//        halConverter.setSupportedMediaTypes(Arrays.asList(HAL_JSON));
//        halConverter.setObjectMapper(objectMapper);
//        return halConverter;
//    }

    @Override
    public List<Zastavka> loadInBackground() {
        List<Zastavka> result = new ArrayList<>();

        List<Zastavka> zastavky = restClient.getZastavky().getBody();
        for (Zastavka zastavka : zastavky) {
            String normalize = Normalizer.normalize(zastavka.getJmeno(), Normalizer.Form.NFD)
                    .replaceAll("[^\\p{ASCII}]", "");
            if (!TextUtils.isEmpty(mQuery) && normalize.toLowerCase().contains(mQuery)) {
                result.add(zastavka);
            } else if (zastavka.isSelected()) {
                result.add(zastavka);
            }
        }

        return result;
    }

    @Override
    public void deliverResult(List<Zastavka> zastavky) {
        if (isReset()) {
            onReleaseResources(zastavky);
        }
        List<Zastavka> oldData = mZastavky;
        mZastavky = zastavky;

        if (isStarted()) {
            super.deliverResult(zastavky);
        }

        if (oldData != null && oldData != zastavky) {
            onReleaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mZastavky != null) {
            deliverResult(mZastavky);
        }

        if (takeContentChanged() || mZastavky == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<Zastavka> zastavky) {
        super.onCanceled(zastavky);
        onReleaseResources(zastavky);
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps' if needed.
        if (mZastavky != null) {
            onReleaseResources(mZastavky);
            mZastavky = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<Zastavka> zastavky) {
        // For a simple List<> there is nothing to do. For something
        // like a Cursor, we would close it here.
    }

}
