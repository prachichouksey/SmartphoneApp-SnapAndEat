package com.example.snapandeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUrl {
    public String ReadTheURL(String placeURL) throws IOException
    {
        String Data = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try
        {
            URL url = new URL(placeURL);
//            URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.39956,-121.9198883&radius=3000&type=cafe&keyword=starbucks&sensor=true&key=AIzaSyCD0GTdc64QDR_WojsuRpCYArwae25_f00");

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            String line = "";

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuffer.append(line);
            }

            Data = stringBuffer.toString();
            bufferedReader.close();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(inputStream != null)
                inputStream.close();

            httpURLConnection.disconnect();
        }

        return Data;
    }
}
