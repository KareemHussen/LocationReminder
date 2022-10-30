package com.udacity.project4.locationreminders.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var viewModel : RemindersListViewModel
    private lateinit var fakeDataSource: FakeDataSource
    private val observer = Observer<List<ReminderDataItem>> {}

    // context and same thread
    @get:Rule
    var instantExcutor = InstantTaskExecutorRule()

    // test coroutine
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // initialzations
    @Before
    fun setUpViewModel() {
        stopKoin()
        fakeDataSource = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }

    // clearing data source after all tests are done.
    @After
    fun clearDataSource() = runBlockingTest {
        fakeDataSource.deleteAllReminders()
    }

    // test saving and loading data
    @Test
    fun remindersViewModelTest_AddData_CheckForValue() =
        mainCoroutineRule.runBlockingTest {

            // Given - having view model running with observer
            viewModel.remindersList.observeForever(observer)

            // When - loading data from View Model
            viewModel.loadReminders()
            val value = viewModel.remindersList.getOrAwaitValue()

            // Then - checking fetched data value not null
            assertThat(value, (not(nullValue())))

        }

    // test deleting data
    @Test
    fun invalidateShowNoData_showNoData_isTrue() = mainCoroutineRule.runBlockingTest {
        // Given - clearing data source
        fakeDataSource.deleteAllReminders()

        // When - loading data from View Model to make sure everything is updated and linked
        viewModel.loadReminders()

        // Then - checking that loaded values are empty and noData works
        assertThat(viewModel.showNoData.getOrAwaitValue(), `is`(true))
        assertThat(viewModel.remindersList.getOrAwaitValue().size, `is`(0))
    }

    // test loading and coroutines
    @Test
    fun loadTasks_loading() {
        // Pause dispatcher so you can verify initial values.
        mainCoroutineRule.pauseDispatcher()

        // Load the task in the view model.
        viewModel.loadReminders()

        // Then assert that the progress indicator is shown.
        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(true))

        // Execute pending coroutines actions.
        mainCoroutineRule.resumeDispatcher()

        // Then assert that the Resource is Loading
        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(false))
    }


}