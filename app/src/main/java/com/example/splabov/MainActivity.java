package com.example.splabov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Handler.Callback, SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    Handler handler;
    TextView tvUsuarios;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    View view;
    JSONArray jsonUsuarios;
    static String selectedRol;
    static Integer lastId;

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
                JSONObject usuario = new JSONObject(this.jsonUsuarios.get(this.jsonUsuarios.length()-1).toString());
                lastId = usuario.getInt("id");
                this.tvUsuarios.setText(this.jsonUsuarios.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.selectedRol = "";
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
            AlertDialog dialog = this.crearDialog();
            dialog.show();
            ListenerDialog ld = new ListenerDialog(this, this.view, dialog, this.pref, this.editor);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(ld);
        }
        else if(item.getItemId() == R.id.buscar){
            Log.d("Click", "Buscar");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        try {
            String rol = "";
            this.jsonUsuarios = new JSONArray(this.pref.getString("jsonArray", null));
            for(int i=0; i<this.jsonUsuarios.length(); i++){
                JSONObject usuario = this.jsonUsuarios.getJSONObject(i);
                if(s.equals(usuario.getString("username"))) {
                    rol = usuario.getString("rol");
                    break;
                }
            }
            if(!"".equals(rol)){
                this.lanzarAlertDialog("Usuario encontrado", "El rol del usuario es "+rol);
            }
            else{
                this.lanzarAlertDialog("Usuario no encontrado", "El usuario "+s+" no esta dentro de la lista");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        try {
            JSONObject usuario = new JSONObject(this.jsonUsuarios.get(this.jsonUsuarios.length()-1).toString());
            lastId = usuario.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Callback", "Llegaron datos!!!");
        return false;
    }

    public AlertDialog crearDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crear Usuario");
        this.view = LayoutInflater.from(this).inflate(R.layout.agregar, null);
        builder.setView(this.view);
        Spinner spRol = this.view.findViewById(R.id.spRol);
        ArrayAdapter<CharSequence> adapterRol = ArrayAdapter.createFromResource(this,
                R.array.rol, android.R.layout.simple_spinner_item);
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRol.setAdapter(adapterRol);
        spRol.setOnItemSelectedListener(this);
        builder.setNeutralButton("Cerrar", null);
        builder.setPositiveButton("Guardar", null);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.selectedRol = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void lanzarAlertDialog(String titulo, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Cerrar", null);
        builder.create();
        builder.show();
    }
}