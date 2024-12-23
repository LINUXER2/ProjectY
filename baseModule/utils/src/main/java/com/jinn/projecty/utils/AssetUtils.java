package com.jinn.projecty.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * 将assert下的文件保存在sd卡
     * @param fileName
     */
    public static void copyFileFromAssets(Activity activity, String fileName) {
//        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_AUDIO) !=
//                PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, 1);
//        }
        AssetManager assetManager = activity.getAssets();
        InputStream ins = null;
        FileOutputStream fos = null;
        String path = activity.getCacheDir() + "/" + fileName;
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                LogUtils.d(TAG, "file already exists");
                return;
            }
            fos = new FileOutputStream(path);
            ins = assetManager.open(fileName);
            int len = 0;
            byte[] buffer = new byte[1024 * 512];
            while ((len = ins.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } catch (Exception e) {
            LogUtils.i(TAG, "open file error:" + e+"，path:"+path);
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (Exception e) {
                    LogUtils.i(TAG, "close erroe:" + e.toString());
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    LogUtils.i(TAG, "close error:" + e.toString());
                }
            }
        }

    }


}
