package com.obidan.mixedauthexample.injection

import android.content.Context
import com.obidan.mixedauthexample.MixedAuthExampleApplication
import com.obidan.mixedauthexample.util.SecuredCredentialStorage
import dagger.Module
import dagger.Provides
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
    fun provideSecuredCredentialStorage(context: Context): SecuredCredentialStorage = SecuredCredentialStorage(context)
}