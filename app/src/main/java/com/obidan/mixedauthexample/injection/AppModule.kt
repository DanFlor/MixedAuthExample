package com.obidan.mixedauthexample.injection

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.obidan.mixedauthexample.MixedAuthExampleApplication
import com.obidan.mixedauthexample.util.SecuredCredentialStorage
import dagger.Module
import dagger.Provides
import okhttp3.CookieJar
import javax.inject.Singleton

@Module
class AppModule(
        val app: MixedAuthExampleApplication
) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideMixedAuthExampleApp(): MixedAuthExampleApplication = app

    @Provides
    @Singleton
    fun providePersistentCookieJar(
            context: Context
    ): PersistentCookieJar {
        return PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(context)
        );
    }

    @Provides
    @Singleton
    fun provideCookieJar(
            cookieJar: PersistentCookieJar
    ): CookieJar = cookieJar

    @Provides
    @Singleton
    fun provideSecuredCredentialStorage(
            app: MixedAuthExampleApplication,
            cookieJar: PersistentCookieJar
    ): SecuredCredentialStorage {
        return SecuredCredentialStorage(app, cookieJar)
    }
}