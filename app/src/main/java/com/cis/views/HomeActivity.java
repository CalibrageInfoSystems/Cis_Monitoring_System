package com.cis.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cis.R;
import com.cis.service.ApiService;
import com.cis.service.ServiceFactory;
import com.cis.util.ScrollTextView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rcv_users, rcv_eventslist;
    private LinearLayoutManager layoutManager;
    private Context ctx;
    private UserListAdapter adapter;
    private EventAdapter eventAdapter;
    private Subscription mSubscription;
    boolean shouldStopLoop = false;
    Handler mHandler = new Handler();
    private TextView txt_news, txt_desc, txt_event, txt_date;
    private LinearLayout lyt_mainbg;
    private ImageView img_event;
    private ScrollTextView txt_scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);
        init();
        onBindViews();
        // getdata();
        mHandler.removeCallbacksAndMessages(null);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getdata();
                if (!shouldStopLoop) {
                    mHandler.postDelayed(this, 8 * 1000 * 60);
                }
            }
        };
        mHandler.post(runnable);
    }

    private void init() {
        ctx = this;
        rcv_users = findViewById(R.id.rcv_users);
        rcv_eventslist = findViewById(R.id.rcv_eventslist);
        txt_desc = findViewById(R.id.txt_desc);
        img_event = findViewById(R.id.img_event);
        txt_event = findViewById(R.id.txt_event);
        txt_date = findViewById(R.id.txt_date);
        txt_scroll = findViewById(R.id.txt_scroll);

        adapter = new UserListAdapter();
        txt_news = findViewById(R.id.txt_news);
        ImageView imglogo = findViewById(R.id.imagelogo);
        Picasso.with(this).load("http://calibrage.in/assets/images/calibrage-logo.png").into(imglogo);
        lyt_mainbg = findViewById(R.id.lyt_mainbg);
//        Picasso.with(this).load("https://i.pinimg.com/originals/6a/c3/d2/6ac3d23b4c736fe781179bb53f627ab1.jpg").into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                lyt_mainbg.setBackground(new BitmapDrawable(bitmap));
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        });
    }

    private void onBindViews() {
        rcv_users.setLayoutManager(new GridLayoutManager(ctx, 8));
        rcv_eventslist.setLayoutManager(new LinearLayoutManager(this));
        rcv_eventslist.setAdapter(eventAdapter);
        rcv_users.setAdapter(adapter);
        rcv_users.requestFocus();
        getEvents();
    }

    private void getdata() {

        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getlernings("GetTop5Screens")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Getscreen>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(final List<Getscreen> data) {

                        Log.d("DataSize", data.size() + "");

                        adapter.updateData(data);


                    }

                });
    }

    private void getEvents() {

        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getEvents("GetUpCommingEvents")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventsModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(final EventsModel data) {

                        String scrollText = "";
                        Log.d("getEvents", data.getEventsTable().size() + "");
                        eventAdapter = new EventAdapter(data);
                        rcv_eventslist.setAdapter(eventAdapter);

                        if (data.getEventsTable().size() > 0) {
                            Picasso.with(HomeActivity.this).load(data.getEventsTable().get(0).getImageUrl()).error(R.drawable.cis_image).into(img_event);
                            txt_desc.setText(data.getEventsTable().get(0).getDescription());
                            txt_event.setText(data.getEventsTable().get(0).getEventTitle());
                            try {
                                txt_date.setText(formatDateFromDateString(data.getEventsTable().get(0).getEventDate()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        for (int i = 0; i < data.getScrollBarTextTable().size(); i++) {
                            scrollText = scrollText + data.getScrollBarTextTable().get(i).getDailyText() + "         ";
                        }
                        txt_scroll.setText(scrollText);
                    }

                });
    }

    class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.viewHolder> {
        private List<Getscreen> thumbImageList = new ArrayList<>();

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_item, null);
            return new viewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {
            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // Set the height by params
            params.height = 230;
            params.width = 190;
            // set height of RecyclerView
            holder.itemView.setLayoutParams(params);
            holder.nameTxt.setText(thumbImageList.get(position).getSystemName());
            if (thumbImageList.get(position).getThumbImage().size() == 0) {
//                Glide.with(ctx).load("http://calibrage.in/assets/images/calibrage-logo.png").error(R.drawable.cis_image).into(holder.img_slide); new Random().nextInt(array.length)
                Glide.with(ctx).load("http://calibrage.in/assets/images/calibrage-logo.png").error(R.drawable.cis_image).into(holder.img_slide);
            } else {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        String url = thumbImageList.get(position).getThumbImage().get(new Random().nextInt(thumbImageList.get(position).getThumbImage().size())).getThumbImage();
                        Log.d("HomeActivity", "---------------- image -----------url :" + url);
                        Picasso.with(ctx).load(url).error(R.drawable.cis_image).into(holder.img_slide);

                        if (!shouldStopLoop) {
                            mHandler.postDelayed(this, 1000 * 10);
                        }
                    }
                };
                mHandler.post(runnable);
            }

            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        holder.nameTxt.setBackgroundColor(getResources().getColor(R.color.white));
                        holder.nameTxt.setTextColor(getResources().getColor(R.color.orange));
                    } else {
                        holder.nameTxt.setBackgroundColor(getResources().getColor(R.color.half_black));
                        holder.nameTxt.setTextColor(getResources().getColor(R.color.white));
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    String systemName = thumbImageList.get(position).getSystemName();
                    Log.d("SystemName", systemName);
                    intent.putExtra("SystemName", systemName);
                    startActivity(intent);
                }
            });
            holder.img_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    String systemName = thumbImageList.get(position).getSystemName();
                    Log.d("SystemName", systemName);
                    intent.putExtra("SystemName", systemName);
                    startActivity(intent);
                }
            });
            holder.nameTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    String systemName = thumbImageList.get(position).getSystemName();
                    Log.d("SystemName", systemName);
                    intent.putExtra("SystemName", systemName);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (thumbImageList.size() > 0)
                return thumbImageList.size();
            else
                return 0;
        }

        class viewHolder extends RecyclerView.ViewHolder {
            // SliderView sliderView;
            TextView nameTxt;
            ImageView img_slide;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                // sliderView = itemView.findViewById(R.id.sliderView);
                nameTxt = itemView.findViewById(R.id.nameTxt);
                img_slide = itemView.findViewById(R.id.img_slide);
            }
        }

        public void clearAllDataa() {
            thumbImageList.clear();
            notifyDataSetChanged();
        }

        public void updateData(List<Getscreen> viewModels) {
            thumbImageList.clear();
            Log.d("PaymentAdapter", "----- analysis --- Size :" + thumbImageList.size());
            thumbImageList = viewModels;
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("HomeActivity", "onRestart()");
        mHandler.removeCallbacksAndMessages(null);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getdata();
                if (!shouldStopLoop) {
                    mHandler.postDelayed(this, 8 * 1000 * 60);
                }
            }
        };
        mHandler.post(runnable);
    }

    class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        private EventsModel eventsModels;

        public EventAdapter(EventsModel eventsModels) {
            this.eventsModels = eventsModels;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_item_events, null);
            return new EventAdapter.EventViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, final int position) {
            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            // Set the height by params
            params.height = 150;
            // params.width = 190;
            // set height of RecyclerView
            holder.itemView.setLayoutParams(params);
            holder.eventTitle.setText(eventsModels.getEventsTable().get(position).getEventTitle());
            holder.eventDescription.setText(eventsModels.getEventsTable().get(position).getDescription());
            Picasso.with(HomeActivity.this).load(eventsModels.getEventsTable().get(position).getImageUrl()).error(R.drawable.cis_image).into(holder.eventImg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Picasso.with(HomeActivity.this).load(eventsModels.getEventsTable().get(position).getImageUrl()).error(R.drawable.cis_image).into(img_event);
                    txt_desc.setText(eventsModels.getEventsTable().get(position).getDescription());
                    txt_event.setText(eventsModels.getEventsTable().get(position).getEventTitle());
                    try {
                        txt_date.setText(formatDateFromDateString(eventsModels.getEventsTable().get(0).getEventDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (eventsModels.getEventsTable() != null)
                return eventsModels.getEventsTable().size();
            else return 0;
        }

        class EventViewHolder extends RecyclerView.ViewHolder {

            ImageView eventImg;
            TextView eventTitle;
            TextView eventDescription;

            public EventViewHolder(@NonNull View itemView) {
                super(itemView);
                eventImg = itemView.findViewById(R.id.eventImage);
                eventTitle = itemView.findViewById(R.id.eventTitle);
                eventDescription = itemView.findViewById(R.id.eventdescription);
            }
        }
    }

    public static String formatDateFromDateString(String inputDate) throws ParseException {
        Date mParsedDate;
        String mOutputDateString;
        SimpleDateFormat mInputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault());
        SimpleDateFormat mOutputDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", java.util.Locale.getDefault());
        mParsedDate = mInputDateFormat.parse(inputDate);
        mOutputDateString = mOutputDateFormat.format(mParsedDate);
        return mOutputDateString;
    }
}
