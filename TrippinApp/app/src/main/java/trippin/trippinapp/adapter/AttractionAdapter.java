package trippin.trippinapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import trippin.trippinapp.R;
import trippin.trippinapp.activities.TripActivity;
import trippin.trippinapp.model.Attraction;
import trippin.trippinapp.model.Trip;

/**
 * Created by tacco on 5/7/17.
 */

public class AttractionAdapter extends ArrayAdapter<Attraction> {
    private ArrayList<Attraction> dataSet;
    Context mContext;
    private int lastPosition = -1;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtFromTime;
        TextView txtToTime;
        Button btnEnd;
        TextView txtRate;
    }

    public AttractionAdapter(ArrayList<Attraction> data, Context context) {
        super(context, R.layout.attraction_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Attraction attraction = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        AttractionAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.attraction_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.txtFromTime = (TextView) convertView.findViewById(R.id.txtFromTime);
            viewHolder.txtToTime = (TextView) convertView.findViewById(R.id.txtToTime);
            viewHolder.txtRate = (TextView) convertView.findViewById(R.id.txtRate);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AttractionAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(attraction.getName());
        viewHolder.txtFromTime.setText(DateFormat.format("dd/MM/yy", attraction.getStartDate()).toString());

        if (attraction.getEndDate() != null) {
            viewHolder.txtToTime.setText(DateFormat.format("dd/MM/yy", attraction.getEndDate()).toString());
        } else {
            viewHolder.txtToTime.setText("");
            viewHolder.txtName.setTextSize(25);
            viewHolder.txtFromTime.setTextSize(20);
            viewHolder.txtName.setTypeface(null, Typeface.BOLD_ITALIC);
            viewHolder.txtFromTime.setTypeface(null, Typeface.BOLD_ITALIC);
        }

        //viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
