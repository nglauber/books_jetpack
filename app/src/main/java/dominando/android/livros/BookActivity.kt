package dominando.android.livros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth

class BookActivity : AppCompatActivity() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var authListener: FirebaseAuth.AuthStateListener =
            FirebaseAuth.AuthStateListener {
                if (it.currentUser == null) {
                    goToLogin()
                }
            }

    private val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.navHost)
    }
    private val rootScreens = setOf(R.id.signInFragment, R.id.listBooks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        val appBarConfiguration = AppBarConfiguration.Builder(rootScreens).build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
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
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        if (rootScreens.contains(navController.currentDestination?.id)) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun goToLogin() {
        val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.signInFragment, false)
                .build()
        navController.navigate(R.id.signInFragment, null, options)
    }
}
