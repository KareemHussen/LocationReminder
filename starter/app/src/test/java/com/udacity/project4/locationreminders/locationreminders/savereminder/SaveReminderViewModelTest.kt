package com.udacity.project4.locationreminders.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var viewModel: SaveReminderViewModel

    // test main coroutine
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // context and same thread
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    // initializations
    @Before
    fun setUpViewModel(){
        stopKoin()
        fakeDataSource = FakeDataSource()
        viewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }

    // clearing fake data source
    @After
    fun clearDataSource() = runBlockingTest{
        fakeDataSource.deleteAllReminders()
    }

    // saving reminder then clearing all reminders and check all is deleted
    @Test
    fun saveReminderTest_SavingDataThenClearing () = mainCoroutineRule.runBlockingTest{

        // Given - add reminder data to View Model
        val task1 = ReminderDTO("First Title" , "First Desc" , "First Location" , 127.3 , 381.1 , "0")

        viewModel.reminderTitle.value = task1.title
        viewModel.reminderDescription.value = task1.description
        viewModel.reminderSelectedLocationStr.value = task1.location
        viewModel.latitude.value = task1.latitude
        viewModel.longitude.value = task1.longitude

        // When - clear all data from View Model
        viewModel.onClear()

        // Then - check LiveData is empty

        MatcherAssert.assertThat(
            viewModel.reminderTitle.getOrAwaitValue(),
            Is.`is`(CoreMatchers.nullValue())
        )
        MatcherAssert.assertThat(
            viewModel.latitude.getOrAwaitValue(),
            Is.`is`(CoreMatchers.nullValue())
        )
        MatcherAssert.assertThat(
            viewModel.reminderSelectedLocationStr.getOrAwaitValue(),
            Is.`is`(CoreMatchers.nullValue())
        )
        MatcherAssert.assertThat(
            viewModel.longitude.getOrAwaitValue(),
            Is.`is`(CoreMatchers.nullValue())
        )
        MatcherAssert.assertThat(
            viewModel.reminderDescription.getOrAwaitValue(),
            Is.`is`(CoreMatchers.nullValue())
        )
    }




}