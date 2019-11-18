package dominando.android.livros

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dominando.android.presentation.Router
import dominando.android.presentation.auth.Auth
import dominando.android.presentation.auth.AuthStateListener
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class BookActivity : AppCompatActivity() {

    val router: Router by inject { parametersOf(this@BookActivity) }

    val auth: Auth<Int, Intent> by inject { parametersOf(this@BookActivity) }

    private val authListener: AuthStateListener =
            object : AuthStateListener {
                override fun onAuthChanged(isLoggedIn: Boolean) {
                    if (!isLoggedIn) {
                        router.showLogin()
                    }
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthChangeListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthChangeListener(authListener)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            try {
                auth.handleSignInResult(data,
                        {
                            router.showBooksList()
                        },
                        {
                            showErrorSignIn()
                        })
            } catch (e: Exception) {
                showErrorSignIn()
            }
        }
    }

    fun startSignIn() {
        auth.startSignIn(RC_GOOGLE_SIGN_IN)
    }

    private fun showErrorSignIn() {
        Toast.makeText(this, R.string.error_google_sign_in, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }
}
