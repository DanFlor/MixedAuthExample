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
    fun provideCookieJar(context: Context): CookieJar {
        return PersistentCookieJar(
                SetCookieCache(),
                SharedPrefsCookiePersistor(context)
        );
    }

    @Provides
    @Singleton
    fun provideSecuredCredentialStorage(app: MixedAuthExampleApplication, cookieJar: CookieJar): SecuredCredentialStorage {
        return SecuredCredentialStorage(app, cookieJar)
    }
}