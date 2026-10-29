package com.yunx.app.data.download

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Buffer
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

/** 内置 HTTP 下载器把响应体写到调用方指定的目录，不经过 Windows 原生库。 */
class ChunkDownloaderHttpSaveTest {

    private lateinit var server: MockWebServer
    private val downloader = ChunkDownloader { OkHttpClient() }

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun downloadFullWritesResponseBytesIntoChosenDirectory(): Unit = runBlocking {
        val payload = "yunx-macos-http-body".toByteArray(Charsets.UTF_8)
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/octet-stream")
                .setBody(Buffer().write(payload))
        )
        val dir = File("build/http-download-test").apply { mkdirs() }
        val saved = File(dir, "saved.bin")
        if (saved.exists()) saved.delete()

        val ok = downloader.downloadFull(
            taskId = 41L,
            url = server.url("/file").toString(),
            partFile = saved,
            headers = emptyMap(),
            total = payload.size.toLong()
        ) { }

        assertTrue(ok)
        assertTrue(saved.isFile)
        assertArrayEquals(payload, saved.readBytes())
        saved.delete()
    }
}
