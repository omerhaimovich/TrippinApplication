package trippin.trippinapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import trippin.trippinapp.R;
import trippin.trippinapp.activities.TripActivity;
import trippin.trippinapp.model.Trip;
import trippin.trippinapp.model.User;
import trippin.trippinapp.serverAPI.RequestHandler;

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
        LinearLayout info;
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
        String trip_id = ((Trip)object).getGoogleID();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Trip trip = Trip.FromJSON(RequestHandler.getTrip(trip_id,
                                                    User.getCurrentUser().getEmail(),
                                                    (double)0,
                                                    (double)0).getAsJsonObject(), true);

            Intent intent = new Intent(this.getContext(), TripActivity.class);
            intent.putExtra("trip_id", trip.getGoogleID());
            this.getContext().startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(mContext, "Failed Loading Trip", Toast.LENGTH_SHORT).show();
        }
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
            viewHolder.info = (LinearLayout) convertView.findViewById(R.id.tripItemContent);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        if (User.getCurrentUser().getCurrentTrip() == trip)
        {


        }
        viewHolder.txtName.setText(trip.getName());
        viewHolder.txtFromTime.setText(DateFormat.format("dd/MM/yy", trip.getStartDate()).toString());

        if (trip.getEndDate() != null)
        {
            viewHolder.txtToTime.setText(DateFormat.format("dd/MM/yy", trip.getEndDate()).toString());
        }
        else
        {
            viewHolder.txtName.setTextSize(25);
            viewHolder.txtFromTime.setTextSize(20);
            //viewHolder.txtName.setTextColor(Color.BLACK);
            viewHolder.txtName.setTypeface(null, Typeface.BOLD_ITALIC);
            viewHolder.txtFromTime.setTypeface(null, Typeface.BOLD_ITALIC);
        }

        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
