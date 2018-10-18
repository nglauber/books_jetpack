package dominando.android.livros

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation

abstract class BaseFragment : Fragment() {

    val navController: NavController
        get() = Navigation.findNavController(requireActivity(), R.id.navHost)

}
