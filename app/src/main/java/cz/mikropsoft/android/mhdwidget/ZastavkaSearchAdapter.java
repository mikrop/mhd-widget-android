package cz.mikropsoft.android.mhdwidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import cz.mikropsoft.android.mhdwidget.model.Zastavka;

/**
 * Adaptér pro hledání zastávek.
 */
public class ZastavkaSearchAdapter extends BaseAdapter {

    private List<Zastavka> mZastavky;
    private LayoutInflater mInflater;
    private String mQuery;

    public ZastavkaSearchAdapter(Context context, List<Zastavka> zastavky) {
        super();
        mZastavky = zastavky;
        mInflater = LayoutInflater.from(context);
        mQuery = null;
    }

    @Override
    public int getCount() {
        return mZastavky.size();
    }

    @Override
    public Zastavka getItem(int position) {
        return mZastavky.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.zastavka_suggestion, null);

            holder = new ViewHolder();
            holder.favorite = (CheckBox) convertView.findViewById(R.id.favorite);
            holder.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    Zastavka zastavka = (Zastavka) cb.getTag();
                    zastavka.setSelected(cb.isChecked());
                }
            });
            holder.zastavka = (TextView) convertView.findViewById(R.id.zastavka);
            holder.smer = (TextView) convertView.findViewById(R.id.smer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Zastavka zastavka = getItem(position);
        holder.favorite.setChecked(zastavka.isSelected());
        holder.favorite.setTag(zastavka);
        holder.zastavka.setText(zastavka.getJmeno());
        holder.smer.setText(zastavka.getSmer());

        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    // Public methods ------------------------------------------------------------------------------

    public void setData(List<Zastavka> zastavky) {
        mZastavky = zastavky;
        notifyDataSetChanged();
    }

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    // ViewHolder class ----------------------------------------------------------------------------

    static class ViewHolder {
        CheckBox favorite;
        TextView zastavka;
        TextView smer;
    }

}
