package com.sidprice.android.baking_app;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sidprice.android.baking_app.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * Based upon -> https://github.com/googlesamples/android-testing/tree/master/ui/espresso/RecyclerViewSample
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BakingAppNavigationInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.sidprice.android.baking_app", appContext.getPackageName());
    }

    @Test
    public void BakingApp_UI_navigation_checks() {
        // Click on the first entry of the recipe list
        onView(ViewMatchers.withId(R.id.cards_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Check that the "Ingredients" list has been displayed
        onView(withText("Ingredients")).check(matches(isDisplayed()));

        // Click on a step on this page
        onView(ViewMatchers.withId(R.id.recipe_step_detail_recycler_view))
            .perform((RecyclerViewActions.actionOnItemAtPosition(0, click())));

        // Check "Previous" button is hidden
        onView(ViewMatchers.withId(R.id.step_previous)).check(matches(not(isDisplayed()))) ;

        // Click on the "Next" button
        onView(ViewMatchers.withId(R.id.step_next))
                .perform(click()) ;

        // Now check the "Previous" button is visible
        onView(ViewMatchers.withId(R.id.step_previous)).check(matches(isDisplayed())) ;
    }
}
