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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
	
	@Rule
	public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);
	
	@Test
	public void testIngredientsLoaded() {
		IdlingRegistry idlingRegistry = IdlingRegistry.getInstance();
		
		idlingRegistry.register(mainActivityTestRule.getActivity().getCountingIdlingResource());
		onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
		
		onView(withId(R.id.ingredient_list_item)).check(isCompletelyAbove(withId(R.id.recycler_view)));
	}
	
	
	
}
