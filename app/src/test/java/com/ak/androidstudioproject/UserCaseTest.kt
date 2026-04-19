package com.ak.androidstudioproject

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
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
import kotlin.collections.emptyList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.never
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.mockito.kotlin.mock
import com.ak.androidstudioproject.AppDetailes.*
import com.ak.androidstudioproject.AppDetailes.Domain.AppDetailsRepository
import com.ak.androidstudioproject.AppDetailes.Domain.GetFullAppInfoUseCase
import com.ak.androidstudioproject.AppDetailes.Domain.ObserveAppDetailsUseCase
import com.ak.androidstudioproject.AppDetailes.Domain.ToggleWishlistUseCase
import com.ak.androidstudioproject.AppList.*
import com.ak.androidstudioproject.AppList.Data.AppsListRepositoryImpl
import com.ak.androidstudioproject.AppList.Data.ListMapper
import com.ak.androidstudioproject.AppList.Data.Local.PreCardDao
import com.ak.androidstudioproject.AppList.Data.Local.PreCardEntity
import com.ak.androidstudioproject.AppList.Data.PreCardMapper
import com.ak.androidstudioproject.AppList.Data.Remote.ListRetrofitApiService
import com.ak.androidstudioproject.AppList.Data.Remote.PreCardRetrofitApiService
import com.ak.androidstudioproject.AppList.Domain.AppList
import com.ak.androidstudioproject.AppList.Domain.AppsListRepository
import com.ak.androidstudioproject.AppList.Domain.GetAppPreCardUseCase
import com.ak.androidstudioproject.AppList.Domain.GetAppUrlsUseCase
import com.ak.androidstudioproject.AppList.Domain.PreCardInfo
import com.ak.androidstudioproject.AppList.Domain.RefreshEventUseCase
import com.ak.androidstudioproject.AppList.Domain.ValidateAppUrlsUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.Assertions.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GetFullAppInfoUseCaseTest {

    private lateinit var useCase: GetFullAppInfoUseCase
    private val repository: AppDetailsRepository = mock()

    @BeforeEach
    fun setUp() {
        useCase = GetFullAppInfoUseCase(repository)
    }

    @Test
    fun `invoke should return FullCardInfo from repository`() = runTest {

        val packageName = "com.example.app"
        val expectedResult = FullCardInfo(
            url = packageName,
            appName = "Test App",
            shortDescription = "Description",
            iconUrl = "https://icon.url",
            categories = listOf("Category1"),
            screenshots = listOf("screenshot1.png"),
            ageRating = "18+",
            developer = "Developer",
            appSize = 1024,
            isInWishlist = false
        )

        whenever(repository.getFullAppInfo(packageName)).thenReturn(expectedResult)


        val result = useCase(packageName)

        assertEquals(expectedResult, result)
        verify(repository).getFullAppInfo(packageName)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {

        val packageName = "com.example.app"
        whenever(repository.getFullAppInfo(packageName)).thenReturn(null)

        val result = useCase(packageName)

        assertNull(result)
        verify(repository).getFullAppInfo(packageName)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveAppDetailsUseCaseTest {

    private lateinit var useCase: ObserveAppDetailsUseCase
    private val repository: AppDetailsRepository = mock()

    @BeforeEach
    fun setUp() {
        useCase = ObserveAppDetailsUseCase(repository)
    }

    @Test
    fun `invoke should return Flow of FullCardInfo from repository`() = runTest {

        val packageName = "com.example.app"
        val expectedDomain = FullCardInfo(
            url = packageName,
            appName = "Test App",
            shortDescription = "Description",
            iconUrl = "https://icon.url",
            categories = listOf("Category1"),
            screenshots = listOf("screenshot1.png"),
            ageRating = "18+",
            developer = "Developer",
            appSize = 1024,
            isInWishlist = false
        )

        whenever(repository.observeAppDetails(packageName)).thenReturn(flowOf(expectedDomain))

        val result = useCase(packageName).first()

        assertEquals(expectedDomain, result)
        verify(repository).observeAppDetails(packageName)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ToggleWishlistUseCaseTest {

    private lateinit var useCase: ToggleWishlistUseCase
    private val repository: AppDetailsRepository = mock()

    @BeforeEach
    fun setUp() {
        useCase = ToggleWishlistUseCase(repository)
    }

    @Test
    fun `invoke should call repository toggleWishlist`() = runTest {

        val packageName = "com.example.app"

        useCase(packageName)

        verify(repository).toggleWishlist(packageName)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class GetAppUrlsUseCaseTest {

    private lateinit var useCase: GetAppUrlsUseCase
    private val repository: AppsListRepository = mock()

    @BeforeEach
    fun setUp() {
        useCase = GetAppUrlsUseCase(repository)
    }

    @Test
    fun `invoke should return AppList from repository`() = runTest {

        val expectedResult = AppList(urls = listOf("com.example.app1", "com.example.app2"))
        whenever(repository.getAppUrls()).thenReturn(expectedResult)

        val result = useCase()

        assertEquals(expectedResult, result)
        verify(repository).getAppUrls()
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {

        whenever(repository.getAppUrls()).thenReturn(null)

        val result = useCase()

        assertNull(result)
        verify(repository).getAppUrls()
    }
}

class ValidateAppUrlsUseCaseTest {

    private lateinit var useCase: ValidateAppUrlsUseCase

    @BeforeEach
    fun setUp() {
        useCase = ValidateAppUrlsUseCase()
    }

    @Test
    fun `invoke should return true when AppList is not null and has urls`() {

        val appList = AppList(urls = listOf("com.example.app1", "com.example.app2"))

        val result = useCase(appList)

        assertTrue(result)
    }

    @Test
    fun `invoke should return false when AppList is null`() {

        val result = useCase(null)

        assertFalse(result)
    }

    @Test
    fun `invoke should return false when AppList has empty urls`() {

        val appList = AppList(urls = emptyList())

        val result = useCase(appList)

        assertFalse(result)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class GetAppPreCardUseCaseTest {

    private lateinit var useCase: GetAppPreCardUseCase
    private val repository: AppsListRepository = mock()

    @BeforeEach
    fun setUp() {
        useCase = GetAppPreCardUseCase(repository)
    }

    @Test
    fun `invoke should return PreCardInfo from repository`() = runTest {

        val packageName = "com.example.app"
        val expectedResult = PreCardInfo(
            url = packageName,
            appName = "Test App",
            shortDescription = "Description",
            iconUrl = "https://icon.url",
            categories = listOf("Category1")
        )

        whenever(repository.getAppPreCard(packageName)).thenReturn(expectedResult)

        val result = useCase(packageName)

        assertEquals(expectedResult, result)
        verify(repository).getAppPreCard(packageName)
    }

    @Test
    fun `invoke should return null when repository returns null`() = runTest {

        val packageName = "com.example.app"
        whenever(repository.getAppPreCard(packageName)).thenReturn(null)

        val result = useCase(packageName)

        assertNull(result)
        verify(repository).getAppPreCard(packageName)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class RefreshEventUseCaseTest {

    private lateinit var useCase: RefreshEventUseCase

    @BeforeEach
    fun setUp() {
        useCase = RefreshEventUseCase()
    }

    @Test
    fun `refreshTrigger should emit Unit when triggerRefresh is called`() = runTest {

        val collectedValues = mutableListOf<Unit>()

        val job = launch {
            useCase.refreshTrigger.collect { value ->
                collectedValues.add(value)
            }
        }

        useCase.triggerRefresh()

        advanceUntilIdle()

        assertEquals(1, collectedValues.size)
        assertEquals(Unit, collectedValues[0])

        job.cancel()
    }

    @Test
    fun `refreshTrigger should be a SharedFlow`() {
        assertNotNull(useCase.refreshTrigger)
    }
}

