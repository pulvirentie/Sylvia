package serialization

import com.yoox.net.KtorRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpRequest
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.junit.Assert
import org.junit.Test

class CustomHeaderTest {
    @Serializable
    data class Foo(val bar: String)

    @Test
    fun ensureCustomHttpHeader() {
        var issuedRequest: MockHttpRequest? = null
        val engine = MockEngine {
            issuedRequest = this
            MockHttpResponse(
                call,
                HttpStatusCode.OK,
                ByteReadChannel("{\"bar\": \"\"}")
            )
        }
        val req = KtorRequest.Get(HttpClient(engine),
            URLBuilder("http://exampl.ecom"),
            Foo.serializer())
        runBlocking {
            req.execute()
            Assert.assertEquals("hack2019", issuedRequest?.headers?.get("YOOX-Client-Id"))
        }
    }
}
