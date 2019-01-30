package dominando.android.livros

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dominando.android.livros.common.FilePicker
import dominando.android.livros.databinding.FragmentBookFormBinding
import dominando.android.presentation.BookFormViewModel
import dominando.android.presentation.BookVmFactory
import dominando.android.presentation.ViewState
import dominando.android.presentation.binding.Book
import dominando.android.presentation.binding.MediaType
import dominando.android.presentation.binding.Publisher

class BookFormFragment : BaseFragment() {
    private val viewModel: BookFormViewModel by lazy {
        ViewModelProviders.of(this,
                BookVmFactory(requireActivity().application)
        ).get(BookFormViewModel::class.java)
    }
    private lateinit var binding: FragmentBookFormBinding
    private val filePicker: FilePicker by lazy {
        FilePicker(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_book_form, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.book == null) {
            viewModel.book = arguments?.getParcelable("book") ?: Book()
        }
        binding.content.book = viewModel.book
        binding.content.publishers = listOf(
                Publisher("1", "Novatec"),
                Publisher("2", "Outra")
        )
        binding.content.presenter = this
        binding.setLifecycleOwner(this)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RC_CAMERA) {
            binding.content.book?.coverUrl = "file://${viewModel.tempImageFile?.absolutePath}"
        }
    }

    private fun init() {
        viewModel.getState().observe(this, Observer { event ->
            event?.peekContent()?.let { state ->
                when (state.status) {
                    ViewState.Status.LOADING -> {
                        binding.content.btnSave.isEnabled = false
                        binding.content.progressBar.visibility = View.VISIBLE
                    }
                    ViewState.Status.SUCCESS -> {
                        binding.content.btnSave.isEnabled = true
                        binding.content.progressBar.visibility = View.GONE
                        showMessageSuccess()
                        navController.popBackStack()
                    }
                    ViewState.Status.ERROR -> {
                        event.consumeEvent()
                        binding.content.btnSave.isEnabled = true
                        binding.content.progressBar.visibility = View.GONE
                        showErrorMessage(R.string.message_error_book_saved)
                    }
                }
            }
        })
    }

    fun onMediaTypeChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (isChecked) {
            if (buttonView === binding.content.rbMediaEbook) {
                binding.content.book?.mediaType = MediaType.EBOOK
            } else if (buttonView === binding.content.rbMediaPaper) {
                binding.content.book?.mediaType = MediaType.PAPER
            }
        }
    }

    fun clickSaveBook(view: View) {
        val book = binding.content.book
        if (book != null) {
            viewModel.saveBook(book)
        }
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
    }
}
