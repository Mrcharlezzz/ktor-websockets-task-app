package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }
        val res = client.get("/")
        assertEquals(HttpStatusCode.OK, res.status, "Body: ${res.bodyAsText()}")
        assertEquals("Healthy", res.bodyAsText())
    }
}
