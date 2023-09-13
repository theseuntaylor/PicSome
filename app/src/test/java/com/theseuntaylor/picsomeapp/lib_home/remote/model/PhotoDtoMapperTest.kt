package com.theseuntaylor.picsomeapp.lib_home.remote.model

import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo
import org.junit.Assert.assertEquals
import org.junit.Test

class PhotoDtoMapperTest {

    @Test
    fun `when remote Photo Is Mapped to domain, Then It Is Mapped Correctly`() {
        // given
        val remotePhoto = PhotoDto(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240"
        )

        val expectedPhoto = Photo(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240"
        )

        // when
        val photo = remotePhoto.toDomainModel()

        // then
        assertEquals(expectedPhoto, photo)

    }

}