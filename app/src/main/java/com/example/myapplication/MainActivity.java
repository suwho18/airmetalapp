package com.example.myapplication;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Button = (Button) findViewById(R.id.confirm);
        Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainairActivity.class);
                startActivity(intent);
            }
        });

        Button Button2 = (Button) findViewById(R.id.sub);
        Button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("대기환경연구소 대기오염 측정자료를 기준으로 각 지역 대기중 중금속 성분을 2시간 평균 측정 자료의 정보를 조회할 수있습니다.")
                        .setPositiveButton("확인", null)
                        .show();
            }
        });
        
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("네", (dialog, which) ->  { finish(); })
                .setNegativeButton("아니요", null)
                .show();
    }

}
