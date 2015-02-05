/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import it.zerocool.batmacaana.dialog.WarningDialog;
import it.zerocool.batmacaana.model.SearchResult;
import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.ParsingUtilities;
import it.zerocool.batmacaana.utilities.RequestUtilities;

/**
 * Created by Marco on 27/01/2015.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;
    private List<SearchResult> searchItems = Collections.emptyList();


    public SearchAdapter(Context context, List<SearchResult> data, FragmentManager fm) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.searchItems = data;
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
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.result_row, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);
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
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        SearchResult current = searchItems.get(position);
        holder.header.setText(current.getheader());
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
            case Constraints.TYPE_EVENT:
                iconResId = R.drawable.ic_event_note_grey600_48dp;
                break;
            case Constraints.TYPE_NEWS:
                iconResId = R.drawable.ic_newspaper_grey600_48dp;
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
        return this.searchItems.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView header;
        TextView description;
        TextView tags;
        ImageView icon;

        public SearchViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchResult current = searchItems.get(getPosition());
                    int id = current.getId();
                    int type = current.getType();
                    String uri = Constraints.OBJECT_SEARCH1 +
                            Constraints.USER_ID +
                            Constraints.OBJECT_SEARCH2 +
                            Integer.valueOf(id).toString() +
                            Constraints.OBJECT_SEARCH3 +
                            Integer.valueOf(type).toString();
                    RequestObjectTask task = new RequestObjectTask();
                    task.execute(uri, Integer.valueOf(type).toString());
                }
            });
            header = (TextView) itemView.findViewById(R.id.search_first_line);
            description = (TextView) itemView.findViewById(R.id.search_second_line);
            tags = (TextView) itemView.findViewById(R.id.search_third_line);
            icon = (ImageView) itemView.findViewById(R.id.resultIcon);
        }
    }

    private class RequestObjectTask extends AsyncTask<String, Void, String> {

        private int type;

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {
            String res = null;
            String uri = params[0];
            type = Integer.parseInt(params[1]);
            try {
                InputStream is = RequestUtilities.requestInputStream(uri);
                String json = RequestUtilities.inputStreamToString(is);
                res = ParsingUtilities.parseSingleResult(json);
            } catch (IOException e) {
                Log.e("ZCLOG", e.getMessage());
//                this.cancel(true);
                return null;
            }
            return res;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param s The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals(Constraints.EMPTY_VALUE)) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Constraints.JSON_ARG, s);
                intent.putExtra(Constraints.TYPE_ARG, type);
                context.startActivity(intent);
            } else {
                String title, message;
                Bundle args = new Bundle();
                WarningDialog dialog = new WarningDialog();
                title = context.getResources().getString(R.string.dialog_title_uhoh);
                message = context.getResources().getString(R.string.dialog_message_error);
                args.putString(WarningDialog.TITLE, title);
                args.putString(WarningDialog.MESSAGE, message);
                args.putBoolean(WarningDialog.KILL, true);
                dialog.setArguments(args);
                dialog.show(fragmentManager, "Error retrieving data");
            }
        }
    }
}
