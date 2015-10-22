package it.zerocool.batmacaana;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import it.zerocool.batmacaana.model.City;
import it.zerocool.batmacaana.utilities.Constant;

/**
 * Adapter for selection of initial city
 * Created by Marco Battisti on 20/10/2015.
 */
public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.CustomerViewHolder> {

    private List<City> items = Collections.emptyList();
    private final Context context;
    private final LayoutInflater inflater;
    private RadioButton radioSelected;
    private int positionSelected;
    private SharedPreferences.Editor editor;
    private static final int DEFAULT_CITY = 0;

    public CustomersAdapter(Context context, List<City> data)  {
        this.context = context;
        this.items = data;
        this.inflater = LayoutInflater.from(context);
        this.editor = context.getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE).edit();
        City defaultCity = items.get(DEFAULT_CITY);
        positionSelected = DEFAULT_CITY;
        editor.putInt(Constant.CITY_ID, defaultCity.getId());
        editor.putString(Constant.CITY_NAME, defaultCity.getName());
        editor.putString(Constant.CITY_AVATAR, defaultCity.getAvatar());
        editor.putInt(Constant.CITY_UID, defaultCity.getUserID());
        editor.putBoolean(Constant.CITY_PREMIUM, defaultCity.isPremium());
        editor.apply();


    }


    /**
     * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomerViewHolder(inflater.inflate(R.layout.customers_row, parent, false));
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the  to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(RecyclerView.ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        City current = items.get(position);
        holder.title.setText(current.getName());
        Picasso.with(context)
                .load(Constant.URI_IMAGE_BIG + current.getAvatar())
                .resize(96, 96)
                .into(holder.avatar);
        holder.radioButton.setChecked(position == positionSelected);
        holder.radioButton.setClickable(false);
        if (position == DEFAULT_CITY) {
            radioSelected = holder.radioButton;
        }

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

    class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView title;
        final ImageView avatar;
        final RadioButton radioButton;

        public CustomerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            avatar = (ImageView) itemView.findViewById(R.id.listIcon);
            radioButton = (RadioButton) itemView.findViewById(R.id.listRadio);
            itemView.setOnClickListener(this);
//            title.setOnClickListener(this);
//            avatar.setOnClickListener(this);
//            radioButton.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.city_selection_row) {
                radioButton.setChecked(true);
                positionSelected = getLayoutPosition();
                City target = items.get(getAdapterPosition());
                editor.putInt(Constant.CITY_ID, target.getId());
                editor.putString(Constant.CITY_NAME, target.getName());
                editor.putString(Constant.CITY_AVATAR, target.getAvatar());
                editor.putInt(Constant.CITY_UID, target.getUserID());
                editor.putBoolean(Constant.CITY_PREMIUM, target.isPremium());
                editor.apply();
                if (radioSelected != null && !radioSelected.equals(radioButton)) {
                    radioSelected.setChecked(false);
                }
                radioSelected = radioButton;
            }
            notifyDataSetChanged();

        }
    }
}
