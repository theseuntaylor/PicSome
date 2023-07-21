package com.application.storelabs.lib_home.domain.model

import com.application.storelabs.lib_home.local.PhotoEntity
import com.application.storelabs.lib_home.remote.model.PhotoDto
import com.application.storelabs.lib_home.remote.model.toDomainModel
import org.junit.Assert
import org.junit.Test

class PhotoDomainMapperTest {

    @Test
    fun `when domain Photo Is Mapped, Then It Is Mapped Correctly`() {
        // given
        val modelPhoto = Photo(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = false
        )

        val expectedPhoto = PhotoEntity(
            id = "102",
            author = "Ben Moore",
            width = 4320,
            height = 3240,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = false
        )

        // when
        val photo = modelPhoto.asEntity()

        // then
        Assert.assertEquals(expectedPhoto, photo)

    }

}