package by.dimanolog.testappbalinasoft;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import by.dimanolog.testappbalinasoft.api.FarforApi;
import by.dimanolog.testappbalinasoft.database.DatabaseHelper;
import by.dimanolog.testappbalinasoft.services.DownloadImageService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Dimanolog on 13.01.2017.
 */

public class App extends Application {

    private static final int MAP_INITIAL_CAPACITY = 12;
    private static final int CONNECT_TIMEOUT = 1;
    private static final int READ_TIMEOUT = 1;
    private static final String BASE_URL = "http://ufa.farfor.ru/";
    private static final String KEY = "ukAXxeJYZN";

    private static FarforApi sFarforApi;
    private static DatabaseHelper sDatabaseHelper;
    private static Map<String, String> sCategoryToImageNameMap;
    private static DownloadImageService sDownloadImageService;

    public static final int PERMISSION_ACCESS_FINE_LOCATION=0;
    public static final int PERMISSION_ACCESS_COARSE_LOCATION =1;



    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;

    public static String getKey(){
        return KEY;
    }

    public static FarforApi getFarforApi() {
        return sFarforApi;
    }
    public static DatabaseHelper getDatabaseHelper(){
        return sDatabaseHelper;
    }

    public static Map<String,String> getCategoryToImageNameMap()
    {
        return sCategoryToImageNameMap;
    }

    public static DownloadImageService getDownloadImageService(){
        return sDownloadImageService;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        httpClientInit();
        UfaFarforApiInit();
        DownloadImageServiceInit();
        categoryToImageInit();
        sDatabaseHelper = OpenHelperManager.getHelper(getApplicationContext(), DatabaseHelper.class);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sDatabaseHelper.close();
    }

    private void DownloadImageServiceInit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .build();

         sDownloadImageService = retrofit.create(DownloadImageService.class);
    }

    private void UfaFarforApiInit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(mOkHttpClient)
                .build();
        sFarforApi = mRetrofit.create(FarforApi.class);
    }


    private void categoryToImageInit() {
        String[] categoryArray=getResources().getStringArray(R.array.categories);
        String[] imageNameArray=getResources().getStringArray(R.array.categories_image);
        sCategoryToImageNameMap =new HashMap<>(MAP_INITIAL_CAPACITY);
        for(int i=0; i<categoryArray.length&&i<imageNameArray.length;i++){
                sCategoryToImageNameMap.put(categoryArray[i],imageNameArray[i]);
        }
        
    }

    private void httpClientInit() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
                .build();

    }

}
