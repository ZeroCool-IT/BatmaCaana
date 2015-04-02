/*
 * Copyright ZeroApp(c) 2015. All right reserved.
 */

package it.zerocool.batmacaana;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

import it.zerocool.batmacaana.dialog.AlertsDisablingDialog;
import it.zerocool.batmacaana.utilities.Constant;

/**
 * Navigation Drawer RecyclerView adapter
 * Created by Marco on 05/01/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final FragmentManager fragmentManager;
    private List<DrawerItem> drawerItems = Collections.emptyList();
    private View previousSelected;
    private int currentSelected;
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
        if (viewType == Constant.VIEW_STATE_SELECTED) {
            view = inflater.inflate(R.layout.drawer_row_selected, parent, false);
            previousSelected = view;
        } else if (viewType == Constant.NAV_DRAWER_SUBHEADER) {
            view = inflater.inflate(R.layout.drawer_row_subheader, parent, false);
        } else if (viewType == Constant.ROUTES) {
            view = inflater.inflate(R.layout.drawer_row_last_main, parent, false);
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
//        SharedPreferences sp = context.getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
//        final int defaultView = Integer.parseInt(sp.getString(Constant.KEY_USER_DEFAULT_START_VIEW, "0"));

        if (position == currentSelected)
            return Constant.VIEW_STATE_SELECTED;
        else if (position == Constant.NAV_DRAWER_SUBHEADER)
            return Constant.NAV_DRAWER_SUBHEADER;
        else if (position == Constant.ROUTES)
            return Constant.ROUTES;
        return position;
    }

    private void selectView(View v) {
        TextView title = (TextView) v.findViewById(R.id.listText);
        /*title.setTextColor(context.getResources().getColor(R.color.primaryColor));
        v.setBackgroundColor(context.getResources().getColor(R.color.selected_item));*/
        title.setTextColor(context.getResources().getColor(R.color.primary_text_color));
        v.setBackgroundColor(context.getResources().getColor(R.color.light_primary_color));
        previousSelected = v;
    }

    private void unselectView(View v) {
        if (v != null) {
            TextView title = (TextView) v.findViewById(R.id.listText);
            title.setTextColor(context.getResources().getColor(R.color.primary_text_color));
            v.setBackgroundColor(context.getResources().getColor(R.color.transparent_bg));
        }
    }

    public void setCurrentSelected(int currentSelected) {
        this.currentSelected = currentSelected;
    }

    public void setDrawerLayout(DrawerLayout dl) {
        this.drawerLayout = dl;
    }


    class DrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView title;
        final ImageView icon;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            selectItem(position);
            if (position != Constant.SETTINGS && position != Constant.UPDATE && position != Constant.SUBHEADER && position != Constant.OFFLINE && position != Constant.CREDITS) {
                unselectView(previousSelected);
                selectView(v);
//                setCurrentSelected(position);
            }
        }

        public void selectItem(int position) {

            switch (position) {
                case Constant.ABOUT:
                    AboutFragment fragment = new AboutFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constant.FRAG_SECTION_ID, position);
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                    ((ActionBarActivity) context).setTitle(context.getResources().getStringArray(R.array.drawer_list)[position]);
                    drawerLayout.closeDrawers();
                    break;
                case Constant.SUBHEADER:
                    break;
                case Constant.FAVORITE:
                    FavoriteFragment frag = new FavoriteFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, frag)
                            .commit();
                    ((ActionBarActivity) context).setTitle(context.getResources().getStringArray(R.array.drawer_list)[position]);
                    drawerLayout.closeDrawers();
                    break;
                case Constant.SETTINGS:
                    Intent settingsIntent = new Intent(context, SettingsActivity.class);
                    context.startActivity(settingsIntent);
                    drawerLayout.closeDrawers();
                    break;
                case Constant.CREDITS:
                    Intent creditsIntent = new Intent(context, CreditsActivity.class);
                    context.startActivity(creditsIntent);
                    drawerLayout.closeDrawers();
                    break;
                case Constant.NOTIFICATIONS:
                    DialogFragment alertsFragment = new AlertsDisablingDialog();
                    alertsFragment.show(fragmentManager, "notifications_disable");
                    break;
                default:
                    ContentFragment f = new ContentFragment();
                    Bundle args = new Bundle();
                    args.putInt(Constant.FRAG_SECTION_ID, position);
                    f.setArguments(args);
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


