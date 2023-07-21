package com.application.storelabs.lib_home.local

import com.application.storelabs.lib_home.domain.model.Photo
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class PhotoEntityMapperTest {

    fun photoEntityParams() = arrayOf(
        arrayOf(
            PhotoEntity(
                id = "102",
                author = "Ben Moore",
                width = 4320,
                height = 3240,
                url = "https://unsplash.com/photos/pJILiyPdrXI",
                download_url = "https://picsum.photos/id/102/4320/3240",
                isFavourite = true,
            ),

            Photo(
                id = "102",
                author = "Ben Moore",
                width = 4320,
                height = 3240,
                url = "https://unsplash.com/photos/pJILiyPdrXI",
                download_url = "https://picsum.photos/id/102/4320/3240",
                isFavourite = true,
            )
        ),
        arrayOf(
            PhotoEntity(
                id = "102",
                author = "Ben Moore",
                width = 4320,
                height = 3240,
                url = "https://unsplash.com/photos/pJILiyPdrXI",
                download_url = "https://picsum.photos/id/102/4320/3240",
                isFavourite = false,
            ),

            Photo(
                id = "102",
                author = "Ben Moore",
                width = 4320,
                height = 3240,
                url = "https://unsplash.com/photos/pJILiyPdrXI",
                download_url = "https://picsum.photos/id/102/4320/3240",
                isFavourite = false,
            )
        )
    )

    @Test
    @Parameters(method = "photoEntityParams")
    fun `when entity Photo Is Mapped to domain, Then It Is Mapped Correctly`(
        entityPhoto: PhotoEntity,
        expectedPhoto: Photo,
    ) {
        // when
        val photo = entityPhoto.toDomainModel()

        // then
        Assert.assertEquals(expectedPhoto, photo)

    }

}