package util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cefd.ufes.br.comuniqc.R;


/**
 * Created by pgrippa on 17/09/16.
 */
public class SpinnerItemAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] items;

    public SpinnerItemAdapter(Context context, String[] values){
        super(context, R.layout.spinner_item, R.id.s_tipo, values);
        this.context = context;
        this.items = values;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.spinner_item, parent, false);
        TextView label= row.findViewById(R.id.s_tipo);
        String opt = items[position];
        label.setText(items[position]);

        ImageView imageView=row.findViewById(R.id.i_icon);

        setIconByType(context, imageView, opt);

        return row;
    }

    private static void setIconByType(Context context, ImageView imageView, String tipo){
        if (tipo.equals(context.getString(R.string.select_option))) {
            imageView.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        } else if (tipo.equals(context.getString(R.string.t_problems))) {
            imageView.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_black_24dp);
        } else if (tipo.equals(context.getString(R.string.t_compliments))) {
            imageView.setImageResource(R.drawable.ic_lightbulb_outline_black_24dp);
        } else if (tipo.equals(context.getString(R.string.t_acsug))) {
            imageView.setImageResource(R.drawable.ic_school_black_24dp);
        }else if (tipo.equals(context.getString(R.string.t_campsug))) {
            imageView.setImageResource(R.drawable.ic_business_black_24dp);
        }else{
            imageView.setImageResource(R.drawable.ic_feedback_black_24dp);
        }
    }
}
