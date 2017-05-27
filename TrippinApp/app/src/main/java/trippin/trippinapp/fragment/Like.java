package trippin.trippinapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.Closeable;
import java.io.IOException;

import trippin.trippinapp.R;
import trippin.trippinapp.model.User;
import trippin.trippinapp.serverAPI.RequestHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Like.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Like#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Like extends DialogFragment implements View.OnClickListener {

    // TODO: Rename and change types of parameters
    private String attraction_id;

    private OnLikelistener mListener;

    public Like() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Like.
     */
    // TODO: Rename and change types and number of parameters
    public static Like newInstance(String attraction_id) {
        Like fragment = new Like();
        Bundle args = new Bundle();
        args.putString("ATTRACTION_ID", attraction_id);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//
//
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.fragment_like, null));
//
//        return builder.create();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            attraction_id = getArguments().getString("ATTRACTION_ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_like, container, false);

        ((Button)view.findViewById(R.id.vote_up)).setOnClickListener(this);
        ((Button)view.findViewById(R.id.vote_down)).setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLikelistener) {
            mListener = (OnLikelistener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.vote_up)
        {
            try {
                RequestHandler.AttractionRated(User.getCurrentUser().getCurrentTrip().getGoogleID(), attraction_id, true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mListener.Close();
        }
        else if (v.getId() == R.id.vote_down)
        {
            try {
                // TODO: Chagne
                //RequestHandler.AttractionRated(User.getCurrentUser().getCurrentTrip().getGoogleID(), attraction_id, false);
                RequestHandler.AttractionRated(User.getCurrentUser().getTrips().get(0).getGoogleID(), attraction_id, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mListener.Close();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLikelistener {
        void Close();
    }
}
