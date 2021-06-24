package com.example.splabov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements Handler.Callback, SearchView.OnQueryTextListener {

    Handler handler;
    TextView tvUsuarios;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    View view;
    JSONArray jsonUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tvUsuarios = super.findViewById(R.id.tvUsuarios);
        this.pref = getPreferences(Context.MODE_PRIVATE);
        this.editor = this.pref.edit();
        try {
            if(!this.pref.contains("jsonArray")){
                this.handler = new Handler(Looper.myLooper(), this);
                HiloConexion hiloApi = new HiloConexion(this.handler,"http://10.0.2.2:3001/usuarios");
                hiloApi.start();
            }
            else{
                this.jsonUsuarios = new JSONArray(this.pref.getString("jsonArray", null));
                this.tvUsuarios.setText(this.jsonUsuarios.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        this.tvUsuarios = super.findViewById(R.id.tvUsuarios);
        this.jsonUsuarios = (JSONArray) message.obj;
        this.editor.putString("jsonArray", this.jsonUsuarios.toString());
        this.editor.commit();
        tvUsuarios.setText(this.jsonUsuarios.toString());
        Log.d("Callback", "Llegaron datos!!!");
        return false;
    }
}