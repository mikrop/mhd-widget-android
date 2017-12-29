package cz.mikropsoft.android.mhdwidget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cz.mikropsoft.android.mhdwidget.JizdniRadItemView;
import cz.mikropsoft.android.mhdwidget.JizdniRadItemView_;
import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.model.AktualniSpoj;
import cz.mikropsoft.android.mhdwidget.model.SpojeZaHodinu;

@EBean
public class JizdniRadAdapter extends BaseAdapter {

    private static final String TAG = JizdniRadAdapter.class.getName();

    @RootContext
    Context context;

    List<SpojeZaHodinu> spojeZaHodinu = new ArrayList<>();
    public void setData(int zastavkaId) {
        Assert.assertNotNull(zastavkaId);

        AktualniSpoj aktualniSpoj = MhdDatabase.getAktualniSpoj(context, zastavkaId);
        this.spojeZaHodinu = MhdDatabase.getInstance(context).spojDao()
                .findByZastavkaId(zastavkaId).stream()
                .peek(spoj -> {
                    spoj.setAktualni(aktualniSpoj != null && Objects.equals(spoj.getOdjezd(), aktualniSpoj.getOdjezd()));
                })
                .collect(Collectors.groupingBy(spoj -> spoj.getOdjezd().getHourOfDay()))
                .values().stream()
                .map(SpojeZaHodinu::new)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return spojeZaHodinu.size();
    }

    @Override
    public SpojeZaHodinu getItem(int position) {
        return spojeZaHodinu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return spojeZaHodinu.get(position).getHodinaDne();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        JizdniRadItemView jizdniRadItemView;
        if (convertView == null) {
            jizdniRadItemView = JizdniRadItemView_.build(parent.getContext());
        } else {
            jizdniRadItemView = (JizdniRadItemView) convertView;
        }
        jizdniRadItemView.bind(getItem(position));
        return jizdniRadItemView;

    }

}
