package com.application.storelabs.lib_home.local

import com.application.storelabs.lib_home.domain.model.Photo
import org.junit.Assert
import org.junit.Test

class PhotoEntityMapperTest {

    @Test
    fun `when entity Photo Is Mapped to domain, Then It Is Mapped Correctly`() {
        // given
        val entityPhoto = PhotoEntity(
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
        val photo = entityPhoto.toDomainModel()

        // then
        Assert.assertEquals(expectedPhoto, photo)

    }

}