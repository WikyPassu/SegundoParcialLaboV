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
            msg.obj = new JSONArray(s);
            this.handler.sendMessage(msg);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
