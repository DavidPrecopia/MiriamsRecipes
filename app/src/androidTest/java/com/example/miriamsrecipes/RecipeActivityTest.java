package com.example.miriamsrecipes;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.miriamsrecipes.activities.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isCompletelyAbove;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {
	
	@Rule
	public final ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);
	
	
	@Test
	public void testIngredientsLoaded() {
		IdlingRegistry.getInstance().register(mainActivityTestRule.getActivity().getCountingIdlingResource());
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		onView(withId(R.id.ingredient_list_item)).check(isCompletelyAbove(withId(R.id.recycler_view)));
	}
	
	@Test
	public void testStepNavigation() {
		IdlingRegistry.getInstance().register(mainActivityTestRule.getActivity().getCountingIdlingResource());
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		onView(withId(R.id.iv_next_arrow)).perform(click());
		onView(withId(R.id.tv_current_step)).check(matches(withText("2")));
	}
}