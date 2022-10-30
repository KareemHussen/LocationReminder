package com.udacity.project4.locationreminders

import com.udacity.project4.locationreminders.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.locationreminders.data.dto.Result

class FakeDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

    private var shouldReturnError = false

    fun setShouldReturnError(returnError : Boolean){
        shouldReturnError = returnError
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError){
            return Result.Error("Can't find Reminders")
        }

        reminders?.let {
            return Result.Success(it)
        }

        return Result.Error("Can't find Reminders")

    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError){
            return Result.Error("Can't find the Reminder")
        }

        val foundReminder = reminders?.find {
            it.id == id
        }

        foundReminder?.let {
            return Result.Success(it)
        }

        return Result.Error("Can't find the Reminder")

    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }


}