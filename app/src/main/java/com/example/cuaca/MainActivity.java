package com.example.cuaca;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private RecyclerView _recyclerView1;
    private SwipeRefreshLayout _swipeRefreshLayout1;
    private RootModel _rootModel;
    private TextView _totalTextView;
    private Button _buttonViewCityInfo;

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _recyclerView1 = findViewById(R.id.recyclerView1);
        _totalTextView = findViewById(R.id.totalTextView);

        initSwipeRefreshLayout();
        initButtonViewCityInfo();
        bindRecyclerView1();
    }

    private void initButtonViewCityInfo() {
        _buttonViewCityInfo = findViewById(R.id.buttonView_cityInfo);

        _buttonViewCityInfo.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                CityModel cm = _rootModel.getCityModel();
                CoordModel com = cm.getCoordModel();
                double latitude = com.getLat();
                double longitude = com.getLon();

                Bundle param = new Bundle();
                param.putDouble("lat", latitude);
                param.putDouble("lon", longitude);

                Intent intent = new Intent(MainActivity.this, GpsActivity.class);
                intent.putExtra("param", param);
                startActivity(intent);
            }
        });
    }

    private void initSwipeRefreshLayout()
    {
        _swipeRefreshLayout1 = findViewById(R.id.swipeRefreshLayout1);
        _swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindRecyclerView1();
                _swipeRefreshLayout1.setRefreshing(false);
            }
        });
    }

    private void bindRecyclerView1() {
        String url = "https://api.openweathermap.org/data/2.5/forecast?id=1630789&appid=b7782ad2b41bcbdfec28f79bb9d4cd95";
        AsyncHttpClient ahc = new AsyncHttpClient();

        ahc.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                _rootModel = gson.fromJson(new String(responseBody), RootModel.class);

                iniCityInfo();
                RecyclerView.LayoutManager lm = new LinearLayoutManager(MainActivity.this);
                CuacaAdapter ca = new CuacaAdapter(_rootModel);

                _recyclerView1.setLayoutManager(lm);
                _recyclerView1.setAdapter(ca);
                _totalTextView.setText("Total Record : " + ca.getItemCount());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniCityInfo() {
        CityModel cm = _rootModel.getCityModel();
        String cityName = cm.getName();
        long sunrise = cm.getSunrise();
        long sunset = cm.getSunset();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String sunriseTime = sdf.format(new Date(sunrise * 1000));
        String sunsetTime = sdf.format(new Date(sunset * 1000));

        String cityInfo = "Kota:  "+ cityName + "\n" +
                "Matahari terbit" + sunriseTime + "(Lokal)\n" +
                "Matahari terbenam" + sunsetTime + "(Lokal)";

        _buttonViewCityInfo.setText(cityInfo);
    }
}