package dominando.android.livros.auth

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dominando.android.livros.R
import dominando.android.presentation.auth.Auth

class FirebaseSignIn(private val activity: FragmentActivity) : Auth<Int, Intent>() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val authListener: FirebaseAuth.AuthStateListener =
            FirebaseAuth.AuthStateListener { fbAuth ->
                callbacks.forEach {
                    it.onAuthChanged(fbAuth.currentUser != null)
                }
            }

    private val googleApiClient: GoogleSignInClient by lazy {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                // .requestProfile() // uncomment if you need the profile
                .requestEmail()
                .build()
        GoogleSignIn.getClient(activity, options)
    }

    init {
        firebaseAuth.addAuthStateListener(authListener)
    }

    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

    override fun startSignIn(authInfo: Int?) {
        val signInIntent = googleApiClient.signInIntent
        activity.startActivityForResult(signInIntent, authInfo ?: 0)
    }

    override fun handleSignInResult(result: Intent?, onSuccess: () -> Unit, onError: () -> Unit) {
        val signInTask = GoogleSignIn.getSignedInAccountFromIntent(result)

        val account = signInTask.getResult(ApiException::class.java)
        require(account != null)
        firebaseAuthWithGoogle(account, onSuccess, onError)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun clear() {
        super.clear()
        callbacks.clear()
        firebaseAuth.removeAuthStateListener(authListener)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?, onSuccess: () -> Unit, onError: () -> Unit) {
        val credential = GoogleAuthProvider.getCredential(acct?.getIdToken(), null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError()
                    }
                }
    }
}
