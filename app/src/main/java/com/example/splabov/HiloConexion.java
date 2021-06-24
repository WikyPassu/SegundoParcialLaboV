package com.example.splabov;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HiloConexion extends Thread {

    public static final int IMAGEN = 1;
    public static final int DATA = 2;
    Handler handler;
    Boolean img;
    String url;

    public HiloConexion(Handler handler, boolean img, String url){
        this.handler = handler;
        this.img = img;
        this.url = url;
    }

    @Override
    public void run() {
        try{
            ConexionHTTP conexionHTTP = new ConexionHTTP();
            if(!img){
                byte[] dataJson = conexionHTTP.obtenerRespuesta(this.url);
                String s = new String(dataJson);
                Message msg = new Message();
                msg.arg1 = DATA;
                msg.obj = this.parserJson(s);
                this.handler.sendMessage(msg);

            }
            else{
                byte[] img = conexionHTTP.obtenerRespuesta(this.url);
                Message msg = new Message();
                msg.arg1 = IMAGEN;
                msg.obj = img;
                this.handler.sendMessage(msg);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<String> parserJson(String s){
        List<String> data = new ArrayList<>();
        try{
            if(!"".equals(s)){
                JSONArray jsonArray = new JSONArray(s);
                //Lo recorrro, extraigo lo que necesite y lo pongo en la lista
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    data.add(jsonObject.getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
