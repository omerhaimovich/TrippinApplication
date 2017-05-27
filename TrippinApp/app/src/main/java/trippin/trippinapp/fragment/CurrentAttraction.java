package trippin.trippinapp.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.w3c.dom.Attr;

import trippin.trippinapp.R;
import trippin.trippinapp.model.Attraction;
import trippin.trippinapp.model.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurrentAttraction.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurrentAttraction#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentAttraction extends Fragment implements View.OnClickListener {
    private Attraction m_attraction;
    private static String ATTRACTION = "ATTRACTION";

    private OnCurrentAttractionListener mListener;

    public CurrentAttraction() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters
     * @return A new instance of fragment CurrentAttraction.
     */
    public static CurrentAttraction newInstance(Attraction attr) {
        CurrentAttraction fragment = new CurrentAttraction();
        Bundle args = new Bundle();
        args.putSerializable(ATTRACTION, attr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_attraction = (Attraction) getArguments().getSerializable(ATTRACTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_attraction, container, false);

        ((TextView)view.findViewById(R.id.fragment_name_attration)).setText(m_attraction.getName());

        view.findViewById(R.id.end_attraction_frag).setOnClickListener(this);

        final ImageView image = ((ImageView)view.findViewById(R.id.imageViewCurrAttr));

        Glide.with(getContext()).load(m_attraction.getImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(image) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                image.setBackground(circularBitmapDrawable);
            }
        });

        return view;
    }

    public void onClose() {
        if (mListener != null) {
            mListener.CloseAttraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCurrentAttractionListener) {
            mListener = (OnCurrentAttractionListener) context;
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
        onClose();
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
    public interface OnCurrentAttractionListener {
        void CloseAttraction();
    }
}
