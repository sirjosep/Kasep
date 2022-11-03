package com.josepvictorr.kasep.util.apihelper;

public class UtilsApi {
    public static final String MASAK_APA_URL_API = "https://masak-apa.tomorisakura.vercel.app/api/";
    public static final String KASEP_URL_API = "http://kasep-api.my.id/api/";
    public static final String YOUTUBE_URL_API = "https://www.googleapis.com/youtube/v3/";

    public static MasakApaApiService getApiService(){
        return RetrofitClient.getClient(MASAK_APA_URL_API).create(MasakApaApiService.class);
    }

    public static KasepApiService getKasepApiService(){
        return RetrofitClient.getClient2(KASEP_URL_API).create(KasepApiService.class);
    }

    public static YoutubeApiService getYoutubeApiService(){
        return RetrofitClient.getClient3(YOUTUBE_URL_API).create(YoutubeApiService.class);
    }
}
