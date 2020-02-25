package com.dmtroncoso.nasaapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Esta clase permite interactuar con el API de la NASA, consultando cuál es la
 * imagen del día (Nasa Picture of the Day).
 *
 * Para poder utilizarla, es necesario obtener una API_KEY. Es tan fácil como
 * acceder a la siguiente URL https://api.nasa.gov/ y registrarse con unos
 * pocos datos personales. La API KEY se recibe en un menasaje de correo electrónico
 *
 *
 */
public class NasaApi {

    private String api_key;
    OkHttpClient client;


    /**
     * Constructor de la clase.
     * @param key API_KEY necesaria para hacer las peticiones
     */
    public NasaApi(String key) {
        this.api_key = key;
        client = new OkHttpClient.Builder()
                .callTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Método para obtener la imagen del día de hoy
     * @return instancia de NasaPicture con la imagen del día de hoy y algo más de información
     *          o nulo si hay una excepción.
     */
    public NasaImage getPicOfToday() {
        return getOnePicture(null);
    }

    /**
     * Método para obtener la imagen del día de una fecha
     * @param date Fecha de la cual queremos obtener la imagen del día. Formato: yyyy-mm-dd
     * @return instancia de NasaPicture con la imagen del día de hoy y algo más de información
     *          o nulo si hay una excepción.
     */
    public NasaImage getPicOfAnyDate(String date) {
        return getOnePicture(date);
    }

    /**
     * Método para obtener las imágenes del día de un rango de fechas
     * @param from Fecha desde. Formato: yyyy-mm-dd
     * @param to Fecha hasta. Formato: yyyy-mm-dd
     * @return Lista de instancias de NasaPicture, o nulo
     */
    public List<NasaImage> getPicOfDateInterval(String from, String to) {

        String url = "https://api.nasa.gov/planetary/apod?api_key=" + api_key + "&start_date=" + from + "&end_date=" + to;

        try {

            Response response = client.newCall(
                    new Request.Builder()
                            .url(url)
                            .build()
            ).execute();


            JSONArray jsonArray = new JSONArray(response.body().string());
            List<NasaImage> result = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                result.add(new NasaImage(jsonObject.getString("url"), jsonObject.getString("title"), jsonObject.getString("explanation"), jsonObject.getString("date")));

            }

            return result;

        } catch (IOException ex) {
            Log.e("NASA", "IOException: " + ex.getMessage());
            return null;
        } catch (JSONException ex) {
            Log.e("NASA", "JSONException: " + ex.getMessage());
            return null;
        }

    }


    private NasaImage getOnePicture(String date) {


        String url = "https://api.nasa.gov/planetary/apod?api_key=" + api_key;
        if (date != null)
            url += "&date=" + date;
        try {

            Response response = client.newCall(
                    new Request.Builder()
                            .url(url)
                            .build()
            ).execute();

            JSONObject jsonObject = new JSONObject(response.body().string());
            return new NasaImage(jsonObject.getString("url"), jsonObject.getString("title"), jsonObject.getString("explanation"), jsonObject.getString("date"));

        } catch (IOException ex) {
            Log.e("NASA", "IOException: " + ex.getMessage());
            return null;
        } catch (JSONException ex) {
            Log.e("NASA", "JSONException: " + ex.getMessage());
            return null;
        }

    }


}
