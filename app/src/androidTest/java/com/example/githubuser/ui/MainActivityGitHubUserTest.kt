package com.example.githubuser.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.githubuser.R
import com.example.githubuser.ui.setting.SettingPreferences
import com.example.githubuser.ui.setting.dataStore
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class MainActivityGitHubUserTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivityGitHubUser::class.java)

    @Test
    fun testThemeChange() {
        activityRule.scenario.onActivity { activity ->
            val pref = SettingPreferences.getInstance(activity.dataStore)
            runBlocking {
                pref.saveThemeSetting(true)
            }
        }

        onView(withId(R.id.nav_view)).check(matches(isDisplayed()))
    }

}