package com.obidan.mixedauthexample.injection

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.obidan.mixedauthexample.api.MixedAuthAPI
import com.obidan.mixedauthexample.api.OauthRefreshAuthenticator
import com.obidan.mixedauthexample.util.SecuredCredentialStorage
import dagger.Module
import dagger.Provides
import okhttp3.CookieJar
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setLenient()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        return gsonBuilder.create()
    }

    @Provides
    fun provideGsonConvertorFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideCookieJar(context: Context): CookieJar {
        return PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(context)
        );
    }

    @Provides
    @Singleton
    fun provideOauthRefreshAuthenticator(
            securedCredentialStorage: SecuredCredentialStorage
    ): OauthRefreshAuthenticator {
        return OauthRefreshAuthenticator(
            securedCredentialStorage
        )
    }

    @Provides
    @Singleton
    fun provideMixedAuthAPI(
            securedCredentialStorage: SecuredCredentialStorage,
            gsonConverterFactory: GsonConverterFactory,
            httpLoggingInterceptor: HttpLoggingInterceptor,
            oauthRefreshAuthenticator: OauthRefreshAuthenticator,
            cookieJar: CookieJar
    ): MixedAuthAPI {

        // Instantiate the concrete API:
        val api = MixedAuthAPI.create(
                securedCredentialStorage,
                gsonConverterFactory,
                httpLoggingInterceptor,
                oauthRefreshAuthenticator,
                cookieJar
        )

        // Hand the API back to the storage, so it can refresh!
        securedCredentialStorage.api = api

        //Done!
        return api
    }

}