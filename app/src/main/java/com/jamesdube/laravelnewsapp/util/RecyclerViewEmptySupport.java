package com.jamesdube.laravelnewsapp.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.MainActivity;
import com.jamesdube.laravelnewsapp.R;

import java.util.List;

import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_ARCHIVED;
import static com.jamesdube.laravelnewsapp.util.Constants.POSTS_FAVOURITES;

/**
 * Created by jdube on 2/23/17.
 */

public class RecyclerViewEmptySupport extends RecyclerView {
    private View emptyView;

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && emptyView != null) {
                if(adapter.getItemCount() == 0) {
                    //determine the current fragment
                    switch (MainActivity.Title)
                    {
                        case POSTS_ARCHIVED:
                            setEmptyViewTitle(getResources().getString(R.string.emptyViewTitleArchived));
                            setEmptyViewDescription(getResources().getString(R.string.emptyViewDescriptionArchived));
                            setEmptyViewIcon(R.drawable.ic_archive);
                            break;

                        case POSTS_FAVOURITES:
                            setEmptyViewTitle(getResources().getString(R.string.emptyViewTitleFavourite));
                            setEmptyViewDescription(getResources().getString(R.string.emptyViewDescriptionFavourite));
                            setEmptyViewIcon(R.drawable.ic_favorite_true);
                            break;

                        default:
                            setEmptyViewTitle(getResources().getString(R.string.emptyViewTitleDefault));
                            setEmptyViewDescription(getResources().getString(R.string.emptyViewDescriptionDefault));
                            setEmptyViewIcon(R.drawable.ic_home);
                            break;
                    }
                    Log.d(App.Tag,"empty view "+MainActivity.Title);
                    emptyView.setVisibility(View.VISIBLE);
                    RecyclerViewEmptySupport.this.setVisibility(View.GONE);
                }
                else {
                    emptyView.setVisibility(View.GONE);
                    RecyclerViewEmptySupport.this.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    public RecyclerViewEmptySupport(Context context) {
        super(context);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEmptySupport(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if(adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    /**
     * Set the title of the empty view
     * @param title
     */
    private void setEmptyViewTitle(String title){
        TextView viewTitle = (TextView) emptyView.findViewById(R.id.emptyViewTitle);
        viewTitle.setText(title);
    }

    /**
     * Set the icon/drawable resource of the empty view
     */
    private void setEmptyViewIcon(int drawable){
        Drawable d = getResources().getDrawable(drawable);

        d.setColorFilter(App.getAppContext().getResources().getColor(Themes.getColorAccent()), PorterDuff.Mode.SRC_IN);
        ImageView imageView = (ImageView) emptyView.findViewById(R.id.emptyViewImage);
        Glide.with(App.getAppContext())
                .load(drawable).placeholder(d)
                .into(imageView);
    }

    /**
     * Set the description of the empty view
     * @param description
     */
    private void setEmptyViewDescription(String description){
        TextView viewTitle = (TextView) emptyView.findViewById(R.id.emptyViewDescription);
        viewTitle.setText(description);
    }

}
