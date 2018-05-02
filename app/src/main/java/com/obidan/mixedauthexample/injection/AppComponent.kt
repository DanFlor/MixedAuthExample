package com.obidan.mixedauthexample.injection

import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun plus(netModule: NetModule): NetComponent
}