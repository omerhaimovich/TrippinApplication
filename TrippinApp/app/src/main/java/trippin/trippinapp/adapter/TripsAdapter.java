package trippin.trippinapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import trippin.trippinapp.R;
import trippin.trippinapp.model.Trip;

/**
 * Created by tacco on 5/6/17.
 */

public class TripsAdapter extends ArrayAdapter<Trip> implements View.OnClickListener{
    private ArrayList<Trip> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtFromTime;
        TextView txtToTime;
//        ImageView info;
    }

    public TripsAdapter(ArrayList<Trip> data, Context context) {
        super(context, R.layout.trip_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Trip dataModel = (Trip)object;

//        switch (v.getId())
//        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Trip trip = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.trip_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.txtFromTime = (TextView) convertView.findViewById(R.id.txtFromTime);
            viewHolder.txtToTime = (TextView) convertView.findViewById(R.id.txtToTime);
//            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(trip.getName());
        viewHolder.txtFromTime.setText(trip.getStartDate().toString());
        viewHolder.txtToTime.setText(trip.getEndDate().toString());
//        viewHolder.info.setOnClickListener(this);
//        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
