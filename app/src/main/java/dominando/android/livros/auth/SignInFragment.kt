package dominando.android.livros.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dominando.android.livros.BookActivity
import dominando.android.livros.R
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSignIn.setOnClickListener {
            if (activity is BookActivity) {
                (activity as BookActivity).startSignIn()
            }
        }
    }
}
