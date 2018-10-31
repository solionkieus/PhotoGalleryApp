package com.sekhon.jason.photogallery;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.sekhon.jason.photogallery.myapplication.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by jason on 2018-09-20.
 */

@RunWith(AndroidJUnit4.class)
public class UITests {

    String[] fromTimes;
    String[] toTimes;
    String[] minLats;
    String[] maxLats;
    String[] minLons;
    String[] maxLons;
    String[] captions;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    @Before
    public void SetUp(){
        fromTimes = new String[]{"2018/12/30", "20170101", "20150101"};
        toTimes = new String[]{"2018/09/20", "20190101", "20190101"};
        minLats = new String[]{"-90", "20", "0"};
        maxLats = new String[]{"90", "60", "1"};
        minLons = new String[]{"-180", "-150", "0"};
        maxLons = new String[]{"180", "-100", "1"};
        captions = new String[]{"apple", "banana"};
    }

        @Test
        public void TestTime() {
            onView(withId(R.id.btnFilter)).perform(click());
            onView(withId(R.id.search_toDate)).perform(typeText(fromTimes[0]), closeSoftKeyboard());
            onView(withId(R.id.search_fromDate)).perform(typeText(fromTimes[0]), closeSoftKeyboard());
            onView(withId(R.id.search_search)).perform(click());
            for (int i = 0; i <= 5; i++) {
                onView(withId(R.id.btnRight)).perform(click());
            }
            for (int i = 0; i <= 5; i++) {
                onView(withId(R.id.btnLeft)).perform(click());
            }
        }

        @Test
        public void TestTime2() {
            onView(withId(R.id.btnFilter)).perform(click());
            onView(withId(R.id.search_toDate)).perform(typeText(fromTimes[0]), closeSoftKeyboard());
            onView(withId(R.id.search_fromDate)).perform(typeText(toTimes[0]), closeSoftKeyboard());
            onView(withId(R.id.search_search)).perform(click());
            for (int i = 0; i <= 5; i++) {
                onView(withId(R.id.btnRight)).perform(click());
            }
            for (int i = 0; i <= 5; i++) {
                onView(withId(R.id.btnLeft)).perform(click());
            }
        }

        @Test
        public void TestLocation() {
            onView(withId(R.id.btnFilter)).perform(click());
            onView(withId(R.id.minLat)).perform(typeText(minLats[1]), closeSoftKeyboard());
            onView(withId(R.id.maxLat)).perform(typeText(maxLats[1]), closeSoftKeyboard());
            onView(withId(R.id.minLon)).perform(typeText(minLons[1]), closeSoftKeyboard());
            onView(withId(R.id.maxLon)).perform(typeText(maxLons[1]), closeSoftKeyboard());
            onView(withId(R.id.search_search)).perform(click());
            for (int i = 0; i <= 5; i++) {
                onView(withId(R.id.btnRight)).perform(click());
            }
            for (int i = 0; i <= 5; i++) {
                onView(withId(R.id.btnLeft)).perform(click());
            }
        }

        @Test
        public void TestCaption() {
            onView(withId(R.id.caption)).perform(typeText(captions[0]), closeSoftKeyboard());
            onView(withId(R.id.btnSave)).perform(click());
            onView(withId(R.id.btnFilter)).perform(click());
            onView(withId(R.id.caption)).perform(typeText(captions[0]), closeSoftKeyboard());
            onView(withId(R.id.search_search)).perform(click());
            for (int i = 0; i <= 5; i++) {
                onView(withId(R.id.btnRight)).perform(click());
            }
            for (int i = 0; i <= 5; i++) {
                onView(withId(R.id.btnLeft)).perform(click());
            }
        }

    @Test
    public void TestAll() {
        onView(withId(R.id.caption)).perform(typeText(captions[1]), closeSoftKeyboard());
        onView(withId(R.id.btnSave)).perform(click());
        onView(withId(R.id.btnFilter)).perform(click());
        onView(withId(R.id.search_fromDate)).perform(typeText(fromTimes[2]), closeSoftKeyboard());
        onView(withId(R.id.search_toDate)).perform(typeText(toTimes[2]), closeSoftKeyboard());
        onView(withId(R.id.minLat)).perform(typeText(minLats[1]), closeSoftKeyboard());
        onView(withId(R.id.maxLat)).perform(typeText(maxLats[1]), closeSoftKeyboard());
        onView(withId(R.id.minLon)).perform(typeText(minLons[1]), closeSoftKeyboard());
        onView(withId(R.id.maxLon)).perform(typeText(maxLons[1]), closeSoftKeyboard());
        onView(withId(R.id.caption)).perform(typeText(captions[1]), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnLeft)).perform(click());
        }
    }

    @After
    public void TearDown(){

    }
}