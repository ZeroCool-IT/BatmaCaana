/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.Constant;

/**
 * Cities' notification management adapter
 * Created by Marco Battisti on 15/05/2015.
 */
public class CityNotificationAdapter extends RecyclerView.Adapter<CityNotificationAdapter.CityHolder> {


    private final Context context;
    private final LayoutInflater inflater;
    private List<City> citiesList = Collections.emptyList();

    public CityNotificationAdapter(Context context, List<City> data) {
        inflater = LayoutInflater.from(context);
        this.citiesList = data;
        this.context = context;

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
    public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notification_list_row, parent, false);
        return new CityHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this
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
    public void onBindViewHolder(CityHolder holder, int position) {
        City current = citiesList.get(position);
        holder.cityName.setText(current.getName());
        Picasso.with(context)
                .load(Constant.URI_IMAGE_BIG + current.getAvatar())
                .into(holder.cityAvatar);


    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    class CityHolder extends RecyclerView.ViewHolder {

        final TextView cityName;
        final ImageView cityAvatar;
        final CheckBox eventCheck;
        final CheckBox newsCheck;

        public CityHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.city_name);
            eventCheck = (CheckBox) itemView.findViewById(R.id.event_check);
            newsCheck = (CheckBox) itemView.findViewById(R.id.news_check);
            cityAvatar = (ImageView) itemView.findViewById(R.id.city_logo);
        }
    }
}
