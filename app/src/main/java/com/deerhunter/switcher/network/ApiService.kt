package com.deerhunter.switcher.network

import com.deerhunter.switcher.preferences.Preferences
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.providers.DigestAuthCredentials
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Singleton
class ApiService @Inject constructor(
    private val httpClientProvider: HttpClientProvider,
    private val preferences: Preferences,
) {

    suspend fun getSwitchState(port: Int): SwitchStatusResponse {
        return httpClientProvider.getHttpClient(getCurrentCredentials()).use { client ->
            client.post(getBaseUrl(port)) {
                contentType(ContentType.Application.Json)
                setBody(
                    SwitchStatusRequest(
                        target = "relay",
                        action = "status"
                    )
                )
            }
        }.body()
    }

    suspend fun changeSwitchState(open: Boolean, port: Int): SwitchChangeResponse {
        return httpClientProvider.getHttpClient(getCurrentCredentials()).use { client ->
            client.post(getBaseUrl(port)) {
                contentType(ContentType.Application.Json)
                setBody(
                    SwitchChangeRequest(
                        target = "relay",
                        action = "trig",
                        data = SwitchChangeData(
                            mode = 1,
                            num = 1,
                            level = if (open) 1 else 0,
                            delay = 5
                        )
                    )
                )
            }.body()
        }
    }

    private suspend fun getCurrentCredentials(): DigestAuthCredentials = DigestAuthCredentials(
        preferences.getString("username"),
        preferences.getString("password")
    )

    private suspend fun getBaseUrl(port: Int): String {
        val ipAddress = preferences.getString("ipAddress", "")
        return "http://$ipAddress:$port/api"
    }
}

@Serializable
data class SwitchStatusRequest(
    @SerialName("target")
    val target: String,
    @SerialName("action")
    val action: String
)

@Serializable
data class SwitchStatusResponse(
    @SerialName("retcode")
    val retcode: Int,
    @SerialName("action")
    val action: String,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: SwitchData
)

@Serializable
data class SwitchData(
    @SerialName("RelayA")
    val relayA: Int
)

@Serializable
data class SwitchChangeRequest(
    @SerialName("target")
    val target: String,
    @SerialName("action")
    val action: String,
    @SerialName("data")
    val data: SwitchChangeData
)

@Serializable
data class SwitchChangeData(
    @SerialName("mode")
    val mode: Int,
    @SerialName("num")
    val num: Int,
    @SerialName("level")
    val level: Int,
    @SerialName("delay")
    val delay: Int
)

@Serializable
data class SwitchChangeResponse(
    @SerialName("retcode")
    val retcode: Int,
    @SerialName("action")
    val action: String,
    @SerialName("message")
    val message: String
)
