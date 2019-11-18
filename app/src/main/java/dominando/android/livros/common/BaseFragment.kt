package dominando.android.livros.common

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import dominando.android.livros.BookActivity
import dominando.android.presentation.Router
import dominando.android.presentation.auth.Auth

abstract class BaseFragment : Fragment() {
    val auth: Auth<Int, Intent>
            get() = (activity as BookActivity).auth

    val router: Router
            get() = (activity as BookActivity).router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}
