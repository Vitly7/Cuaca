package com.example.cuaca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.List;

public class CuacaAdapter extends RecyclerView.Adapter<CuacaAdapter.ViewHolder> {
    private List<ListModel> listModelList;
    private RootModel rm;
    public CuacaAdapter(RootModel rm) {
        this.rm = rm;
        listModelList = rm.getListModelList();
    }
    @NonNull
    @Override
    public CuacaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cuaca, parent, false);
        return new CuacaViewHolder(view);
    }
    private double toCelcius(double kelvin) { return kelvin - 272.15; }
    public String formatNumber (double number, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);
    }

    @Override
    public void onBindViewHolder(@NonNull CuacaViewHolder holder, int position) {
        ListModel lm = listModelList.get(position);
        WeatherModel wm = lm.getWeatherModelList().get(0);
        MainModel mm = lm.getMainModel();

        String suhu = formatNumber(toCelcius(mm.getTemp_min()), "###.##") + "°C - " + formatNumber(toCelcius(mm.getTemp_max()), "###.##") + "°C";

        switch (wm.getIcon()){
            case "01d":
                holder.cuacaImageView.setImageResource(R.mipmap.ic_01d);
                break;
        }
    }
}
