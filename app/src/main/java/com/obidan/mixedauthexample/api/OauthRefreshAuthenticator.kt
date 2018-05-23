package com.obidan.mixedauthexample.api

import com.obidan.mixedauthexample.BuildConfig
import com.obidan.mixedauthexample.Constants
import com.obidan.mixedauthexample.api.delegate.OauthRefreshAuthenticatorDelegate
import com.obidan.mixedauthexample.extension.withBearerAuthTokenPrefix
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber


class OauthRefreshAuthenticator(
        val delegate: OauthRefreshAuthenticatorDelegate
): Authenticator {

    override fun authenticate(route: Route?, response: Response?): Request? {
        Timber.d("Detected authentication error ${response?.code()} on ${response?.request()?.url()}")
        // Did our request have a bearer token in it?
        when (hasBearerAuthorizationToken(response)) {
            false -> {
                // No bearer auth token; nothing to refresh!
                Timber.d("No bearer authentication to refresh.")
                return null
            }
            true -> {
                // It had a bearer auth!
                Timber.d("Bearer authentication present!")
                val previousRetryCount = retryCount(response)
                // Attempt to reauthenticate using the refresh token!
                return reAuthenticateRequestUsingRefreshToken(
                        response?.request(),
                        previousRetryCount + 1
                )
            }
        }
    }

    private fun retryCount(response: Response?): Int {
        return response?.request()?.header(Constants.httpHeaderRetryCount)?.toInt() ?: 0
    }

    private fun hasBearerAuthorizationToken(response: Response?): Boolean {
        response?.let { response ->
            val authorizationHeader = response.request().header(Constants.httpHeaderAuthorization)
            return authorizationHeader.startsWith(Constants.httpHeaderBearerTokenPrefix)
        }
        return false
    }

    @Synchronized
    // We synchronize this request, so that multiple concurrent failures
    // don't all try to use the same refresh token!
    private fun reAuthenticateRequestUsingRefreshToken(staleRequest: Request?, retryCount: Int): Request? {

        // See if we have gone too far:
        if (retryCount > BuildConfig.OAUTH_RE_AUTH_RETRY_LIMIT) {
            // Yup!
            Timber.w("Retry count exceeded! Giving up.")
            // Don't try to re-authenticate any more.
            return null
        }

        // We have some retries left!
        Timber.d("Attempting to fetch a new token...")

        // Try for the new token:
        delegate.userOauthRefreshedBearerToken()?.let { newBearerToken ->
            Timber.d("Retreived new token, re-authenticating...")
            return rewriteRequest(staleRequest, retryCount, newBearerToken)
        }

        // Could not retrieve new token! Unable to re-authenticate!
        Timber.w("Failed to retrieve new token, unable to re-authenticate!")
        return null
    }



    private fun rewriteRequest(
            staleRequest: Request?, retryCount: Int, authToken: String
    ): Request? {
        return staleRequest?.newBuilder()
                ?.header(
                        Constants.httpHeaderAuthorization,
                        authToken.withBearerAuthTokenPrefix()
                )
                ?.header(
                        Constants.httpHeaderRetryCount,
                        "$retryCount"
                )
                ?.build()
    }

}
