package dominando.android.presentation

import dominando.android.presentation.binding.Book

interface Router {
    fun showLogin()
    fun showBooksList()
    fun showBookForm(book: Book?)
    fun showBookDetails(book: Book)
    fun back()
    fun navigationUp(): Boolean
    fun isInRootScreen(): Boolean
}