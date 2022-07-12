package com.josepvictorr.kasep.util.apihelper;

public class UtilsApi {
    public static final String MASAK_APA_URL_API = "https://masak-apa.tomorisakura.vercel.app/api/";
    public static BaseApiService getApiService(){
        return RetrofitClient.getClient(MASAK_APA_URL_API).create(BaseApiService.class);
    }

}
