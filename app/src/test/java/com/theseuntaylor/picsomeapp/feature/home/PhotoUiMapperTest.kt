package com.theseuntaylor.picsomeapp.feature.home

import com.theseuntaylor.picsomeapp.feature.home.model.PhotoUi
import com.theseuntaylor.picsomeapp.feature.home.model.asUiModel
import com.theseuntaylor.picsomeapp.lib_home.domain.model.Photo
import org.junit.Assert
import org.junit.Test

class PhotoUiMapperTest {

    @Test
    fun `when Domain Photo Is Mapped to PhotoUi, Then It Is Mapped Correctly`() {
        // given
        val domainPhoto = Photo(
            id = "102",
            author = "Ben Moore",
            download_url = "https://picsum.photos/id/102/4320/3240",
            height = 3000,
            width = 2000,
            url = "https://unsplash.com/photos/pJILiyPdrXI",
            isFavourite = true
        )

        val expectedPhoto = PhotoUi(
            id = "102",
            author = "Ben Moore",
            download_url = "https://picsum.photos/id/102/4320/3240",
            isFavourite = true
        )

        // when
        val photo = domainPhoto.asUiModel()

        // then
        Assert.assertEquals(expectedPhoto, photo)

    }

}