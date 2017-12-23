package com.dovizkuru;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private TextView txtDolarAlis,txtDolarSatis,txtEuroAlis,txtEuroSatis;
    private Database kurDatabase;
    private KurBilgileri kurBilgileri;
    private ListView lstView;
    private RadioButton radioDolar,radioEuro;

    private void init(){
        txtDolarAlis=findViewById(R.id.txtDolarAlis);
        txtDolarSatis=findViewById(R.id.txtDolarSatis);
        txtEuroAlis=findViewById(R.id.txtEuroAlis);
        txtEuroSatis=findViewById(R.id.txtEuroSatis);
        lstView=findViewById(R.id.lstView);
        radioDolar=findViewById(R.id.radioDolar);
        radioEuro=findViewById(R.id.radioEuro);
        kurBilgileri=new KurBilgileri();
        kurDatabase=new Database(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void onVerileriGoster(View view) {
        new DovizBilgileri().execute();
    }

    public void onKaydet(View view) {
          kurDatabase.dovizKuruEkle(kurBilgileri);
    }

    public void onListele(View view) {
        List<String> result=new ArrayList<>();
        String gecici="";

        if (radioDolar.isChecked()){
            for(KurBilgileri kur:kurDatabase.getAllList()){
                gecici="Alış : "+kur.getDolarAlis()+ "Satış : "+kur.getDolarSatis();
                result.add(gecici);
            }
        }

        else if(radioEuro.isChecked()) {
            for(KurBilgileri kur:kurDatabase.getAllList()){
                gecici="Alış : "+kur.getEuroAlis()+ "Satış : "+kur.getEuroSatis();
                result.add(gecici);
            }
        }
        else {
            Toast.makeText(this, "Seçim yapınız", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                this,android.R.layout.simple_list_item_1,result);
        lstView.setAdapter(arrayAdapter);

        //Nesne olarak listeyi ekrana basıyor.
       /* ArrayAdapter<KurBilgileri> arrayAdapter=new ArrayAdapter<>(
                this,android.R.layout.simple_list_item_1,kurDatabase.getAllList());
        lstView.setAdapter(arrayAdapter);*/
    }

    public class DovizBilgileri extends AsyncTask<String,String,String>{

        private KurBilgileri kurlar=new KurBilgileri();
        private String URL_KAYNAK="http://www.doviz.gen.tr/doviz_json.asp";
        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection connection=null;
            BufferedReader bufferedReader=null;

            try{
                URL url=new URL(URL_KAYNAK);
                connection=(HttpURLConnection)url.openConnection();
                connection.connect();
                bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String satir;
                String dosya="";
                while((satir=bufferedReader.readLine())!=null){
                    dosya+=satir;
                }

                JSONObject jsonObject=new JSONObject(dosya);//Veriler JSON formatında döner
                kurlar.setDolarAlis(jsonObject.getString("dolar"));
                kurlar.setDolarSatis(jsonObject.getString("dolar2"));
                kurlar.setEuroAlis(jsonObject.getString("euro"));
                kurlar.setEuroSatis(jsonObject.getString("euro2"));
                kurBilgileri=kurlar;
                return dosya;
            }
            catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            txtDolarAlis.setText(kurlar.getDolarAlis());
            txtDolarSatis.setText(kurlar.getDolarSatis());
            txtEuroAlis.setText(kurlar.getEuroAlis());
            txtEuroSatis.setText(kurlar.getEuroSatis());
        }
    }
}
