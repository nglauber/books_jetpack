package dominando.android.livros

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dominando.android.livros.RatingBarAction.Companion.setRating
import dominando.android.livros.RatingBarMatcher.Companion.withRatingValue
import dominando.android.presentation.auth.Auth
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

// Run with the command below because via terminal we can disable animations
// gradlew connectedAndroidTest
@RunWith(AndroidJUnit4::class)
class BooksCrudTest {
    @get:Rule
    val activityRule = ActivityTestRule(BookActivity::class.java)

    init {
        loadKoinModules(module {
            factory(override = true) {
                object : Auth<Int, Intent>() {
                    override fun isLoggedIn(): Boolean = true // skip login screen

                    override fun startSignIn(authInfo: Int?) {
                        TODO("not implemented") // this should not be called during the test
                    }
                    override fun handleSignInResult(result: Intent?, onSuccess: () -> Unit, onError: () -> Unit) {
                        TODO("not implemented") // this should not be called during the test
                    }
                    override fun signOut() {
                        TODO("not implemented") // this should not be called during the test
                    }
                } as Auth<Int, Intent>
            }
        })
    }

    @Test
    fun crudTest() {
        add()
        edit()
        delete()
    }

    private fun add() {
        onView(withId(R.id.fabAdd)).perform(click())
        fillBookForm(BOOK_TITLE, BOOK_AUTHOR, BOOK_PAGES, BOOK_YEAR, true, BOOK_RATING)
        listViewHasBookWithTitle(BOOK_TITLE)
    }

    private fun edit() {
        val pagesText = activityRule.activity.resources
                .getString(R.string.text_format_book_pages, CHANGED_BOOK_PAGES)
        val yearText = activityRule.activity.resources
                .getString(R.string.text_format_book_year, CHANGED_BOOK_YEAR)

        clickOnHotelName(BOOK_TITLE)
        onView(withId(R.id.menu_edit_book)).perform(click())
        fillBookForm(CHANGED_BOOK_TITLE, CHANGED_BOOK_AUTHOR, CHANGED_BOOK_PAGES, CHANGED_BOOK_YEAR, false, CHANGED_BOOK_RATING)
        onView(withId(R.id.txtTitle)).check(matches(withText(CHANGED_BOOK_TITLE)))
        onView(withId(R.id.txtAuthor)).check(matches(withText(CHANGED_BOOK_AUTHOR)))
        onView(withId(R.id.txtPages)).check(matches(withText(pagesText)))
        onView(withId(R.id.txtYear)).check(matches(withText(yearText)))

        onView(withId(R.id.rtbRating)).check(matches(withRatingValue(CHANGED_BOOK_RATING)))
        pressBack()
        listViewHasBookWithTitle(CHANGED_BOOK_TITLE)
    }

    private fun delete() {
        onView(withId(R.id.rvBooks)).perform(
                RecyclerViewActions.actionOnItemAtPosition<BookAdapter.ViewHolder>(0, GeneralSwipeAction(
                        Swipe.FAST, GeneralLocation.BOTTOM_RIGHT, GeneralLocation.BOTTOM_LEFT,
                        Press.FINGER)))
    }

    private fun fillBookForm(
        title: String,
        author: String,
        pages: Int,
        year: Int,
        available: Boolean,
        rating: Float
    ) {
        onView(withId(R.id.edtTitle)).perform(replaceText(title))
        onView(withId(R.id.edtAuthor)).perform(replaceText(author))
        onView(withId(R.id.edtPages)).perform(replaceText(pages.toString()))
        onView(withId(R.id.edtYear)).perform(replaceText(year.toString()))
        if (available) {
            onView(withId(R.id.chkAvailable)).perform(click())
        }
        onView(withId(R.id.rtbRating)).perform(setRating(rating))
        closeSoftKeyboard()
        onView(withId(R.id.btnSave)).perform(click())
    }

    private fun listViewHasBookWithTitle(title: String) {
        onView(withText(title)).check(matches(isDisplayed()))
    }

    private fun clickOnHotelName(title: String) {
        onView(withId(R.id.rvBooks))
                .perform(
                        RecyclerViewActions.actionOnItem<BookAdapter.ViewHolder>(
                                hasDescendant(withText(title)), click()
                        )
                )
    }

    companion object {
        private const val BOOK_TITLE = "Meu livro"
        private const val BOOK_AUTHOR = "Nelson"
        private const val BOOK_PAGES = 940
        private const val BOOK_YEAR = 2014
        private const val BOOK_RATING = 3.5f
        private const val CHANGED_BOOK_TITLE = "Dominando o Android"
        private const val CHANGED_BOOK_AUTHOR = "Nelson Glauber"
        private const val CHANGED_BOOK_PAGES = 1046
        private const val CHANGED_BOOK_YEAR = 2019
        private const val CHANGED_BOOK_RATING = 5.0f
    }
}
