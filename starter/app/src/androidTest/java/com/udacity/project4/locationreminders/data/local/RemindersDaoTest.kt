package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Database
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var db: RemindersDatabase

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java)
            .build()
    }


    // insert task and get it by id
    @Test
     fun insertTaskThenReturnWithId() = runBlockingTest {
        // GIVEN - Task Insert it to DB.
        val task1 = ReminderDTO("First Title" , "First Desc" , "First Location" , 127.3 , 381.1 , "0")

        db.reminderDao().saveReminder(task1)

        // WHEN - Get the task by id from the database.
        val loaded = db.reminderDao().getReminderById(task1.id)

        // THEN - The loaded data contains the expected values.
        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(task1.id))
        assertThat(loaded.title, `is`(task1.title))
        assertThat(loaded.description, `is`(task1.description))
    }

    // insert tasks and check that all Added
    @Test
     fun insertTasksThenGetAll() = runBlockingTest {
        // GIVEN - Tasks Insert it to DB.
        val task1 = ReminderDTO("First Title" , "First Desc" , "First Location" , 127.3 , 381.1 , "0")
        val task2 = ReminderDTO("Second Title" , "Second Desc" , "Second Location" , 1912.7 , 512.4 , "1")

        db.reminderDao().saveReminder(task1)
        db.reminderDao().saveReminder(task2)

        // WHEN - Get all tasks in database.
        val loaded = db.reminderDao().getReminders()

        // THEN - The loaded data contains the expected values.
        assertThat<List<ReminderDTO>>(loaded , notNullValue())
        assertThat(loaded.size, `is`(not(0)))
        assertThat(loaded.size, `is`(2))

    }

    // insert tasks and delete all tasks then check that all deleted
    @Test
    fun insertTasksAndDeleteAll() = runBlockingTest {

        // GIVEN - Insert a tasks.
        val task1 = ReminderDTO("First Title" , "First Desc" , "First Location" , 127.3 , 381.1 , "0")
        val task2 = ReminderDTO("Second Title" , "Second Desc" , "Second Location" , 1912.7 , 512.4 , "1")

        db.reminderDao().saveReminder(task1)
        db.reminderDao().saveReminder(task2)


        // WHEN - Delete all tasks and Get tasks from the database.
        db.reminderDao().deleteAllReminders()
        val loaded = db.reminderDao().getReminders()

        // THEN - The loaded datais empty.
        assertThat(loaded.size, `is`(0))

    }

    @After
    fun closeDatabase() = db.close()

}