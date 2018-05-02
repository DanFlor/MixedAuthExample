package com.obidan.mixedauthexample.injection

import com.obidan.mixedauthexample.MixedAuthExampleActivity
import com.obidan.mixedauthexample.MixedAuthExampleFragment
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = arrayOf(NetModule::class))
interface NetComponent {
    fun inject(target: MixedAuthExampleActivity)
    fun inject(target: MixedAuthExampleFragment)
}