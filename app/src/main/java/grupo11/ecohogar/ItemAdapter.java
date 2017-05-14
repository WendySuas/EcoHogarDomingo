package grupo11.ecohogar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Vivienda> item;

    public ItemAdapter(Activity activity, ArrayList<Vivienda> item) {
        this.activity = activity;
        this.item = item;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Vivienda> getItem() {
        return item;
    }

    public void setItem(ArrayList<Vivienda> item) {
        this.item = item;
    }
    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }
    @Override
    public long getItemId(int position) {
        return item.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null)
        {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item,null);
        }
        Vivienda vivienda = item.get(position);
        TextView nombre = (TextView)v.findViewById(R.id.TV_vivienda);
        nombre.setText(vivienda.getZona());
        return v;
    }

}
