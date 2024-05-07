package com.nizarfadlan.storyu

import android.content.Context
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nizarfadlan.storyu.presentation.ui.auth.SignInFragment
import com.nizarfadlan.storyu.presentation.ui.main.MainActivity
import com.nizarfadlan.storyu.presentation.ui.main.listStory.StoryListFragment
import com.nizarfadlan.storyu.presentation.ui.main.setting.SettingFragment
import com.nizarfadlan.storyu.utils.EspressoIdlingResource
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.endsWith
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SignInOutTest {
    private lateinit var instrumentationContext: Context
    private val email = "nizar@email.com"
    private val password = "nizar123"

    @Before
    fun setUp() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun test1LoadSignInScreen() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val titleScenario =
            launchFragmentInContainer<SignInFragment>(themeResId = R.style.Theme_StoryU)

        titleScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.auth_navigation)
            navController.setCurrentDestination(R.id.signInFragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(isEditTextInLayout(R.id.layout_login_email))
            .perform(replaceText(email))
        onView(isEditTextInLayout(R.id.layout_login_password))
            .perform(replaceText(password))

        onView(withId(R.id.btnSignIn)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSignIn)).perform(click())
        closeSoftKeyboard()
    }

    @Test
    fun test2LoadStoryListScreen() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val mainActivityScenario = ActivityScenario.launch(MainActivity::class.java)
        mainActivityScenario.onActivity { _ ->
            val titleScenario =
                launchFragmentInContainer<StoryListFragment>(themeResId = R.style.Theme_StoryU)

            titleScenario.onFragment { fragment ->
                navController.setGraph(R.navigation.main_navigation)
                navController.setCurrentDestination(R.id.storyListFragment)
                Navigation.setViewNavController(fragment.requireView(), navController)
            }
        }


        onView(withId(R.id.bottomNavigation)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomNavigation)).perform(NavigationViewActions.navigateTo(R.id.settingFragment))
    }

    @Test
    fun test3LoadSettingScreen() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val titleScenario =
            launchFragmentInContainer<SettingFragment>(themeResId = R.style.Theme_StoryU)

        titleScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.main_navigation)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.action_logout)).check(matches(isDisplayed()))
        onView(withId(R.id.action_logout)).perform(click())
    }

    private fun isEditTextInLayout(parentViewId: Int): Matcher<View> {
        return allOf(
            isDescendantOfA(withId(parentViewId)),
            withClassName(endsWith("EditText"))
        )
    }
}