package dominando.android.livros

import android.view.View
import android.widget.RatingBar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class RatingBarMatcher(private val rating: Float)
    : BoundedMatcher<View, RatingBar>(RatingBar::class.java) {
    override fun describeTo(description: Description?) {
        description
                ?.appendText("with hint rating value:")
                ?.appendValue(rating)
    }
    override fun matchesSafely(item: RatingBar): Boolean {
        return item.rating == rating
    }
    companion object {
        fun withRatingValue(rating: Float): RatingBarMatcher {
            return RatingBarMatcher(rating)
        }
    }
}