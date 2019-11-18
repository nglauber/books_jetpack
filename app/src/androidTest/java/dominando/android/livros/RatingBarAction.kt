package dominando.android.livros

import android.view.View
import android.widget.RatingBar
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher

class RatingBarAction : ViewAction {
    private var rating: Float = 0f
    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(RatingBar::class.java)
    }
    override fun getDescription(): String {
        return "RatingBar with rating $rating"
    }
    override fun perform(uiController: UiController, view: View) {
        val ratingBar = view as RatingBar
        ratingBar.rating = rating.toFloat()
    }

    companion object {
        fun setRating(value: Float): RatingBarAction {
            val ratingBarAction = RatingBarAction()
            ratingBarAction.rating = value
            return ratingBarAction
        }
    }
}
