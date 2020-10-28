package com.example.lin01;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class list extends AppCompatActivity {
    String TAG = list.class.getSimpleName()+"My";
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        catchData();
    }



    private void catchData(){
        String catchData = " https://datacenter.taichung.gov.tw/swagger/OpenData/31dd220b-c094-4758-9b22-9f72853bc991";
        ProgressDialog dialog = ProgressDialog.show(this,"讀取中"
                ,"請稍候",true);

        new Thread(()->{
            try {
                URL url = new URL(catchData);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                StringBuffer json = new StringBuffer();
                while (line != null) {
                    json.append(line);
                    line = in.readLine();
                }
                JSONArray jsonArray = new JSONArray(String.valueOf(json));
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String num = jsonObject.getString("編號");
                    String dd = jsonObject.getString("區別");
                    String call = jsonObject.getString("名稱");
                    String carof1 = jsonObject.getString("車位數(大客車)");
                    String carof2 = jsonObject.getString("車位數(汽車)");
                    String carof3 = jsonObject.getString("車位數(機車)");
                    String loa = jsonObject.getString("設置地點");
                    String aa = jsonObject.getString("構造方式");
                    String rate = jsonObject.getString("收費費率");
                    String time = jsonObject.getString("收費時間");
                    String ps = jsonObject.getString("備註");


                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("location",loa);
                    hashMap.put("car",carof2);
                    hashMap.put("motorcar",carof3);
                    hashMap.put("price",rate);

                    arrayList.add(hashMap);
                }
                Log.d(TAG,""+json);
                runOnUiThread(()->{
                    dialog.dismiss();
                    RecyclerView recyclerView;
                    MyAdapter myAdapter;
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                    myAdapter = new MyAdapter();
                    recyclerView.setAdapter(myAdapter);

                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();


    }
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
            return new ViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvLoa.setText(arrayList.get(position).get("location"));
            holder.tvCar1.setText("汽車:"+arrayList.get(position).get("car"));
            holder.tvCar2.setText("機車:"+arrayList.get(position).get("motorcar"));
            holder.tvPrice.setText("收費"+arrayList.get(position).get("price"));


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvLoa,tvCar1,tvCar2,tvPrice,tvTime;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvLoa = itemView.findViewById(R.id.textView_loa);
                tvCar1 = itemView.findViewById(R.id.textView_car1);
                tvCar2 = itemView.findViewById(R.id.textView_car2);
                tvPrice = itemView.findViewById(R.id.textView_price);

            }
        }
    }
}