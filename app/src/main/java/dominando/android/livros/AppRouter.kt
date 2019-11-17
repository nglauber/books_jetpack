package dominando.android.livros

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import dominando.android.presentation.Router
import dominando.android.presentation.binding.Book

class AppRouter(
        activity: AppCompatActivity
): Router {

    private val navController: NavController by lazy {
        Navigation.findNavController(activity, R.id.navHost)
    }
    private val rootScreens = setOf(R.id.signInFragment, R.id.listBooks)

    init {
        val appBarConfiguration = AppBarConfiguration.Builder(rootScreens).build()
        NavigationUI.setupActionBarWithNavController(activity, navController, appBarConfiguration)
    }

    override fun showLogin() {
        val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(R.id.signInFragment, false)
                .build()
        navController.navigate(R.id.signInFragment, null, options)
    }

    override fun showBooksList() {
        navController.navigate(R.id.listBooks)
    }

    override fun showBookForm(book: Book?) {
        val navOptions = NavOptions.Builder().apply {
            setEnterAnim(R.anim.slide_in_left)
            setExitAnim(R.anim.slide_out_left)
            setPopEnterAnim(R.anim.slide_in_right)
            setPopExitAnim(R.anim.slide_out_right)
        }.build()

        val args = book?.let {
            Bundle().apply {
                putParcelable("book", it)
            }
        }
        navController.navigate(R.id.formBook, args, navOptions)
    }

    override fun showBookDetails(book: Book) {
        val args = Bundle().apply {
            putParcelable("book", book)
        }
        navController.navigate(R.id.action_list_to_details, args)
    }

    override fun back() {
        navController.popBackStack()
    }

    override fun navigationUp(): Boolean {
        return navController.navigateUp()
    }

    override fun isInRootScreen(): Boolean {
        return rootScreens.contains(navController.currentDestination?.id)
    }
}