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
import java.util.stream.Collectors;

import cz.mikropsoft.android.mhdwidget.JizdniRadItemView;
import cz.mikropsoft.android.mhdwidget.JizdniRadItemView_;
import cz.mikropsoft.android.mhdwidget.databases.MhdDatabase;
import cz.mikropsoft.android.mhdwidget.model.JizdniRad;

@EBean
public class JizdniRadAdapter extends BaseAdapter {

    private static final String TAG = JizdniRadAdapter.class.getName();

    @RootContext
    Context context;

    List<JizdniRad> jizdniRady = new ArrayList<>();
    public void setData(int zastavkaId) {
        Assert.assertNotNull(zastavkaId);

        List<JizdniRad> jizdniRady = MhdDatabase.getInstance(context).spojDao()
                .findByZastavkaId(zastavkaId).stream()
                .collect(Collectors.groupingBy(spoj -> spoj.getOdjezd().getHourOfDay()))
                .values().stream()
                .map(JizdniRad::new)
                .collect(Collectors.toList());
        Collections.sort(jizdniRady, Collections.reverseOrder());

        this.jizdniRady = jizdniRady;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jizdniRady.size();
    }

    @Override
    public JizdniRad getItem(int position) {
        return jizdniRady.get(position);
    }

    @Override
    public long getItemId(int position) {
        return jizdniRady.get(position).getHodina().getHourOfDay();
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
