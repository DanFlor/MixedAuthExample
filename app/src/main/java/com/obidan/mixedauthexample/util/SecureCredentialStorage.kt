package com.obidan.mixedauthexample.util

import com.obidan.mixedauthexample.BuildConfig
import com.obidan.mixedauthexample.MixedAuthExampleApplication
import com.obidan.mixedauthexample.api.MixedAuthAPI
import com.obidan.mixedauthexample.api.MixedAuthCredentialsDelegate
import com.obidan.mixedauthexample.extension.withBearerAuthTokenPrefix
import devliving.online.securedpreferencestore.DefaultRecoveryHandler
import devliving.online.securedpreferencestore.SecuredPreferenceStore
import okhttp3.CookieJar
import okhttp3.Credentials

class SecuredCredentialStorage(
        val app: MixedAuthExampleApplication,
        val cookieJar: CookieJar
): MixedAuthCredentialsDelegate {

    var securedPrefs: SecuredPreferenceStore
    // This is set after the API is injected:
    var api: MixedAuthAPI? = null

    init {
        SecuredPreferenceStore.init(app, DefaultRecoveryHandler())
        securedPrefs = SecuredPreferenceStore.getSharedInstance()
    }

    override fun userBasicAuthCredentials(): String {
        val username = securedPrefs.getString(USERNAME_KEY, "")
        val password = securedPrefs.getString(PASSWORD_KEY, "")
        return Credentials.basic(username, password)
    }

    override fun clientBasicAuthCredentials(): String = Credentials.basic(
            BuildConfig.OAUTH_CLIENT_ID,
            BuildConfig.OAUTH_CLIENT_SECRET
    )

    override fun userOauthBearerToken(): String {
        return bearerToken().withBearerAuthTokenPrefix()
    }

    override fun userOauthRefreshToken(): String {
        return refreshToken()
    }

    override fun userOauthRefreshedBearerToken(): String? {

        // Call the token refresh:
        val response = api?.refreshTokens()?.execute()

        // Check for success:
        if (response?.isSuccessful == true) {
            // refresh succeeded! get the token response:
            response.body()?.let { tokenResponse ->
                // Got it!  Store the tokens:
                setUserTokens(tokenResponse.access_token, tokenResponse.refresh_token)
                // Return the new access token:
                return tokenResponse.access_token.withBearerAuthTokenPrefix()
            }
        }
        return null
    }

    fun setUserTokens(bearerToken: String, refreshToken: String) {
        securedPrefs.Editor()
                .putString(BEARER_TOKEN_KEY, bearerToken)
                .putString(REFRESH_TOKEN_KEY, refreshToken)
                .commit()
    }

    fun setUserBasicCredentials(username: String, password: String) {
        securedPrefs.Editor()
                .putString(USERNAME_KEY, username)
                .putString(PASSWORD_KEY, password)
                .commit()
    }

    override fun userOauthSessionNotRecoverable() {
        app.forcedLogin()
    }

    private fun bearerToken() = securedPrefs.getString(BEARER_TOKEN_KEY, BOGUS_JWT)
    private fun refreshToken() = securedPrefs.getString(REFRESH_TOKEN_KEY, BOGUS_JWT)

    companion object {
        const val USERNAME_KEY = "SecuredUsernameKey"
        const val PASSWORD_KEY = "SecuredPasswordKey"
        const val BEARER_TOKEN_KEY = "SecuredBearerTokenKey"
        const val REFRESH_TOKEN_KEY = "SecuredRefreshTokenKey"

        // This is the sample JWT from jwt.io: we present it as default so that
        // we always have a valid, decryptable JWT to prevent possible 500 errors
        // due to uncaught decryption exceptions on the server side.
        const val BOGUS_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    }

}