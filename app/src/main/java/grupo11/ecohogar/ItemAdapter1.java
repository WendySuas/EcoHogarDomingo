package grupo11.ecohogar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Wen on 18/05/2017.
 */
public class ItemAdapter1 extends BaseAdapter {
    private Activity activity;
    private ArrayList<DatoEnergia> item;

    public ItemAdapter1(Activity activity, ArrayList<DatoEnergia> item) {
        this.activity = activity;
        this.item = item;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<DatoEnergia> getItem() {
        return item;
    }

    public void setItem(ArrayList<DatoEnergia> item) {
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
            v = inf.inflate(R.layout.itemdatoenergia,null);
        }
        DatoEnergia datoEnergia = item.get(position);
        TextView Id_sensor = (TextView)v.findViewById(R.id.TV_id_sensor);
        Id_sensor.setText(datoEnergia.getValor());

        return v;
    }

}
