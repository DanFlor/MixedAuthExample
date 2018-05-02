package com.obidan.mixedauthexample.api.delegate

interface MixedAuthCredentialsDelegate: OauthRefreshAuthenticatorDelegate {
    // Used to retrieve the user's basic user/pass credentials auth header
    fun userBasicAuthCredentials(): String
}