package dominando.android.livros

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dominando.android.presentation.Router

abstract class BaseFragment : Fragment() {

//    val navController: NavController
//        get() = Navigation.findNavController(requireActivity(), R.id.navHost)
    protected val router: Router by lazy {
        (activity as BookActivity).router
    }
}
