package com.obidan.mixedauthexample.injection

import com.obidan.mixedauthexample.MixedAuthExampleApplication

class Injector private constructor() {
    companion object {
        fun getNetComponent(): NetComponent = MixedAuthExampleApplication.INSTANCE.netComponent
    }
}