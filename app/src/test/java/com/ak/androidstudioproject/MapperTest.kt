package com.ak.androidstudioproject

import com.ak.androidstudioproject.AppDetailes.Data.AppDetailsMapper
import com.ak.androidstudioproject.AppDetailes.Data.Local.FullCardEntity
import com.ak.androidstudioproject.AppDetailes.Data.Remote.AgeRestriction
import com.ak.androidstudioproject.AppDetailes.Data.Remote.AppBody
import com.ak.androidstudioproject.AppDetailes.Data.Remote.FileUrl
import com.ak.androidstudioproject.AppDetailes.Data.Remote.FullCardDTO
import com.ak.androidstudioproject.AppDetailes.Data.Remote.Rating
import com.ak.androidstudioproject.AppList.Data.ListMapper
import com.ak.androidstudioproject.AppList.Data.Local.PreCardEntity
import com.ak.androidstudioproject.AppList.Data.PreCardMapper
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardBody
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardDTO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AppDetailsMapperTest {

    private lateinit var mapper: AppDetailsMapper

    @BeforeEach
    fun setUp() {
        mapper = AppDetailsMapper()
    }

    @Test
    fun `toDomain from DTO should return not null when DTO is valid`() {
        val dto = FullCardDTO(
            code = "200",
            message = "OK",
            body = AppBody(
                appId = 1,
                packageName = "com.example.app",
                appName = "Test App",
                categories = listOf("Category1"),
                companyName = "Test Company",
                shortDescription = "Short desc",
                fullDescription = "Full desc",
                fileSize = 1024L,
                versionName = "1.0",
                iconUrl = "https://icon.url",
                fileUrls = listOf(FileUrl("https://screenshot.url", 0, "image", "portrait")),
                ageRestriction = AgeRestriction("18+", "Adult", "Adult content", "https://age.url"),
                rating = Rating(4.5, 100),
                downloads = 1000,
                price = 499
            ),
            timestamp = "2024-01-01"
        )

        val result = mapper.toDomain(dto)

        assertNotNull(result)
    }

    @Test
    fun `toDomain from DTO should return null when DTO is null`() {
        val result = mapper.toDomain(null as FullCardDTO?)
        assertNull(result)
    }

    @Test
    fun `toDomain from Entity should return not null when Entity is valid`() {
        val entity = FullCardEntity(
            appId = 1,
            packageName = "com.example.app",
            appName = "Test App",
            categories = listOf("Category1"),
            companyName = "Test Company",
            shortDescription = "Short desc",
            fullDescription = "Full desc",
            fileSize = 1024L,
            versionName = "1.0",
            iconUrl = "https://icon.url",
            fileUrls = listOf(FileUrl("https://screenshot.url", 0, "image", "portrait")),
            ageRestriction = AgeRestriction("18+", "Adult", "Adult content", "https://age.url"),
            rating = Rating(4.5, 100),
            downloads = 1000,
            price = 499,
            isInWishlist = false
        )

        val result = mapper.toDomain(entity)

        assertNotNull(result)
    }

    @Test
    fun `toDomain from Entity should return object with default values when Entity is null`() {
        val result = mapper.toDomain(null as FullCardEntity?)

        assertNotNull(result)
        assertEquals("", result.url)
        assertEquals("", result.appName)
        assertTrue(result.categories.isEmpty())
        assertEquals(0L, result.appSize)
    }

    @Test
    fun `toEntity should return not null when DTO is valid`() {
        val dto = FullCardDTO(
            code = "200",
            message = "OK",
            body = AppBody(
                appId = 1,
                packageName = "com.example.app",
                appName = "Test App",
                categories = listOf("Category1"),
                companyName = "Test Company",
                shortDescription = "Short desc",
                fullDescription = "Full desc",
                fileSize = 1024L,
                versionName = "1.0",
                iconUrl = "https://icon.url",
                fileUrls = listOf(FileUrl("https://screenshot.url", 0, "image", "portrait")),
                ageRestriction = AgeRestriction("18+", "Adult", "Adult content", "https://age.url"),
                rating = Rating(4.5, 100),
                downloads = 1000,
                price = 499
            ),
            timestamp = "2024-01-01"
        )

        val result = mapper.toEntity("com.example.app", dto)

        assertNotNull(result)
        assertEquals("com.example.app", result.packageName)
    }
}

class PreCardMapperTest {

    private lateinit var mapper: PreCardMapper

    @BeforeEach
    fun setUp() {
        mapper = PreCardMapper()
    }

    @Test
    fun `toDomain from DTO should return not null when DTO is valid`() {
        val dto = PreCardDTO(
            code = "200",
            message = "OK",
            body = PreCardBody(
                packageName = "com.example.app",
                appName = "Test App",
                categories = listOf("Category1"),
                shortDescription = "Short desc",
                iconUrl = "https://icon.url"
            ),
            timestamp = "2024-01-01"
        )

        val result = mapper.toDomain(dto)

        assertNotNull(result)
    }

    @Test
    fun `toDomain from DTO should return null when DTO is null`() {
        val result = mapper.toDomain(null as PreCardDTO?)
        assertNull(result)
    }

    @Test
    fun `toDomain from Entity should return not null when Entity is valid`() {
        val entity = PreCardEntity(
            packageName = "com.example.app",
            appName = "Test App",
            iconUrl = "https://icon.url",
            shortDescription = "Short desc",
            categories = listOf("Category1")
        )

        val result = mapper.toDomain(entity)

        assertNotNull(result)
        assertEquals("com.example.app", result.url)
        assertEquals("Test App", result.appName)
    }

    @Test
    fun `toEntity should return not null when DTO is valid`() {
        val dto = PreCardDTO(
            code = "200",
            message = "OK",
            body = PreCardBody(
                packageName = "com.example.app",
                appName = "Test App",
                categories = listOf("Category1"),
                shortDescription = "Short desc",
                iconUrl = "https://icon.url"
            ),
            timestamp = "2024-01-01"
        )

        val result = mapper.toEntity("com.example.app", dto)

        assertNotNull(result)
        assertEquals("com.example.app", result.packageName)
    }
}

class ListMapperTest {

    private lateinit var mapper: ListMapper

    @BeforeEach
    fun setUp() {
        mapper = ListMapper()
    }

    @Test
    fun `extractPackageNamesFromHtml should return list when HTML contains package names`() {
        val html = "{\"packageName\":\"com.example.app\"}"

        val result = mapper.extractPackageNamesFromHtml(html)

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `extractPackageNamesFromHtml should return empty list when HTML has no package names`() {
        val html = "<html>No packages</html>"

        val result = mapper.extractPackageNamesFromHtml(html)

        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `toDomain should return AppList when HTML contains package names`() {
        val html = "{\"packageName\":\"com.example.app\"}"

        val result = mapper.toDomain(html)

        assertNotNull(result)
        assertTrue(result?.urls?.isNotEmpty() == true)
    }

    @Test
    fun `toDomain should return null when HTML has no package names`() {
        val html = "<html>No packages</html>"

        val result = mapper.toDomain(html)

        assertNull(result)
    }

    @Test
    fun `toDomain should return null when input is null`() {
        val result = mapper.toDomain(null)

        assertNull(result)
    }

    @Test
    fun `toDomain should return null when input is blank`() {
        val result = mapper.toDomain("")

        assertNull(result)
    }
}