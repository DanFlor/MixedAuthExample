package com.obidan.mixedauthexample.api.delegate

interface OauthRefreshAuthenticatorDelegate {

    // Used to retrieve the oauth client's basic client id/secret auth header
    fun clientBasicAuthCredentials(): String

    // Used to retrieve the user's oauth bearer token:
    fun userOauthBearerToken(): String

    // Used to retrieve the user's oauth refresh token:
    fun userOauthRefreshToken(): String

    // Used to request a user's refreshed oauth bearer token:
    fun userOauthRefreshedBearerToken(): String?

    // Used to signal that the oauth session is not recoverable
    fun userOauthSessionNotRecoverable()
}