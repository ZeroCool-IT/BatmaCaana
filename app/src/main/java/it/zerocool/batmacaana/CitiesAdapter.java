/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.Constant;

/**
 * Adapter for cities' selector
 * Created by Marco Battisti on 25/03/2015.
 */
public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final FragmentManager fragmentManager;
    private final TextView nameTextView;
    private final ImageView avatarIv;
    private final DrawerAdapter adapter;
    private final RecyclerView recyclerView;
    private final DrawerLayout drawerLayout;
    private final FragmentActivity activity;
    private final ImageButton selectorButton;
    private List<City> items = Collections.emptyList();


    public CitiesAdapter(Context context, List<City> data, FragmentManager fm,
                         TextView textView, ImageView iv, DrawerAdapter adapter,
                         RecyclerView rv, DrawerLayout drawerLayout, FragmentActivity activity,
                         ImageButton selectorButton) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.items = data;
        this.fragmentManager = fm;
        this.avatarIv = iv;
        this.nameTextView = textView;
        this.adapter = adapter;
        this.recyclerView = rv;
        this.drawerLayout = drawerLayout;
        this.activity = activity;
        this.selectorButton = selectorButton;
    }


    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int)}. Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public CitiesAdapter.CitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.drawer_row, parent, false);
        return new CitiesViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link android.widget.ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will have
     * the updated adapter position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(CitiesAdapter.CitiesViewHolder holder, int position) {
        City current = items.get(position);
        holder.name.setText(current.getName());
        Picasso.with(context)
                .load(Constant.URI_IMAGE_BIG + current.getAvatar())
                .resize(96, 96)
                .into(holder.avatar);


    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    private void selectItem(int position, boolean closeDrawer) {
        if (position != Constant.ABOUT) {
            ContentFragment f = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.FRAG_SECTION_ID, position);
            f.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, f)
                    .commit();

            activity.setTitle(context.getResources().getStringArray(R.array.drawer_list)[position]);
        } else {
            AboutFragment fragment = new AboutFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constant.FRAG_SECTION_ID, position);
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
        if (closeDrawer) {
            drawerLayout.closeDrawers();
        }
    }

    class CitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView name;
        final ImageView avatar;

        public CitiesViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.listText);
            avatar = (ImageView) itemView.findViewById(R.id.listIcon);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            City city = items.get(this.getAdapterPosition());
            int uid = city.getUserID();
            String name = city.getName();
            nameTextView.setText(name);
            selectorButton.setImageResource(R.drawable.ic_arrow_drop_down_black_18dp);
            Picasso.with(context)
                    .load(Constant.URI_IMAGE_BIG + city.getAvatar())
                    .into(avatarIv);
            Toast.makeText(context, R.string.changing_city, Toast.LENGTH_SHORT).show();
            SharedPreferences sp = context.getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(Constant.CITY_NAME, name);
            editor.putString(Constant.CITY_AVATAR, city.getAvatar());
            editor.putInt(Constant.CITY_UID, uid);
            editor.apply();
            recyclerView.setAdapter(adapter);
            recyclerView.invalidate();
            int defaultView = Integer.parseInt(sp.getString(Constant.KEY_USER_DEFAULT_START_VIEW, "0"));
            selectItem(defaultView, true);

        }
    }
}