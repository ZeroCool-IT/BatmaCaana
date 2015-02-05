/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import it.zerocool.batmacaana.utilities.Constraints;
import it.zerocool.batmacaana.utilities.SharedPreferencesProvider;

/**
 * Created by Marco on 05/01/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    List<DrawerItem> drawerItems = Collections.emptyList();
    private Context context;
    private LayoutInflater inflater;
    private View previousSelected;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;


    public DrawerAdapter(Context context, List<DrawerItem> data, FragmentManager fm) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.drawerItems = data;
        this.fragmentManager = fm;

    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == Constraints.VIEW_STATE_SELECTED) {
            view = inflater.inflate(R.layout.drawer_row_selected, parent, false);
            previousSelected = view;
        } else if (viewType == Constraints.NAV_DRAWER_SUBHEADER) {
            view = inflater.inflate(R.layout.drawer_row_subheader, parent, false);
        } else if (viewType == Constraints.ABOUT) {
            view = inflater.inflate(R.layout.drawer_row_last_main, parent, false);
        } else if (viewType == Constraints.OFFLINE) {
            view = inflater.inflate(R.layout.drawer_row_offline_toggle, parent, false);
        } else {
            view = inflater.inflate(R.layout.drawer_row, parent, false);
        }
        return new DrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        DrawerItem current = drawerItems.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconID);
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p/>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        SharedPreferences sp = SharedPreferencesProvider.getSharedPreferences(context);
        final int defaultView = sp.getInt(Constraints.KEY_USER_DEFAULT_START_VIEW, 0);

        if (position == defaultView)
            return Constraints.VIEW_STATE_SELECTED;
        else if (position == Constraints.NAV_DRAWER_SUBHEADER)
            return Constraints.NAV_DRAWER_SUBHEADER;
        else if (position == Constraints.ABOUT)
            return Constraints.ABOUT;
        else if (position == Constraints.OFFLINE)
            return Constraints.OFFLINE;
        return super.getItemViewType(position);
    }

    private void selectView(View v) {
        TextView title = (TextView) v.findViewById(R.id.listText);
        title.setTextColor(context.getResources().getColor(R.color.primaryColor));
        v.setBackgroundColor(context.getResources().getColor(R.color.selected_item));
        previousSelected = v;
    }

    private void unselectView(View v) {
        if (v != null) {
            TextView title = (TextView) v.findViewById(R.id.listText);
            title.setTextColor(context.getResources().getColor(R.color.primary_text_color));
            v.setBackgroundColor(context.getResources().getColor(R.color.transparent_bg));
        }
    }

    public void setDrawerLayout(DrawerLayout dl) {
        this.drawerLayout = dl;
    }


    class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView icon;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            selectItem(getPosition());
            if (position != Constraints.SETTINGS && position != Constraints.UPDATE && position != Constraints.SUBHEADER && position != Constraints.OFFLINE) {
                unselectView(previousSelected);
                selectView(v);
            }
        }

        public void selectItem(int position) {

            switch (position) {
                case Constraints.ABOUT:
                    break;
                case Constraints.SUBHEADER:
                    break;
                case Constraints.FAVORITE:
                    FavoriteFragment frag = new FavoriteFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, frag)
                            .commit();
                    ((ActionBarActivity) context).setTitle(context.getResources().getStringArray(R.array.drawer_list)[position]);
                    drawerLayout.closeDrawers();
                    break;
                case Constraints.SETTINGS:
                    break;
                case Constraints.OFFLINE:
                    break;
                case Constraints.UPDATE:
                    break;
                default:
                    ContentFragment f = new ContentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constraints.FRAG_SECTION_ID, position);
                    f.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, f)
                            .commit();
                    ((ActionBarActivity) context).setTitle(context.getResources().getStringArray(R.array.drawer_list)[position]);
                    drawerLayout.closeDrawers();
                    break;
            }
        }

    }
}


