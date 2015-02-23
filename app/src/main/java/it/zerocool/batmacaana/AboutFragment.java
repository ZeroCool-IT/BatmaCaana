/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Arrays;

import it.zerocool.batmacaana.utilities.Constant;


/**
 * Fragment with city general information
 * Created by Marco Battisti on 14/02/2014
 */
public class AboutFragment extends Fragment implements View.OnClickListener {


    private ImageSwitcher mainPicture;
    private int current;
    private ArrayList<Integer> imageItems;

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


        //Bind widget
        mainPicture = (ImageSwitcher) layout.findViewById(R.id.main_image);
        ExpandableTextView descriptionText = (ExpandableTextView) layout.findViewById(R.id.description_tv);
        ExpandableTextView infoText = (ExpandableTextView) layout.findViewById(R.id.info_tv);
        ImageButton leftButton = (ImageButton) layout.findViewById(R.id.left_button);
        ImageButton rightButton = (ImageButton) layout.findViewById(R.id.right_button);
        Button mailButton = (Button) layout.findViewById(R.id.mailButton);
        Button phoneButton = (Button) layout.findViewById(R.id.phoneButton);
        Button urlButton = (Button) layout.findViewById(R.id.urlButton);
        Button mapButton = (Button) layout.findViewById(R.id.mapButton);
        ImageButton fullScreen = (ImageButton) layout.findViewById(R.id.fullscreenButton);


        //Set fixed text
        descriptionText.setText(getString(R.string.about));
        infoText.setText(getString(R.string.info));


        //Gallery image and iterator
        imageItems = new ArrayList<>();
        imageItems.addAll(Arrays.asList(Constant.GALLERY_IMAGE));
        mainPicture.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageSwitcher.LayoutParams params = new ImageSwitcher.LayoutParams(
                        ImageSwitcher.LayoutParams.MATCH_PARENT, ImageSwitcher.LayoutParams.MATCH_PARENT);

                imageView.setLayoutParams(params);
                return imageView;
            }
        });
        current = 0;
        mainPicture.setImageResource(imageItems.get(current));
        //Listener
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        mailButton.setOnClickListener(this);
        phoneButton.setOnClickListener(this);
        urlButton.setOnClickListener(this);
        fullScreen.setOnClickListener(this);
        mainPicture.setOnClickListener(this);
        mapButton.setOnClickListener(this);

        return layout;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.left_button:
                previous();
                break;
            case R.id.right_button:
                next();
                break;
            case R.id.urlButton:
                String url = getString(R.string.city_website);
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else
                        Toast.makeText(getActivity(), R.string.no_browser_app, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.phoneButton:
                String phone = getString(R.string.phone_number);
                if (phone != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    String uri = "tel: " + phone.trim();
                    intent.setData(Uri.parse(uri));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else
                        Toast.makeText(getActivity(), R.string.no_dial_app, Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getActivity(), R.string.no_phone_available, Toast.LENGTH_SHORT).show();
                break;
            case R.id.mailButton:
                String mail = getString(R.string.info_mail);
                if (mail != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.setType("*/*");
                    String[] addresses = new String[1];
                    addresses[0] = mail;
                    intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else
                        Toast.makeText(getActivity(), R.string.no_mail_app, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fullscreenButton:
                Intent intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(Constant.FROM_GALLERY, true);
                intent.putExtra(Constant.LANDSCAPE_ORIENTATION, true);
                intent.putExtra(Constant.IMAGE, current);
//                String hexColor = String.format("#%06X", (0xFFFFFF & R.color.light_primary_color));
//                intent.putExtra("COLOR", hexColor);
                startActivity(intent);
                break;
            case R.id.main_image:
                intent = new Intent(getActivity(), FullscreenActivity.class);
                intent.putExtra(Constant.FROM_GALLERY, true);
                intent.putExtra(Constant.LANDSCAPE_ORIENTATION, true);
                intent.putExtra(Constant.IMAGE, current);
//                hexColor = String.format("#%06X", (0xFFFFFF & R.color.light_primary_color));
//                intent.putExtra("COLOR", hexColor);
                startActivity(intent);
                break;
            case R.id.mapButton:
                String uri = "geo:0,0?q=41.604451, 13.085299?z=1";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                } else
                    Toast.makeText(getActivity(), R.string.no_map_app, Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }


    private void next() {
        if (current < imageItems.size() - 1) {
            Animation in = AnimationUtils.loadAnimation(getActivity(),
                    android.R.anim.fade_in);
            Animation out = AnimationUtils.loadAnimation(getActivity(),
                    android.R.anim.fade_out);
            mainPicture.setInAnimation(in);
            mainPicture.setOutAnimation(out);
            mainPicture.setImageResource(imageItems.get(++current));

        }
    }

    private void previous() {
        if (current > 0) {
            Animation in = AnimationUtils.loadAnimation(getActivity(),
                    android.R.anim.fade_out);
            Animation out = AnimationUtils.loadAnimation(getActivity(),
                    android.R.anim.fade_in);
            mainPicture.setInAnimation(out);
            mainPicture.setOutAnimation(in);

            mainPicture.setImageResource(imageItems.get(--current));

        }
    }


}
