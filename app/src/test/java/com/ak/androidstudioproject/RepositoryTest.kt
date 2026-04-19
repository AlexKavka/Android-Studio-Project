package com.ak.androidstudioproject

import com.ak.androidstudioproject.AppDetailes.Data.AppDetailsMapper
import com.ak.androidstudioproject.AppDetailes.Data.AppDetailsRepositoryImpl
import com.ak.androidstudioproject.AppDetailes.Data.Local.FullCardDao
import com.ak.androidstudioproject.AppDetailes.Data.Local.FullCardEntity
import com.ak.androidstudioproject.AppDetailes.Data.Remote.AgeRestriction
import com.ak.androidstudioproject.AppDetailes.Data.Remote.AppBody
import com.ak.androidstudioproject.AppDetailes.Data.Remote.FullCardDTO
import com.ak.androidstudioproject.AppDetailes.Data.Remote.RetrofitApiService
import com.ak.androidstudioproject.AppDetailes.Domain.FullCardInfo
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardBody
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardDTO
import org.junit.jupiter.api.BeforeEach
import kotlin.collections.emptyList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.whenever
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.never
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import com.ak.androidstudioproject.AppList.Data.AppsListRepositoryImpl
import com.ak.androidstudioproject.AppList.Data.ListMapper
import com.ak.androidstudioproject.AppList.Data.Local.PreCardDao
import com.ak.androidstudioproject.AppList.Data.Local.PreCardEntity
import com.ak.androidstudioproject.AppList.Data.PreCardMapper
import com.ak.androidstudioproject.AppList.Data.Remote.ListRetrofitApiService
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardRetrofitApiService
import com.ak.androidstudioproject.AppList.Domain.AppList
import com.ak.androidstudioproject.AppList.Domain.PreCardInfo
import org.junit.jupiter.api.Assertions.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AppDetailsRepositoryImplTest {
    private lateinit var repository: AppDetailsRepositoryImpl
    private val apiService: RetrofitApiService = mock()
    private val dao: FullCardDao = mock()
    private val mapper: AppDetailsMapper = mock()

    @BeforeEach
    fun setUp() {
        repository = AppDetailsRepositoryImpl(apiService, dao, mapper)
    }

    @Test
    fun `getFullAppInfo should return cached entity when exists`() = runTest {

        val packageName = "com.example.app"
        val cachedEntity = FullCardEntity(
            appId = 1,
            packageName = packageName,
            appName = "Cached App",
            categories = emptyList(),
            companyName = "Company",
            shortDescription = "Desc",
            fullDescription = "Full",
            fileSize = 100,
            versionName = "1.0",
            iconUrl = "url",
            fileUrls = emptyList(),
            ageRestriction = AgeRestriction("18+", "", "", ""),
            rating = null,
            downloads = null,
            price = null,
            isInWishlist = false
        )
        val expectedDomain = FullCardInfo(
            url = packageName,
            appName = "Cached App",
            shortDescription = "Desc",
            iconUrl = "url",
            categories = emptyList(),
            screenshots = emptyList(),
            ageRating = "18+",
            developer = "Company",
            appSize = 100,
            isInWishlist = false
        )

        whenever(dao.getFullCard(packageName)).thenReturn(cachedEntity)
        whenever(mapper.toDomain(cachedEntity)).thenReturn(expectedDomain)

        val result = repository.getFullAppInfo(packageName)

        assertNotNull(result)
        assertEquals(expectedDomain, result)
        verify(dao).getFullCard(packageName)
        verify(apiService, never()).getAppInfo(any())
    }

    @Test
    fun `getFullAppInfo should fetch from API when cache is empty`() = runTest {

        val packageName = "com.example.app"
        val dto = FullCardDTO(
            code = "200",
            message = "OK",
            body = AppBody(
                appId = 1,
                packageName = packageName,
                appName = "API App",
                categories = emptyList(),
                companyName = "Company",
                shortDescription = "Desc",
                fullDescription = "Full",
                fileSize = 100,
                versionName = "1.0",
                iconUrl = "url",
                fileUrls = emptyList(),
                ageRestriction = AgeRestriction("18+", "", "", ""),
                rating = null,
                downloads = null,
                price = null
            ),
            timestamp = "2024-01-01"
        )
        val expectedDomain = FullCardInfo(
            url = packageName,
            appName = "API App",
            shortDescription = "Desc",
            iconUrl = "url",
            categories = emptyList(),
            screenshots = emptyList(),
            ageRating = "18+",
            developer = "Company",
            appSize = 100,
            isInWishlist = false
        )
        val expectedEntity = FullCardEntity(
            appId = 1,
            packageName = packageName,
            appName = "API App",
            categories = emptyList(),
            companyName = "Company",
            shortDescription = "Desc",
            fullDescription = "Full",
            fileSize = 100,
            versionName = "1.0",
            iconUrl = "url",
            fileUrls = emptyList(),
            ageRestriction = AgeRestriction("18+", "", "", ""),
            rating = null,
            downloads = null,
            price = null,
            isInWishlist = false
        )

        whenever(dao.getFullCard(packageName)).thenReturn(null)
        whenever(apiService.getAppInfo(packageName)).thenReturn(dto)
        whenever(mapper.toDomain(dto)).thenReturn(expectedDomain)
        whenever(mapper.toEntity(packageName, dto)).thenReturn(expectedEntity)

        val result = repository.getFullAppInfo(packageName)

        assertNotNull(result)
        assertEquals(expectedDomain, result)
        verify(apiService).getAppInfo(packageName)
        verify(dao).insertFullCard(expectedEntity)
    }

    @Test
    fun `getFullAppInfo should return null when API throws exception and no cache`() = runTest {

        val packageName = "com.example.app"

        whenever(dao.getFullCard(packageName)).thenReturn(null)
        whenever(apiService.getAppInfo(packageName)).thenThrow(RuntimeException("Network error"))

        val result = repository.getFullAppInfo(packageName)

        assertNull(result)
    }

    @Test
    fun `toggleWishlist should update wishlist status in DAO`() = runTest {

        val packageName = "com.example.app"
        whenever(dao.getWishlistStatus(packageName)).thenReturn(true)

        repository.toggleWishlist(packageName)

        verify(dao).updateWishlistStatus(packageName)
        verify(dao).getWishlistStatus(packageName)
    }

    @Test
    fun `observeAppDetails should return flow of FullCardInfo`() = runTest {

        val packageName = "com.example.app"
        val entity = FullCardEntity(
            appId = 1,
            packageName = packageName,
            appName = "Test App",
            categories = emptyList(),
            companyName = "Company",
            shortDescription = "Desc",
            fullDescription = "Full",
            fileSize = 100,
            versionName = "1.0",
            iconUrl = "url",
            fileUrls = emptyList(),
            ageRestriction = AgeRestriction("18+", "", "", ""),
            rating = null,
            downloads = null,
            price = null,
            isInWishlist = true
        )
        val expectedDomain = FullCardInfo(
            url = packageName,
            appName = "Test App",
            shortDescription = "Desc",
            iconUrl = "url",
            categories = emptyList(),
            screenshots = emptyList(),
            ageRating = "18+",
            developer = "Company",
            appSize = 100,
            isInWishlist = true
        )

        whenever(dao.getFullCardFlow(packageName)).thenReturn(flowOf(entity))
        whenever(mapper.toDomain(entity)).thenReturn(expectedDomain)

        val result = repository.observeAppDetails(packageName).first()

        assertEquals(expectedDomain, result)
        verify(dao).getFullCardFlow(packageName)
        verify(mapper).toDomain(entity)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class AppsListRepositoryImplTest {

    private lateinit var repository: AppsListRepositoryImpl
    private val preCardApiService: PreCardRetrofitApiService = mock()
    private val listApiService: ListRetrofitApiService = mock()
    private val listMapper: ListMapper = mock()
    private val preCardMapper: PreCardMapper = mock()
    private val preCardDao: PreCardDao = mock()

    @BeforeEach
    fun setUp() {
        repository = AppsListRepositoryImpl(
            preCardApiService,
            listApiService,
            listMapper,
            preCardMapper,
            preCardDao
        )
    }

    @Test
    fun `getAppPreCard should return cached entity when exists`() = runTest {

        val packageName = "com.example.app"
        val cachedEntity = PreCardEntity(
            packageName = packageName,
            appName = "Cached App",
            iconUrl = "https://icon.url",
            shortDescription = "Cached description",
            categories = listOf("Category1")
        )
        val expectedDomain = PreCardInfo(
            url = packageName,
            appName = "Cached App",
            shortDescription = "Cached description",
            iconUrl = "https://icon.url",
            categories = listOf("Category1")
        )

        whenever(preCardDao.getPreCard(packageName)).thenReturn(cachedEntity)
        whenever(preCardMapper.toDomain(cachedEntity)).thenReturn(expectedDomain)

        val result = repository.getAppPreCard(packageName)

        assertNotNull(result)
        assertEquals(expectedDomain, result)
        verify(preCardDao).getPreCard(packageName)
        verify(preCardApiService, never()).getAppInfo(any())
    }

    @Test
    fun `getAppPreCard should fetch from API when cache is empty`() = runTest {

        val packageName = "com.example.app"
        val dto = PreCardDTO(
            code = "200",
            message = "OK",
            body = PreCardBody(
                packageName = packageName,
                appName = "API App",
                categories = listOf("Category1"),
                shortDescription = "API description",
                iconUrl = "https://api.icon.url"
            ),
            timestamp = "2024-01-01"
        )
        val expectedDomain = PreCardInfo(
            url = packageName,
            appName = "API App",
            shortDescription = "API description",
            iconUrl = "https://api.icon.url",
            categories = listOf("Category1")
        )
        val expectedEntity = PreCardEntity(
            packageName = packageName,
            appName = "API App",
            iconUrl = "https://api.icon.url",
            shortDescription = "API description",
            categories = listOf("Category1")
        )

        whenever(preCardDao.getPreCard(packageName)).thenReturn(null)
        whenever(preCardApiService.getAppInfo(packageName)).thenReturn(dto)
        whenever(preCardMapper.toDomain(dto)).thenReturn(expectedDomain)
        whenever(preCardMapper.toEntity(packageName, dto)).thenReturn(expectedEntity)

        val result = repository.getAppPreCard(packageName)

        assertNotNull(result)
        assertEquals(expectedDomain, result)
        verify(preCardApiService).getAppInfo(packageName)
        verify(preCardDao).insertPreCard(expectedEntity)
    }

    @Test
    fun `getAppPreCard should return cached entity when API throws exception`() = runTest {

        val packageName = "com.example.app"
        val cachedEntity = PreCardEntity(
            packageName = packageName,
            appName = "Cached App",
            iconUrl = "https://icon.url",
            shortDescription = "Cached description",
            categories = listOf("Category1")
        )
        val expectedDomain = PreCardInfo(
            url = packageName,
            appName = "Cached App",
            shortDescription = "Cached description",
            iconUrl = "https://icon.url",
            categories = listOf("Category1")
        )

        whenever(preCardDao.getPreCard(packageName)).thenReturn(cachedEntity)
        whenever(preCardApiService.getAppInfo(packageName)).thenThrow(RuntimeException("Network error"))
        whenever(preCardMapper.toDomain(cachedEntity)).thenReturn(expectedDomain)

        val result = repository.getAppPreCard(packageName)

        assertNotNull(result)
        assertEquals(expectedDomain, result)
    }

    @Test
    fun `getAppPreCard should return null when cache empty and API throws exception`() = runTest {

        val packageName = "com.example.app"

        whenever(preCardDao.getPreCard(packageName)).thenReturn(null)
        whenever(preCardApiService.getAppInfo(packageName)).thenThrow(RuntimeException("Network error"))

        val result = repository.getAppPreCard(packageName)

        assertNull(result)
    }


    @Test
    fun `getAppUrls should merge cache and API results`() = runTest {

        val cachedPackageNames = listOf("com.example.cached1", "com.example.cached2")
        val apiResponse =
            "{\"packageName\":\"com.example.api1\",\"packageName\":\"com.example.api2\"}"
        val apiUrls = listOf("com.example.api1", "com.example.api2")
        val expectedMerged = listOf(
            "com.example.api1",
            "com.example.api2",
            "com.example.cached1",
            "com.example.cached2"
        )

        whenever(preCardDao.getAllPackageNames()).thenReturn(cachedPackageNames)
        whenever(listApiService.getAppList(query = "a")).thenReturn(apiResponse)
        whenever(listMapper.toDomain(apiResponse)).thenReturn(AppList(urls = apiUrls))

        val result = repository.getAppUrls()

        assertNotNull(result)
        assertEquals(expectedMerged, result?.urls)
    }

    @Test
    fun `getAppUrls should return only cached when API fails`() = runTest {

        val cachedPackageNames = listOf("com.example.cached1", "com.example.cached2")

        whenever(preCardDao.getAllPackageNames()).thenReturn(cachedPackageNames)
        whenever(listApiService.getAppList(query = "a")).thenThrow(RuntimeException("Network error"))

        val result = repository.getAppUrls()

        assertNotNull(result)
        assertEquals(cachedPackageNames, result?.urls)
    }

    @Test
    fun `getAppUrls should return only cached when API returns empty list`() = runTest {

        val cachedPackageNames = listOf("com.example.cached1", "com.example.cached2")

        whenever(preCardDao.getAllPackageNames()).thenReturn(cachedPackageNames)
        whenever(listApiService.getAppList(query = "a")).thenReturn("{}")
        whenever(listMapper.toDomain(any())).thenReturn(AppList(urls = emptyList()))

        val result = repository.getAppUrls()

        assertNotNull(result)
        assertEquals(cachedPackageNames, result?.urls)
    }

    @Test
    fun `getAppUrls should return only API when cache is empty`() = runTest {

        val cachedPackageNames = emptyList<String>()
        val apiResponse = "{\"packageName\":\"com.example.api1\"}"
        val apiUrls = listOf("com.example.api1")

        whenever(preCardDao.getAllPackageNames()).thenReturn(cachedPackageNames)
        whenever(listApiService.getAppList(query = "a")).thenReturn(apiResponse)
        whenever(listMapper.toDomain(apiResponse)).thenReturn(AppList(urls = apiUrls))

        val result = repository.getAppUrls()

        assertNotNull(result)
        assertEquals(apiUrls, result?.urls)
    }

    @Test
    fun `getAppUrls should return empty list when both cache and API are empty`() = runTest {

        val cachedPackageNames = emptyList<String>()

        whenever(preCardDao.getAllPackageNames()).thenReturn(cachedPackageNames)
        whenever(listApiService.getAppList(query = "a")).thenReturn("{}")
        whenever(listMapper.toDomain(any())).thenReturn(AppList(urls = emptyList()))

        val result = repository.getAppUrls()

        assertNotNull(result)
        assertTrue(result?.urls?.isEmpty() == true)
    }

    @Test
    fun `getAppUrls should remove duplicates from cache when already in API`() = runTest {

        val cachedPackageNames = listOf("com.example.api1", "com.example.cached2")
        val apiResponse = "{\"packageName\":\"com.example.api1\"}"
        val apiUrls = listOf("com.example.api1")
        val expectedMerged = listOf("com.example.api1", "com.example.cached2")

        whenever(preCardDao.getAllPackageNames()).thenReturn(cachedPackageNames)
        whenever(listApiService.getAppList(query = "a")).thenReturn(apiResponse)
        whenever(listMapper.toDomain(apiResponse)).thenReturn(AppList(urls = apiUrls))

        val result = repository.getAppUrls()

        assertNotNull(result)
        assertEquals(expectedMerged, result?.urls)
        assertEquals(2, result?.urls?.size)
    }

    @Test
    fun `clearCache should call DAO clearAll`() = runTest {

        repository.clearCache()
        verify(preCardDao).clearAll()
    }
}