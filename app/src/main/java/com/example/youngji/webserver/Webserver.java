package com.example.youngji.webserver;

import android.content.Context;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


/**
 * Created by youngji on 2017. 8. 2..
 */

public class Webserver extends NanoHTTPD{
    Context context;

    public Webserver()
    {
        super(8080);
    }

    public Webserver(Context context){
        super(8080);
        this.context = context;
    }

    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header,
                          Map<String, String> parameters,
                          Map<String, String> files) {
        String answer = "";
        try {
            if(Method.GET.equals(method)){
                answer = "/sdcard/m3u8test/test.m3u8";
            }
            else{
                answer = "Error";
            }
        } catch(Exception e) {
            e.printStackTrace();
        }


        return new NanoHTTPD.Response(answer);
    }

    public void FFmpegDo(){
        FFmpeg ffmpeg = FFmpeg.getInstance(context);

        try{
            ffmpeg.loadBinary(new LoadBinaryResponseHandler(){
                @Override
                public void onStart(){

                }
                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}

            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
