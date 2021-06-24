package com.example.splabov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Handler.Callback, SearchView.OnQueryTextListener {

    Handler handler;
    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.handler = new Handler(Looper.myLooper(), this);
        HiloConexion hiloApi = new HiloConexion(this.handler, false, "http://restcountries.eu/rest/v2/all");
        hiloApi.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.agregar){
            Log.d("Click", "Agregar");
        }
        else if(item.getItemId() == R.id.buscar){
            Log.d("Click", "Buscar");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d("Click", "Buscar");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        if(message.arg1 == HiloConexion.DATA){
            this.tvData = super.findViewById(R.id.tvData);
            List<String> data = (List<String>) message.obj;
            tvData.setText(data.toString());
            Log.d("Callback", "Llegaron datos!!!");
        }
//        else if(message.arg1 == HiloConexion.IMAGEN){
//            ImageView iv = super.findViewById(R.id.img);
//            byte[] img = (byte[]) message.obj;
//            iv.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
//            Log.d("Callback", "Llego imagen");
//        }
        return false;
    }
}