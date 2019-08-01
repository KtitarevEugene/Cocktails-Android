package ru.ektitarev.cocktails.di.modules;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.ektitarev.cocktails.api.Api;
import ru.ektitarev.cocktails.common.Constants;

@Module
public class ApiModule {

    @Provides
    ConnectionSpec provideConfiguration() {
        return new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                .build();
    }

    @Provides
    OkHttpClient provideOkHttp(ConnectionSpec config) {
        return new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(config))
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    Api provideApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }
}
