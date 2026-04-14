package com.example.birthdaylist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.birthdaylist.screens.HomeScreen
import com.example.birthdaylist.ui.theme.BirthdayListTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addButtonIsDisplayed() {
        composeTestRule.setContent {
            BirthdayListTheme {
                HomeScreen()
            }
        }

        // Check if the FloatingActionButton with content description "Add Friend" exists and is displayed
        composeTestRule.onNodeWithContentDescription("Add Friend").assertIsDisplayed()
    }
}
