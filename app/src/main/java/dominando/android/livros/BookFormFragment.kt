package dominando.android.livros

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import dominando.android.livros.common.BaseFragment
import dominando.android.livros.common.Constants.EXTRA_BOOK
import dominando.android.livros.common.FilePicker
import dominando.android.livros.databinding.FragmentBookFormBinding
import dominando.android.presentation.BookFormViewModel
import dominando.android.presentation.ViewState
import dominando.android.presentation.binding.Book
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class BookFormFragment : BaseFragment() {

    private val viewModel: BookFormViewModel by viewModel()

    private val filePicker: FilePicker by lazy {
        FilePicker(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_book_form,
                container, false) as FragmentBookFormBinding

        return binding.run {
            lifecycleOwner = this@BookFormFragment
            content.presenter = this@BookFormFragment
            content.viewModel = this@BookFormFragment.viewModel
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Book>(EXTRA_BOOK)?.let {
            viewModel.setBook(it)
        }
        lifecycle.addObserver(viewModel)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RC_CAMERA) {
            viewModel.book().value?.coverUrl = "file://${viewModel.tempImageFile?.absolutePath}"
        }
    }

    private fun init() {
        viewModel.state().observe(viewLifecycleOwner, Observer { event ->
            event?.let { state ->
                when (state.status) {
                    ViewState.Status.LOADING -> Log.d(TAG, "Process is loading")
                    ViewState.Status.SUCCESS -> {
                        showMessageSuccess()
                        router.back()
                    }
                    ViewState.Status.ERROR -> {
                        showErrorMessage(R.string.message_error_book_saved)
                    }
                }
            }
        })
    }

    fun clickTakePhoto(view: View) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            val file = filePicker.createTempFile()
            viewModel.tempImageFile = file
            val photoUri = filePicker.uriFromFile(file)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePictureIntent, RC_CAMERA)
        }
    }

    private fun showMessageSuccess() {
        Toast.makeText(requireContext(), R.string.message_book_saved, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessage(@StringRes message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val RC_CAMERA = 1
        private const val TAG = "BookFormFragment"
    }
}
