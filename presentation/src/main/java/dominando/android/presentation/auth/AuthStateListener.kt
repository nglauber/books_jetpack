package dominando.android.presentation.auth

interface AuthStateListener {
    fun onAuthChanged(isLoggedIn: Boolean)
}
