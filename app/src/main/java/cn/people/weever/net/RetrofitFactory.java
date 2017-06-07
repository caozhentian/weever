package cn.people.weever.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.people.weever.BuildConfig;
import cn.people.weever.config.IpConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author ztcao generate Retrofit 
 *
 */
public class RetrofitFactory {

	public static  Retrofit getBaseRetrofit() {

		OkHttpClient.Builder builder = new OkHttpClient().newBuilder() ;
		OkHttpClient client = null ;
		builder.addInterceptor(new TokenInterceptor()) ;
		if(BuildConfig.DEBUG){
			HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
			logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = builder.addInterceptor(logging).build();
			client = builder.build();
		}
		else{
			client =  builder.build();
		}
		Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create();//define datefamat
		
		Retrofit retrofit = new Retrofit.Builder().baseUrl(IpConfig.BASIC_URL)
				.addConverterFactory(GsonConverterFactory.create(gson)).client(client).build();
		return retrofit;

	}

}
