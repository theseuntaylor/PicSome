package com.theseuntaylor.picsomeapp.lib_home.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Sized Picsum image URLs. Each test guards one failure mode. */
class PicsumPhotoImageUrlsTest {

    private val urls = PicsumPhotoImageUrls()

    @Test
    fun `a smaller size keeps the photo's aspect ratio and asks for WebP`() {
        assertEquals("https://picsum.photos/id/10/800/600.webp", urls.sized("10", 4000, 3000, 800))
    }

    @Test
    fun `a size larger than the original is capped rather than upscaled`() {
        assertEquals("https://picsum.photos/id/10/4000/3000.webp", urls.sized("10", 4000, 3000, 5000))
    }

    @Test
    fun `a very wide panorama never rounds its height down to zero`() {
        assertEquals("https://picsum.photos/id/10/400/1.webp", urls.sized("10", 10_000, 10, 400))
    }

    @Test
    fun `missing or invalid sizes fall back to the original instead of a broken URL`() {
        assertNull(urls.sized("10", 0, 3000, 800))
        assertNull(urls.sized("10", 4000, 0, 800))
        assertNull(urls.sized("10", -1, 3000, 800))
        assertNull(urls.sized("10", 4000, 3000, 0))
    }

    @Test
    fun `very large photos don't overflow the height calculation`() {
        assertEquals("https://picsum.photos/id/10/49000/49000.webp", urls.sized("10", 50_000, 50_000, 49_000))
    }
}
