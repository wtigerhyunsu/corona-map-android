package com.example.p502;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    ListView listView;
    LinearLayout container,third_layout;
    ArrayList<Item> list;
    ArrayList<corona_location> loc2;
    ArrayList<corona_location> loc;
    ItemAdapter itemAdapter;
    ProgressDialog progressDialog;
    Intent intent_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        listView = findViewById(R.id.listView);
        container = findViewById(R.id.container);
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(SecondActivity.this);
        getData();
        third_layout = findViewById(R.id.third_layout);
    }

    private void getData() {

        String url = "http://52.78.108.32/webview/corona.jsp";
        ItemAsync itemAsync = new ItemAsync(url);
        itemAsync.execute();

    }

    // Item을 받아오는 AsyncTask ItemAsync 생성 //
    class ItemAsync extends AsyncTask<Void,Void,String>{

        String url;

        public ItemAsync(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("HTTP Connect");
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            result = HttpHandler.getString(url);
            // result 에는 jsp 에서 불러온 데이터 (json) 이 들어온다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // 받아온 JSON 데이터를 찍는로그 //
           // Log.d("---",s.trim());

            progressDialog.dismiss();

            JSONArray ja = null;

            JSONArray ja2 = null;

            JSONObject jo2 = null;

            ArrayList<corona_location> loc = null;
                try {
                    // 전체 정보를 담을 JSONArray ja //
                    ja = new JSONArray(s);

                    // Location 정보를 따로 담는데 쓰일 ja2 //
                    ja2 = new JSONArray(s);

                    for (int i =0; i<ja.length();i++){

                        JSONObject jo = ja.getJSONObject(i);

                        ja2 = jo.getJSONArray("location");
                        // loc 라는 Location_Corona 클래스를 담을 Arraylist 생성 //
                        loc = new ArrayList<>();

                        for(int j=0; j<ja2.length();j++){

                            jo2 = ja2.getJSONObject(j);

                            int loc_no = jo2.getInt("no");
                            String loc_name = jo2.getString("name");
                            double loc_lat = jo2.getDouble("lat");
                            double loc_lon = jo2.getDouble("lon");

                            corona_location corona_location = new corona_location(loc_no,loc_name,loc_lat,loc_lon);

                            loc.add(corona_location);


                           // Log.d("---",loc.size()+"");

                        }
                      //  Log.d("---",loc.size()+""); // 여기서 사이즈가 66이 나오는지 확인


                        // 담은 데이터를 보내주고 화면을 전환시킬 Intent_map 생성 //
                        intent_map = new Intent(getApplicationContext(), MapsActivity.class);

                        //  환자의 전체 정보를 담는다 . //
                        String name = jo.getString("name");
                        String id = jo.getString("id");
                        String img = jo.getString("img");
                        String nationality = jo.getString("nationality");
                        String sex = jo.getString("sex");

                        int age = jo.getInt("age");
                        int meet = jo.getInt("meet");
                        int risk = jo.getInt("risk");

                        //  환자의 전체 정보를 Item 클래스에 담는다 . //
                        Item item = new Item(id,name,sex,age,nationality,meet,risk,img,loc);

                        // Item 을 담는 ArrayList list 에 Item 을 넣는다. //
                        list.add(item);
                        Log.d("---",list.size()+"");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 커스텀한 ItemAdapter 를 이용하여 listView 에 list 정보를 넣어준다. //

                itemAdapter = new ItemAdapter(list);
                listView.setAdapter(itemAdapter);


                // 위에서 corona_location 정보를 넣었던 loc 를 listView 에서 포지션 값에 따라 받아온다. //
            final ArrayList<corona_location> finalLoc = loc;

                // listVIew 를 클릭하면 실행 //
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<corona_location> alist = list.get(position).getLoc();
                        // loc라는 이름으로 를 alist 를 보낸다. //
                        Log.d("---",alist.size()+"");
                        intent_map.putExtra("loc", alist);
                        //액티비티가 계속 생기지 않도록 플래그 설정 //
                        intent_map.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        startActivity(intent_map);
                    }
                });
            }

        }

        // 버튼 클릭시 loc2 를 생성 , location 정보 전체값을 뿌려준다. //
        public void ckbt(View v){

        loc2 = new ArrayList<>();

            for(int i=0;i<list.size();i++){
                for(int j=0;j<list.get(i).getLoc().size();j++){
                    loc2.add(list.get(i).getLoc().get(j));
                }
            }

            intent_map = new Intent(getApplicationContext(), MapsActivity.class);
            intent_map.putExtra("loc", loc2);
            intent_map.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent_map);
            Log.d("---loc2size",loc2.size()+"");


        }

        class ItemAdapter extends BaseAdapter{

            ArrayList<Item> alist;

            public ItemAdapter(ArrayList<Item> alist) {
                this.alist = alist;
            }


            @Override
            public int getCount() {
                return alist.size();
            }

            @Override
            public Object getItem(int position) {
                return alist.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View ItemView = null;
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ItemView = inflater.inflate(R.layout.corona_layout,container,true);
                // JSON 유형에 맞춰서 변경해야함 //
                TextView text_no = ItemView.findViewById(R.id.text_no);
                TextView text_nation = ItemView.findViewById(R.id.text_nation);
                TextView text_sex = ItemView.findViewById(R.id.text_sex);
                TextView text_age = ItemView.findViewById(R.id.text_age);
                RatingBar ratingBar = ItemView.findViewById(R.id.ratingBar);

                final ImageView imageView = ItemView.findViewById(R.id.imageView);

                text_no.setText(alist.get(position).getName());
                text_nation.setText(alist.get(position).getNationality());
                text_age.setText(alist.get(position).getAge()+"");
                text_sex.setText(alist.get(position).getSex());
                ratingBar.setRating(alist.get(position).getRisk());

                ArrayList<corona_location> loc = alist.get(position).getLoc();


                // 비트맵 이미지 서버에서 받아오기 //
                String img = alist.get(position).getImg();

                img = "http://52.78.108.32:8080/webview/img/"+img;

                final String finalImg = img;
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        URL url = null;
                        InputStream is = null;
                        try {
                            url = new URL(finalImg);
                            is = url.openStream();
                            final Bitmap bm = BitmapFactory.decodeStream(is);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(bm);
                                }
                            });


                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                });


                t.start();
                return ItemView;
            }
        }

    }



