package com.udacity.project4.locationreminders.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    // Data source and initializations
    private lateinit var fakeRepository: FakeDataSource

    // test coroutine
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // initializing data source
    @Before
    fun setup() {
        fakeRepository = FakeDataSource(arrayListOf())
    }

    // save reminder then get the reminder again by ID
    @Test
    fun saveReminder_getsReminderById() = mainCoroutineRule.runBlockingTest {
        // Given - saving reminder by repo
        val task1 = ReminderDTO("First Title" , "First Desc" , "First Location" , 127.3 , 381.1 , "0")
        fakeRepository.saveReminder(task1)

        // When - getting reminder if exist
        val result = fakeRepository.getReminder(task1.id) as Result.Success

        // Then - checking if retreived data matchs what's displayed
        assertThat(result.data.title, IsEqual(task1.title))
        assertThat(result.data.description, IsEqual(task1.description))
        assertThat(result.data.location, IsEqual(task1.location))
        assertThat(result.data.latitude, IsEqual(task1.latitude))
        assertThat(result.data.longitude, IsEqual(task1.longitude))
        assertThat(result.data.id, IsEqual(task1.id))
    }

    //save few reminders then fetch em all
    @Test
    fun saveReminder_getsAllReminders() = mainCoroutineRule.runBlockingTest {
        // Given - saving some reminders to repo
        val task1 = ReminderDTO("First Title" , "First Desc" , "First Location" , 127.3 , 381.1 , "0")
        val task2 = ReminderDTO("Second Title" , "Second Desc" , "Second Location" , 1912.7 , 512.4 , "1")

        fakeRepository.saveReminder(task1)
        fakeRepository.saveReminder(task2)

        // When - fetching all reminders
        val result = fakeRepository.getReminders() as Result.Success

        // Then - checking if the retrieved data matchs what's displayed
        assertThat(result.data[0].title, IsEqual(task1.title))
        assertThat(result.data[0].description, IsEqual(task1.description))
        assertThat(result.data[0].location, IsEqual(task1.location))
        assertThat(result.data[0].latitude, IsEqual(task1.latitude))
        assertThat(result.data[0].longitude, IsEqual(task1.longitude))
        assertThat(result.data[0].id, IsEqual(task1.id))
        assertThat(result.data.size, IsEqual(2))
    }

    //saving reminders then deleting all reminders
    @Test
    fun deleteReminders_deletesAllReminders() = mainCoroutineRule.runBlockingTest {
        // Given - adding 2 reminders to repo
        val task1 = ReminderDTO("First Title" , "First Desc" , "First Location" , 127.3 , 381.1 , "0")
        val task2 = ReminderDTO("Second Title" , "Second Desc" , "Second Location" , 1912.7 , 512.4 , "1")

        fakeRepository.saveReminder(task1)
        fakeRepository.saveReminder(task2)

        // When - deleting all reminders then fetching what exists in repo
        fakeRepository.deleteAllReminders()
        val result = fakeRepository.getReminders() as Result.Success

        //Then - checks if the retrieved data is empty
        assertThat(result.data.size, IsEqual(0))
        assertThat(result.data.isEmpty(), IsEqual(true))
    }

    // testing repo for errors
    @Test
    fun errorReminder_returnsErrorWhenEmpty() = mainCoroutineRule.runBlockingTest {

        // Given - making sure repo is clear of tasks
        val task1 = ReminderDTO("First Title" , "First Desc" , "First Location" , 127.3 , 381.1 , "0")
        fakeRepository.saveReminder(task1)



        // When -- delete all tasks and get task by ID to error
        fakeRepository.deleteAllReminders()
        val result = fakeRepository.getReminder(task1.id) as Result.Error

        // Then - checking id the result is error as supposed to be
        assertThat(result.message, IsEqual("Can't find the Reminder"))
    }


}

