package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainairActivity extends AppCompatActivity {

    private static final String TAG = MainairActivity.class.getSimpleName();
    int region;
    int metal;
    String Date="99991231";
    String URL_FORMAT ;
    int ct;
    private static RequestQueue requestQueue;
    private LinearLayout linearLayoutInfos;
    private LayoutInflater inflater;
    RadioGroup region_group;
    RadioGroup metal_group;
    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    public static String format_yyyyMMdd = "yyyyMMdd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airmain);
        this.InitializeView();
        this.InitializeListener();
        region=1;
        metal=90303;

        linearLayoutInfos = findViewById(R.id.LayoutInfos);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());


        region_group = findViewById(R.id.region_group);
        region_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(i==R.id.region_1){
                    region=1;

                }
                else if(i==R.id.region_2){
                    region=7;

                }
                else if(i==R.id.region_3){
                    region=6;

                }
                else if(i==R.id.region_4){
                    region=3;

                }
                else if(i==R.id.region_5){
                    region=10;

                }
            }
        });


        metal_group = findViewById(R.id.metal_group);
        metal_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(i==R.id.metal_1){
                    metal=90303;
                }
                else if(i==R.id.metal_2){
                    metal=90319;
                }
            }
        });

        findViewById(R.id.buttonSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL_FORMAT = "http://apis.data.go.kr/1480523/MetalMeasuringResultService/MetalService?numOfRows=12&pageNo=1&resultType=json&stationcode="+region+"&date="+ Date +"&timecode=RH02&itemcode="+metal+"&serviceKey=xAgJmtDIUhhkXhth%2FgR5Bbq785jllSNicZ%2B1ZBGRp3peCg8qDS%2FDEfliWHjxZUw08JplIQM1hjVczO1SOZ9M9A%3D%3D";
                    makeRequest();
            }
        });




    }


    public void makeRequest() {
        String url = URL_FORMAT;

        StringRequest request = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"응답 -> " + response);

                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"에러 -> " + error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        Log.d(TAG,"요청 보냄.");
    }


    private void processResponse(String response) {
        Gson gson = new Gson();
        AirList airList = gson.fromJson(response, AirList.class);
        System.out.println(URL_FORMAT);

        if(airList.MetalService.item.size()!=0){
            findViewById(R.id.imageView2).setVisibility(View.GONE);
            for (int i =airList.MetalService.item.size()-1; i>=0 ; i--){
                if(i<5)
                    addAir("0"+(i*2)+"시",airList.MetalService.item.get(i).VALUE);
                else
                    addAir(""+(i*2)+"시",airList.MetalService.item.get(i).VALUE);
            }
            if(metal==90303)
                addAirF(Date, "납(단위:ng/m3)");
            else if(metal==90319)
                addAirF(Date, "칼슘(단위:ng/m3)");
        }
        else if(Date=="99991231")
            Toast.makeText(this, "날짜를 입력해주세요.", Toast.LENGTH_LONG).show();
        else if(Integer.parseInt(Date)>ct)
            Toast.makeText(this, "잘못된 날짜입니다.", Toast.LENGTH_LONG).show();
        else
        Toast.makeText(this, "측정된 값이 없습니다.", Toast.LENGTH_LONG).show();


    }


    public void addAirF(String time, String value_n){
        int ymd=Integer.parseInt(time);
        int y=ymd/10000;
        int m=(ymd/100)-(y*100);
        int d=ymd%100;
        String sregion="수도권";
        if(region==1){
            sregion="수도권";
        }
        else if(region==7){
            sregion="경기권";
        }
        else if(region==6){
            sregion="영남권";
        }
        else if(region==3){
            sregion="호남권";
        }
        else if(region==10){
            sregion="강원권";
        }
        String clock=y+"년 "+m+"월 "+d+"일 ("+sregion+")";
        ViewGroup air_info = (ViewGroup) inflater.inflate(R.layout.activity_air_info, linearLayoutInfos, false);
        ((TextView) air_info.findViewById(R.id.textViewName)).setText(clock);
        ((TextView) air_info.findViewById(R.id.textViewFilm)).setText(value_n);
        linearLayoutInfos.addView(air_info, 0);
    }
    public void addAir(String time, String value_n){
        ViewGroup air_info = (ViewGroup) inflater.inflate(R.layout.activity_air_info, linearLayoutInfos, false);
        ((TextView) air_info.findViewById(R.id.textViewName)).setText(time);
        ((TextView) air_info.findViewById(R.id.textViewFilm)).setText(value_n);
        linearLayoutInfos.addView(air_info, 0);
    }

    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.editTextDate);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                month++;
                int time=(year*10000)+(month*100)+day;

                textView_Date.setText(year + "년" + month + "월" + day + "일");
                Date=time+"";
            }
        };
    }

    public void OnClickHandler(View view){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_yyyyMMdd, Locale.getDefault());
        String current = format.format(currentTime);
        int time=Integer.parseInt(current);
        ct=time;
        int y=time/10000;
        int m=(time/100)-(y*100)-1;
        int d=time%100;

        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, y, m, d);

        dialog.show();
    }
}
