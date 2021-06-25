package com.example.splabov;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListenerDialog implements View.OnClickListener {

    Activity a;
    View v;
    AlertDialog dialog;
    EditText etNombre;
    Spinner spRol;
    ToggleButton tbAdmin;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public ListenerDialog(Activity a, View v, AlertDialog dialog, SharedPreferences pref, SharedPreferences.Editor editor){
        this.a = a;
        this.v = v;
        this.dialog = dialog;
        this.pref = pref;
        this.editor = editor;
    }

    @Override
    public void onClick(View view) {
        this.etNombre = this.v.findViewById(R.id.etNombre);
        this.spRol = this.v.findViewById(R.id.spRol);
        this.tbAdmin = this.v.findViewById(R.id.tbAdmin);
        if(!"".equals(this.etNombre.getText().toString()) && !"".equals(MainActivity.selectedRol)){
            try {
                int id = MainActivity.lastId + 1;
                MainActivity.lastId = id;
                String stringUsuario = "{'id':"+id+",'username':'"+this.etNombre.getText().toString()+"','rol':'"+MainActivity.selectedRol+"','admin':"+this.tbAdmin.isChecked()+"}";
                JSONObject usuario = new JSONObject(stringUsuario);
                JSONArray jsonUsuarios = new JSONArray(this.pref.getString("jsonArray", null));
                jsonUsuarios.put(usuario);
                this.editor.putString("jsonArray", jsonUsuarios.toString());
                this.editor.commit();
                TextView tvUsuarios = this.a.getWindow().getDecorView().findViewById(R.id.tvUsuarios);
                tvUsuarios.setText(this.pref.getString("jsonArray", null));
                this.dialog.dismiss();
                Toast.makeText(this.a, "Usuario "+usuario.getString("username")+" agregado!", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this.a, "Se deben completar todos los campos.", Toast.LENGTH_LONG).show();
        }
    }
}
