package com.stx.xhb.DCAPlatform.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpAssist {

    public static String uploadFile(final File file,String name) {
        //服务器的访问路径 按照自己的情况配置
        String uploadUrl = API.Uploadfile_URL;
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection",
                    "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(
                    httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + name+file.getPath().substring(file.getPath()
                    .lastIndexOf(".") ) + "\"" + end);
            dos.writeBytes(end);
            FileInputStream fis = new FileInputStream(file.getPath());
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);

            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            dos.close();
            is.close();
            if (result != null && result.equals("Y")) {
                return "1";
            } else {

                return "0";
            }


        } catch (Exception e) {
            e.printStackTrace();

            return "0";
        }
    }
}
