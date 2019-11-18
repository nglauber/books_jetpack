package dominando.android.livros.di

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import dominando.android.livros.auth.FirebaseSignIn
import dominando.android.livros.router.AppRouter
import dominando.android.presentation.Router
import dominando.android.presentation.auth.Auth
import org.koin.dsl.module

val appModule = module {

    factory { (activity: FragmentActivity) ->
        AppRouter(activity) as Router
    }

    factory { (activity: FragmentActivity) ->
        FirebaseSignIn(activity) as Auth<Int, Intent>
    }
}
