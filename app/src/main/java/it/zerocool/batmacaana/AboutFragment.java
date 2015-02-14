/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Arrays;

import it.zerocool.batmacaana.utilities.Constant;


/**
 * Fragment with city general information
 * Created by Marco Battisti on 14/02/2014
 */
public class AboutFragment extends Fragment {

    private RecyclerView rvGallery;
    private ImageView mainPicture;
    private ExpandableTextView text;


    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_about, container, false);

        mainPicture = (ImageView) layout.findViewById(R.id.main_image);
        text = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        text.setText(getString(R.string.about));


        rvGallery = (RecyclerView) layout.findViewById(R.id.gallery_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvGallery.setLayoutManager(layoutManager);
        ArrayList<Integer> imageItems = new ArrayList<>();
        imageItems.addAll(Arrays.asList(Constant.GALLERY_IMAGE));
        GalleryAdapter adapter = new GalleryAdapter(getActivity(), mainPicture, imageItems);
        rvGallery.setAdapter(adapter);

        return layout;
    }


}
