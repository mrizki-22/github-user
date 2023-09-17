package com.example.githubuser.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.data.local.entity.FavoriteUser
import com.example.githubuser.data.local.room.FavoriteUserDao
import com.example.githubuser.data.local.room.FavoriteUserDatabase
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class UserRepositoryTest {

    @Mock
    private lateinit var favoriteUserDao: FavoriteUserDao

    @Mock
    private lateinit var mockApplication: Application

    @Mock
    private lateinit var mockDatabase: FavoriteUserDatabase

    private lateinit var userRepository: UserRepository


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(FavoriteUserDatabase.getDatabase(mockApplication)).thenReturn(mockDatabase)
        `when`(mockDatabase.favoriteUserDao()).thenReturn(favoriteUserDao)
        userRepository = UserRepository(mockApplication)
    }
    @Test
    fun `test getAllFavoriteUser`() {
        val mockUsersList = listOf(FavoriteUser("mrizki-22", "avatar_url"))
        val liveDataMock = MutableLiveData<List<FavoriteUser>>()
        liveDataMock.value = mockUsersList

        `when`(favoriteUserDao.getAll()).thenReturn(liveDataMock)
        val result = userRepository.getAllFavoriteUser().value

        assertEquals(mockUsersList, result)
    }
//
//    @Test
//    fun insertFavoriteUser() {
//    }
//
//    @Test
//    fun deleteFavoriteUser() {
//    }
//
//    @Test
//    fun isFavorite() {
//    }
}