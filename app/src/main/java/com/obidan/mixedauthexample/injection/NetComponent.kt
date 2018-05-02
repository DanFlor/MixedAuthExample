package com.obidan.mixedauthexample.injection

import com.obidan.mixedauthexample.MixedAuthExampleActivity
import com.obidan.mixedauthexample.MixedAuthExampleFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetModule::class])
interface NetComponent {
    fun inject(target: MixedAuthExampleActivity)
    fun inject(target: MixedAuthExampleFragment)
}