package com.jinn.projecty.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AssetUtils {
    private static final String TAG = "AssetUtils";

    public static String getFromAsset(Context context,String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        BufferedReader  bufferedReader=null;
        try{
            inputStreamReader =new InputStreamReader(context.getResources().getAssets().open(fileName));
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        }
        catch (IOException e){
            LogUtils.e(TAG,"get from asset error:"+e.toString());
        }
        finally {
             try {
                 if(inputStreamReader!=null){
                     inputStreamReader.close();
                 }
                 if(bufferedReader!=null){
                     bufferedReader.close();
                 }
             }catch (IOException e){
                 LogUtils.e(TAG,"close error:"+e.toString());
             }
        }
        return stringBuilder.toString();
    }


}
