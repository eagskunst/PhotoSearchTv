package com.eagskunst.photosearch.commons

import android.content.Context
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyQueryInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val appInfo = context.packageManager
            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
        val apiKey = appInfo.metaData["flickrApiKey"].toString()

        val request = chain.request()
        val url = request.url
            .newBuilder()
            .addQueryParameter("api_key", apiKey)
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "true")
            .addQueryParameter("es-us", "true")
            .build()

        return chain.proceed(
            request.newBuilder()
                .url(url)
                .build()
        )
    }
}