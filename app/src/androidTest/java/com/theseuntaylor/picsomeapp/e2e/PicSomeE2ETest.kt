package com.theseuntaylor.picsomeapp.e2e

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.services.storage.TestStorage
import com.theseuntaylor.picsomeapp.MainActivity
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith

/**
 * End-to-end journeys against the real app and the real Picsum API.
 *
 * Every test starts from cleared app data (Test Orchestrator + clearPackageData) and writes
 * full-screen screenshots to test storage, which AGP pulls into
 * app/build/outputs/connected_android_test_additional_output after the run.
 */
@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class PicSomeE2ETest {

    @get:Rule
    val compose = createEmptyComposeRule()

    @get:Rule
    val testName = TestName()

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context: Context = instrumentation.targetContext
    private var scenario: ActivityScenario<MainActivity>? = null
    private var step = 0

    @After
    fun tearDown() {
        setAirplaneMode(false)
        scenario?.close()
    }

    @Test
    fun favouritesTab_whenNothingIsFavourited_explainsHowToAddOne() {
        launchAndWaitForPhotos()

        compose.onNodeWithText("Favourites").performClick()

        compose.onNodeWithText("No favourites yet", substring = true).assertIsDisplayed()
        compose.onAllNodesWithText("Your Favourite Photos").assertCountEquals(1)
        screenshot("empty_favourites")
    }

    @Test
    fun favouritingAPhoto_showsItInFavourites_andUnfavouritingRemovesIt() {
        launchAndWaitForPhotos()

        compose.onAllNodesWithContentDescription("Add to favourites").onFirst().performClick()
        compose.waitUntilAtLeastOneExists(hasContentDescriptionText("Remove from favourites"))
        screenshot("home_after_favouriting")

        compose.onNodeWithText("Favourites").performClick()
        compose.waitUntil(TIMEOUT) {
            compose.onAllNodesWithContentDescription("Remove from favourites").count() == 1
        }
        screenshot("favourites_with_one_photo")

        compose.onNodeWithContentDescription("Remove from favourites").performClick()
        compose.waitUntilAtLeastOneExists(hasTextContaining("No favourites yet"), TIMEOUT)
        screenshot("favourites_after_unfavouriting")
    }

    @Test
    fun scrollingHomeDown_hidesBottomBar_andScrollingUp_bringsItBack() {
        launchAndWaitForPhotos()
        compose.onNodeWithText("Favourites").assertIsDisplayed()

        compose.onNode(hasScrollAction()).performTouchInput { swipeUp() }
        compose.waitUntilDoesNotExist(hasTextExactly("Favourites"), TIMEOUT)
        screenshot("scrolled_down_bar_hidden")

        compose.onNode(hasScrollAction()).performTouchInput { swipeDown() }
        compose.waitUntilExactlyOneExists(hasTextExactly("Favourites"), TIMEOUT)
        screenshot("scrolled_up_bar_visible")
    }

    @Test
    fun launchingOffline_showsErrorWithRetry_andRetryLoadsPhotosOnceOnline() {
        setAirplaneMode(true)
        scenario = ActivityScenario.launch(MainActivity::class.java)

        compose.waitUntilExactlyOneExists(hasTextExactly("Retry"), TIMEOUT)
        compose.onNodeWithText("no network connection", substring = true).assertIsDisplayed()
        screenshot("offline_error")

        setAirplaneMode(false)
        waitForInternet()
        compose.onNodeWithText("Retry").performClick()

        waitForPhotos()
        screenshot("recovered_after_retry")
    }

    @Test
    fun downloadingAPhoto_savesItToDownloadsNamedAfterThePhoto() {
        launchAndWaitForPhotos()
        val photoId = firstPhotoId()

        compose.onNodeWithContentDescription("Cover image for $photoId").performClick()
        compose.waitUntilExactlyOneExists(hasContentDescriptionText("Download"), TIMEOUT)
        screenshot("photo_details")

        val startedAt = System.currentTimeMillis()
        compose.onNodeWithContentDescription("Download").performClick()
        compose.waitUntilExactlyOneExists(hasTextContaining("Downloading"), TIMEOUT)
        screenshot("download_started")

        val downloadId = waitForCompletedDownload(namePart = "picsome_$photoId", after = startedAt)
        copyDownloadToTestStorage(downloadId, "downloaded_picsome_$photoId.jpg")
    }

    @Test
    fun homeGrid_reservesImageSpaceUpfront_andFirstScreenStaysWithinDataBudget() {
        val uid = context.applicationInfo.uid
        val receivedBefore = TrafficStats.getUidRxBytes(uid)
        launchAndWaitForPhotos()

        // Right after the grid appears, before images arrive, each card should already be
        // sized from the photo's dimensions rather than collapsed around the 24dp placeholder.
        val firstImageHeightDp = compose
            .onAllNodesWithContentDescription("Cover image for", substring = true)
            .onFirst()
            .fetchSemanticsNode()
            .boundsInRoot.height / context.resources.displayMetrics.density
        screenshot("grid_before_images_arrive")

        val bytesUsed = waitForNetworkToSettle(uid) - receivedBefore
        screenshot("grid_settled")
        writeMetrics(
            "first_image_height_dp=$firstImageHeightDp",
            "home_first_screen_bytes=$bytesUsed",
            "home_first_screen_kb=${bytesUsed / 1024}",
        )

        assertTrue("First image slot was ${firstImageHeightDp}dp tall", firstImageHeightDp >= 100f)
        assertTrue("Only ${bytesUsed / 1024} KB downloaded; images never loaded?", bytesUsed > MIN_EXPECTED_BYTES)
        assertTrue(
            "Home's first screen downloaded ${bytesUsed / 1024} KB (budget ${DATA_BUDGET_BYTES / 1024} KB)",
            bytesUsed < DATA_BUDGET_BYTES
        )
    }

    // region helpers

    private fun launchAndWaitForPhotos() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
        waitForPhotos()
    }

    private fun waitForPhotos() {
        compose.waitUntilAtLeastOneExists(hasContentDescriptionText("Add to favourites"), TIMEOUT)
        compose.waitForIdle()
    }

    private fun firstPhotoId(): String =
        compose.onAllNodesWithContentDescription("Cover image for", substring = true)
            .onFirst()
            .fetchSemanticsNode()
            .config[SemanticsProperties.ContentDescription]
            .first()
            .removePrefix("Cover image for ")

    private fun screenshot(label: String) {
        compose.waitForIdle()
        val bitmap = instrumentation.uiAutomation.takeScreenshot() ?: return
        val name = "%s/%02d_%s.png".format(testName.methodName, ++step, label)
        TestStorage().openOutputFile(name).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    /** Waits until the app has received nothing new for [SETTLE_MS], then returns its total received bytes. */
    private fun waitForNetworkToSettle(uid: Int): Long {
        val deadline = System.currentTimeMillis() + NETWORK_TIMEOUT
        var last = TrafficStats.getUidRxBytes(uid)
        var stableSince = System.currentTimeMillis()
        while (System.currentTimeMillis() < deadline) {
            Thread.sleep(250)
            val now = TrafficStats.getUidRxBytes(uid)
            if (now != last) {
                last = now
                stableSince = System.currentTimeMillis()
            } else if (System.currentTimeMillis() - stableSince >= SETTLE_MS) {
                break
            }
        }
        return last
    }

    private fun writeMetrics(vararg lines: String) {
        TestStorage().openOutputFile("${testName.methodName}/metrics.txt").use {
            it.write(lines.joinToString("\n", postfix = "\n").toByteArray())
        }
    }

    private fun setAirplaneMode(enabled: Boolean) {
        val state = if (enabled) "enable" else "disable"
        instrumentation.uiAutomation.executeShellCommand("cmd connectivity airplane-mode $state").close()
    }

    private fun waitForInternet() {
        val connectivity = context.getSystemService(ConnectivityManager::class.java)
        compose.waitUntil(NETWORK_TIMEOUT) {
            connectivity.getNetworkCapabilities(connectivity.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
        }
    }

    private fun waitForCompletedDownload(namePart: String, after: Long): Long {
        val downloads = context.getSystemService(DownloadManager::class.java)
        var downloadId = -1L
        compose.waitUntil(NETWORK_TIMEOUT) {
            downloads.query(DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL))
                .use { cursor ->
                    while (cursor.moveToNext()) {
                        val localUri = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI)) ?: continue
                        val modified = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP))
                        if (namePart in localUri && modified >= after) {
                            downloadId = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID))
                            return@waitUntil true
                        }
                    }
                }
            false
        }
        return downloadId
    }

    private fun copyDownloadToTestStorage(downloadId: Long, name: String) {
        val downloads = context.getSystemService(DownloadManager::class.java)
        downloads.openDownloadedFile(downloadId).use { descriptor ->
            android.os.ParcelFileDescriptor.AutoCloseInputStream(descriptor).use { input ->
                TestStorage().openOutputFile("${testName.methodName}/$name").use { input.copyTo(it) }
            }
        }
    }

    private fun SemanticsNodeInteractionCollection.count() = fetchSemanticsNodes().size

    private fun hasTextExactly(text: String) = androidx.compose.ui.test.hasText(text)
    private fun hasTextContaining(text: String) = androidx.compose.ui.test.hasText(text, substring = true)
    private fun hasContentDescriptionText(text: String) = androidx.compose.ui.test.hasContentDescription(text)

    // endregion

    private companion object {
        const val TIMEOUT = 30_000L
        const val NETWORK_TIMEOUT = 60_000L
        const val SETTLE_MS = 3_000L
        const val MIN_EXPECTED_BYTES = 100L * 1024
        const val DATA_BUDGET_BYTES = 1L * 1024 * 1024
    }
}
