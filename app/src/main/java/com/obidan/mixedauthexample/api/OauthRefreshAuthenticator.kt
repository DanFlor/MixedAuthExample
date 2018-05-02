package com.obidan.mixedauthexample.api

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route


class OauthRefreshAuthenticator(
        val mixedAuthCredentialsDelegate: MixedAuthCredentialsDelegate
): Authenticator {

    override fun authenticate(route: Route?, response: Response?): Request? {
        return null
    }
}