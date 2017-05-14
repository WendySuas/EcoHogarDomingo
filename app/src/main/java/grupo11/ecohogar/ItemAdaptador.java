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
 * Created by Clau on 23/04/2017.
 */

public class ItemAdaptador extends BaseAdapter{
    private Activity activity;
    private ArrayList<Mensaje>itemNoti;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<Mensaje> getItem() {
        return itemNoti;
    }

    public void setItem(ArrayList<Mensaje> item) {
        this.itemNoti = item;
    }

    public ItemAdaptador(Activity activity, ArrayList<Mensaje> item) {

        this.activity = activity;
        this.itemNoti = item;
    }

    @Override
    public int getCount() {
        return itemNoti.size();
    }

    @Override
    public Object getItem(int position) {
        return itemNoti.get(position);
    }
    @Override
    public long getItemId(int position) {
        return itemNoti.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView==null)
        {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_notificacion,null);
        }
        Mensaje mensaje= itemNoti.get(position);
        TextView notificacion = (TextView)v.findViewById(R.id.txtNotificacion);
        notificacion.setText(mensaje.getFecha());
        return v;
    }
}
