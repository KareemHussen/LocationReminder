package com.udacity.project4.locationreminders

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.locationreminders.reminderslist.ReminderListFragment
import com.udacity.project4.locationreminders.locationreminders.reminderslist.ReminderListFragmentDirections
import com.udacity.project4.locationreminders.locationreminders.reminderslist.RemindersListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : KoinTest {


    //data sorce
    private val reminderDataSource: ReminderDataSource by inject()

    // setup Repository
    @Before
    fun initRepository() {
        //stop app koin
        stopKoin()

        //use Koin Library as a service locator
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    get(),
                    get()
                )
            }
            single {
                FakeDataSource() as ReminderDataSource
            }
        }
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(listOf(myModule))
        }
    }

    // click on reminder item and check for it in Fragment UI
    @Test
    fun clickAddReminder_navigateToAddReminder() = runBlockingTest{

        // Given - the fragment scenirio
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // When - clicking add FAB
        onView(withId(R.id.addReminderFAB)).perform(click())

        // Then - navigate without error
        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )
    }

    // Add reminder and check for it in Fragment UI
    @Test
    fun reminderFragment_DisplayedUI() = runBlockingTest{
        // Given - add reminder to DB
        val task1 = ReminderDTO("First Title" , "First Desc" , "First Location" , 127.3 , 381.1 , "0")
        reminderDataSource.saveReminder(task1)

        // When - launching fragment
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        // Then - checking data is shown to the screen
        onView(withId(R.id.title)).check(matches (withText(task1.title)))
        onView(withId(R.id.title)).check(matches (isDisplayed()))
        onView(withId(R.id.description)).check(matches (withText(task1.description)))
        onView(withId(R.id.description)).check(matches (isDisplayed()))
        onView(withId(R.id.noDataTextView)).check(matches (not(isDisplayed())))

    }


    //closing DB after done
    @After
    fun cleanupDb() = runBlockingTest {
        reminderDataSource.deleteAllReminders()
    }

}