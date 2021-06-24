package com.example.splabov;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;

public class HiloConexion extends Thread {

    Handler handler;
    String url;

    public HiloConexion(Handler handler, String url){
        this.handler = handler;
        this.url = url;
    }

    @Override
    public void run() {
        try{
            ConexionHTTP conexionHTTP = new ConexionHTTP();
            byte[] dataJson = conexionHTTP.obtenerRespuesta(this.url);
            String s = new String(dataJson);
            Message msg = new Message();
            JSONArray jsonArray = new JSONArray(s);
            msg.obj = jsonArray;
            this.handler.sendMessage(msg);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
