package dominando.android.livros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import dominando.android.presentation.Router

class BookActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var authListener: FirebaseAuth.AuthStateListener =
            FirebaseAuth.AuthStateListener {
                if (it.currentUser == null) {
                    router.showLogin()
                }
            }

    val router: Router by lazy {
        AppRouter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        if (firebaseAuth.currentUser != null) {
            router.showBooksList()
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return router.navigationUp()
    }

    override fun onBackPressed() {
        if (router.isInRootScreen()) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
