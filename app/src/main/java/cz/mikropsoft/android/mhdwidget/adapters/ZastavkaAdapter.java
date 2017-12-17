package cz.mikropsoft.android.mhdwidget.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.springframework.util.CollectionUtils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.mikropsoft.android.mhdwidget.MainActivity;
import cz.mikropsoft.android.mhdwidget.ZastavkaItemView;
import cz.mikropsoft.android.mhdwidget.ZastavkaItemView_;
import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.databases.ZastavkaDao;
import cz.mikropsoft.android.mhdwidget.model.Zastavka;

@EBean
public class ZastavkaAdapter extends BaseAdapter implements Filterable {

    private static final String TAG = ZastavkaAdapter.class.getName();

    @RootContext
    Context context;

    List<Zastavka> zastavky = new ArrayList<>();
    ZastavkaFilter filter = new ZastavkaFilter();

    @Override
    public int getCount() {
        return zastavky.size();
    }

    @Override
    public Zastavka getItem(int position) {
        return zastavky.get(position);
    }

    @Override
    public long getItemId(int position) {
        return zastavky.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ZastavkaItemView zastavkaItemView;
        if (convertView == null) {
            zastavkaItemView = ZastavkaItemView_.build(parent.getContext());
        } else {
            zastavkaItemView = (ZastavkaItemView) convertView;
        }
        zastavkaItemView.bind(getItem(position));
        return zastavkaItemView;

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class ZastavkaFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Zastavka> original = MhdDatabase.getInstance(context).zastavkaDao().getAll();
            int size = original.size();
            List<Zastavka> values = new ArrayList<>(size);

            if (constraint == null) {
                values.addAll(original);
            } else {
                for (int i = 0; i < size; i++) {
                    Zastavka filterable = original.get(i);
                    String normalize = Normalizer.normalize(filterable.getJmeno(), Normalizer.Form.NFD)
                            .replaceAll("[^\\p{ASCII}]", "");
                    if (!TextUtils.isEmpty(constraint) && normalize.toLowerCase().contains(constraint)) {
                        values.add(filterable);
                    } else if (filterable.isFavorite()) {
                        values.add(filterable);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = values;
            results.count = values.size();
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            setData((ArrayList<Zastavka>) results.values);
        }

    }

    public void setData(List<Zastavka> zastavky) {
        Collections.sort(zastavky);
        this.zastavky = zastavky;
        notifyDataSetChanged();
    }

}
