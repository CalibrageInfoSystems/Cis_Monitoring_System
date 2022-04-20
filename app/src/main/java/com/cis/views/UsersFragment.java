package com.cis.views;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.leanback.app.VerticalGridFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.VerticalGridPresenter;

import com.cis.R;

public class UsersFragment extends VerticalGridFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Title");

        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(4);
        setGridPresenter(gridPresenter);

        ArrayObjectAdapter adapter = new ArrayObjectAdapter(new PizzaCardPresenter());
        setAdapter(adapter);

        adapter.add("Pizza 1");
        adapter.add("Pizza 2");
        adapter.add("Pizza 3");
        adapter.add("Pizza 4");
        adapter.add("Pizza 5");
        adapter.add("Pizza 6 with a very long title which will not fit");
    }

    private class PizzaCardPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            return new ViewHolder(new ImageCardView(parent.getContext()));
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ImageCardView view = (ImageCardView) viewHolder.view;
            view.setMinimumWidth(200);
            Context context = view.getContext();

            String title = (String) item;

            view.setTitleText(title);
            view.setContentText(null);
            view.setMainImage(context.getDrawable(R.drawable.cis_image));
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {

        }
    }
}