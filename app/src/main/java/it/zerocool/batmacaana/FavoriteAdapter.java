/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import it.zerocool.batmacaana.model.Place;
import it.zerocool.batmacaana.utilities.Constraints;

/**
 * Created by Marco Battisti on 02/02/2015.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;
    private List<Place> favoriteItems = Collections.emptyList();


    public FavoriteAdapter(Context context, List<Place> data, FragmentManager fm) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.favoriteItems = data;
        this.fragmentManager = fm;
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
     * avoid unnecessary {@link android.view.View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.result_row, parent, false);
        FavoriteViewHolder holder = new FavoriteViewHolder(view);
        return holder;
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
     * (e.g. in a click listener), use {@link ViewHolder#getPosition()} which will have the
     * updated position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Place current = favoriteItems.get(position);
        holder.header.setText(current.getName());
        holder.description.setText(current.getDescription());
        holder.tags.setText(TextUtils.join(", ", current.getTags()));
        int iconResId = R.drawable.ic_map_grey600_48dp;
        switch (current.getType()) {
            case Constraints.TYPE_TOSEE:
                iconResId = R.drawable.ic_beenhere_grey600_48dp;
                break;
            case Constraints.TYPE_EAT:
                iconResId = R.drawable.ic_local_restaurant_grey600_48dp;
                break;
            case Constraints.TYPE_SLEEP:
                iconResId = R.drawable.ic_local_hotel_grey600_48dp;
                break;
            case Constraints.TYPE_SERVICE:
                iconResId = R.drawable.ic_directions_train_grey600_48dp;
                break;
            case Constraints.TYPE_SHOP:
                iconResId = R.drawable.ic_local_mall_grey600_48dp;
                break;
        }
        Picasso.with(context)
                .load(iconResId)
                .error(R.drawable.ic_map_grey600_48dp)
                .into(holder.icon);

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return this.favoriteItems.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView header;
        TextView description;
        TextView tags;
        ImageView icon;

        public FavoriteViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Place current = favoriteItems.get(getPosition());
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra(Constraints.JSON_ARG, current.getJson());
                    intent.putExtra(Constraints.TYPE_ARG, current.getType());
                    context.startActivity(intent);
                }
            });

            header = (TextView) itemView.findViewById(R.id.search_first_line);
            description = (TextView) itemView.findViewById(R.id.search_second_line);
            tags = (TextView) itemView.findViewById(R.id.search_third_line);
            icon = (ImageView) itemView.findViewById(R.id.resultIcon);
        }
    }

}
