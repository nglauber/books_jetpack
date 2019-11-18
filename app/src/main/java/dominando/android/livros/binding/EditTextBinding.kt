package dominando.android.livros.binding

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

object EditTextBinding {
    @JvmStatic
    @BindingAdapter("android:text")
    fun setTextFromInt(editText: EditText, value: Int) {
        if (getTextAsInt(editText) != value) {
            editText.setText(value.toString())
        }
    }
    @JvmStatic
    @InverseBindingAdapter(attribute = "android:text")
    fun getTextAsInt(editText: EditText): Int {
        return try {
            Integer.parseInt(editText.text.toString())
        } catch (e: Exception) {
            0
        }
    }
}
