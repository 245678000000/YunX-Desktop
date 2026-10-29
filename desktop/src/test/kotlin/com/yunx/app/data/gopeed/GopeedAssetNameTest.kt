package com.yunx.app.data.gopeed

import org.junit.Assert.assertEquals
import org.junit.Test

class GopeedAssetNameTest {
    @Test
    fun macArm64MatchesGopeedWebRelease() {
        assertEquals(
            "gopeed-web-v1.9.3-macos-arm64.zip",
            GopeedKernelProvisioner.assetName("v1.9.3", "Mac OS X", "aarch64")
        )
    }

    @Test
    fun windowsAmd64StaysOnTheExistingName() {
        assertEquals(
            "gopeed-web-v1.9.3-windows-amd64.zip",
            GopeedKernelProvisioner.assetName("v1.9.3", "Windows 11", "amd64")
        )
    }
}
