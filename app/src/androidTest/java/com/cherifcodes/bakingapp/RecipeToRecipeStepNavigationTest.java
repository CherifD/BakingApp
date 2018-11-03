package com.cherifcodes.bakingapp;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;


@RunWith(AndroidJUnit4.class)
public class RecipeToRecipeStepNavigationTest {
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    private String[] resipeTitles = {"Nutella Pie", "Brownies", "Yellow Cake", "Cheesecake"};

    @Test
    public void testRecipesRecyclerViewDisplay() {
        onView(withId(R.id.rclv_recipes)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeToRecipeStepNativation() {

        for (int i = 0; i < resipeTitles.length; i++) {
            //In MainActivity, click on the first recipe
            onView(withId(R.id.rclv_recipes))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

            //Ensure that the viewIngredients (id = btn_view_ingredients) is displayed with the
            //correct label.
            onView(withId(R.id.btn_view_ingredients))
                    .check(matches(withText(R.string.view_ingredients_btn_label)))
                    .check(matches(isDisplayed()));
            //Ensure that the toolbar is showing the correct recipe
            onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                    .check(matches(withText(resipeTitles[i])));

            //Return to the MainActivity
            Espresso.pressBack();
        }
    }

}
