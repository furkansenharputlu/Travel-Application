package com.harputyazilim.gezdir;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static com.harputyazilim.gezdir.R.id.recyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Matches.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Matches#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Matches extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Match> matchList = new ArrayList<>();
    MatchAdapter matchAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Matches() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Matches.
     */
    // TODO: Rename and change types and number of parameters
    public static Matches newInstance() {
        Matches fragment = new Matches();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button back2Map= (Button) view.findViewById(R.id.back2);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.matchesList);

        matchAdapter = new MatchAdapter(matchList,getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(matchAdapter);

        prepareMatchData(view);
        back2Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FS","back2MAP");
                getFragmentManager().popBackStack();
            }
        });

    }

    private void prepareMatchData(View view) {

        Match match = new Match("Aykut", (ImageView)view.findViewById(R.id.profileImage));
        matchList.add(match);
        match = new Match("Kubilay",(ImageView)view.findViewById(R.id.profileImage));
        matchList.add(match);
        match = new Match("Turgut",(ImageView)view.findViewById(R.id.profileImage));
        matchList.add(match);
        match = new Match("Hasan",(ImageView)view.findViewById(R.id.profileImage));
        matchList.add(match);
        match = new Match("Melek",(ImageView)view.findViewById(R.id.profileImage));
        matchList.add(match);
        match = new Match("Melih",(ImageView)view.findViewById(R.id.profileImage));
        matchList.add(match);
        match = new Match("Furkan",(ImageView)view.findViewById(R.id.profileImage));
        matchList.add(match);
        match = new Match("Sevgi",(ImageView)view.findViewById(R.id.profileImage));
        matchList.add(match);
        match = new Match("Kübra",(ImageView)view.findViewById(R.id.profileImage));
        matchList.add(match);



        matchAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
