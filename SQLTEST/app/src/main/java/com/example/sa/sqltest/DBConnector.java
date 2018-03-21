package com.example.sa.sqltest;

        import android.util.Log;
        import java.io.BufferedReader;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
public class DBConnector {
    public static String executeQuery(String query_string) {
        String result = "";
        HttpURLConnection urlConnection=null;
        InputStream is =null;
        try {
            URL url=new URL("http://140.130.35.236/skin/www/abc.php");                       //php的位置
            //URL url=new URL("http://140.130.35.236/skin/%E5%90%AB%E6%B0%B4%E9%87%8F.php");
            urlConnection=(HttpURLConnection) url.openConnection();//對資                料庫打開連結
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();//接通資料庫
            is=urlConnection.getInputStream();//從database 開啟 stream

            BufferedReader bufReader = new BufferedReader(new                                    InputStreamReader(is, "utf-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while((line = bufReader.readLine()) != null) {
                builder.append(line + "\n");
            }
            is.close();
            result = builder.toString();
        } catch(Exception e) {
            Log.e("log_tag", e.toString());
        }

        return result;
    }
}