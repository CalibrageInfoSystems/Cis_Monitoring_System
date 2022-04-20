package com.cis.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cis.R;
import com.cis.service.APIConstantURL;
import com.cis.service.ApiService;
import com.cis.service.ServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    TextView fromDate;
    TextView toDate;
    int mHour;
    int mMinute;
    String Todate, from_date, farmatted_Todate, reformattedDate, farmatted_fromdate, reformattedDatee;
    private Context context;
    SliderView sliderView;
    private Subscription mSubscription;
    List<GetDetailScreen> dataa;
    private String systemName;
    TextView nameText;
    ImageView back;
    ImageButton searchBtn;


    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_detail);
        context = this;
        getDetailData();

        fromDate = findViewById(R.id.from_date);
        toDate = findViewById(R.id.to_date);
        sliderView = findViewById(R.id.sliderView);
        searchBtn = findViewById(R.id.searchBtn);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getSearchedData();
            }
        });


        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        from_date = day + "/" + month + "/" + year;
                        fromtimePicker();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Todate = day + "/" + month + "/" + year;
                        totimePicker();
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    private void fromtimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;


                        fromDate.setText(from_date + " " + hourOfDay + ":" + minute);
                        try {
                            Date oneWayTripDate = output.parse(from_date);

                            reformattedDatee = input.format(oneWayTripDate);
                            //  GetAll_tokens_closed();
                            Log.e("===============", "======sending_date===========" + reformattedDatee);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        farmatted_fromdate = reformattedDatee + " " + hourOfDay + ":" + minute;


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void totimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        toDate.setText(Todate + " " + hourOfDay + ":" + minute);
                        try {
                            Date oneWayTripDate = output.parse(Todate);

                            reformattedDate = input.format(oneWayTripDate);
                            //  GetAll_tokens_closed();
                            Log.e("===============", "======sending_date===========" + reformattedDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        farmatted_Todate = reformattedDate + " " + hourOfDay + ":" + minute;
                        Log.e("farmatted_Todate==199", farmatted_Todate);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void getDetailData() {
        Bundle extras = getIntent().getExtras();
        systemName = extras.getString("SystemName");
        Log.d("Nameeee", systemName);
        nameText = findViewById(R.id.nameTxt);
        nameText.setText(systemName);

        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.getDetailScreens(APIConstantURL.DETAIL_URL + systemName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GetDetailScreen>>() {
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
                    public void onNext(final List<GetDetailScreen> data) {

                        int position = 0;


                        List<String> dataa = new ArrayList<>();
                        if (data.get(position).getThumbImage().size() == 0) {
                            dataa.add("http://calibrage.in/assets/images/calibrage-logo.png");
                        } else {
                            for (int i = 0; i < data.get(position).getThumbImage().size(); i++) {
                                dataa.add(data.get(position).getThumbImage().get(i).getImage());
                            }
                        }

                        final SlideAdapter adapter = new SlideAdapter(context, dataa);
                        adapter.setCount(dataa.size());
                        sliderView.setSliderAdapter(adapter);
                        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.startAutoCycle();
                        sliderView.setScrollTimeInSec(8);

                    }

                });
    }

    private void getSearchedData() {

        JsonObject object = searchRequestPageObject();
        ApiService service = ServiceFactory.createRetrofitService(this, ApiService.class);
        mSubscription = service.searchResponsePage(object)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SearchResponse>>() {
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
                    public void onNext(final List<SearchResponse> searchResponse) {

                        int position = 0;


                        List<String> dataa = new ArrayList<>();
                        if (searchResponse.get(position).getThumbImage().size() == 0) {
                            dataa.add("http://calibrage.in/assets/images/calibrage-logo.png");
                            Toast.makeText(context, "Available Screens Count :" + 0, Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < searchResponse.get(position).getThumbImage().size(); i++) {
                                dataa.add(searchResponse.get(position).getThumbImage().get(i).getImage());
                            }
                            Toast.makeText(context, "Available Screens Count :" + searchResponse.get(position).getThumbImage().size(), Toast.LENGTH_LONG).show();
                        }

                        final SlideAdapter adapter = new SlideAdapter(context, dataa);
                        adapter.setCount(dataa.size());
                        sliderView.setSliderAdapter(adapter);
                        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.startAutoCycle();
                        sliderView.setScrollTimeInSec(8);

                    }
                });
    }


    private JsonObject searchRequestPageObject() {

        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date oneWayTripDate = input.parse(Todate);

            String reformattedDate = output.format(oneWayTripDate);
            //  GetAll_tokens_closed();
            Log.e("===============", "======sending_date===========" + reformattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SearchRequest requestModel = new SearchRequest();
        requestModel.setSystemName(systemName);
        requestModel.setFromDate(farmatted_fromdate);
        requestModel.setToDate(farmatted_Todate);
        return new Gson().toJsonTree(requestModel).getAsJsonObject();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
