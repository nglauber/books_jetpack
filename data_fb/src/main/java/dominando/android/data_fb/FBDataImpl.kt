package dominando.android.data_fb

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import dominando.android.data.model.BookData
import dominando.android.data.source.FBData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal class FBDataImpl : FBData {

    private val fbAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference.child(BOOKS_KEY)

    override suspend fun saveBook(book: BookData) {
        return suspendCoroutine { continuation ->
            val currentUser = fbAuth.currentUser
            if (currentUser == null) {
                continuation.resumeWithException(RuntimeException("Unauthorized used."))
            } else {
                val db = firestore
                val collection = db.collection(BOOKS_KEY)
                val saveTask = if (book.id.isBlank()) {
                    collection.add(book)
                            .continueWithTask { task ->
                                val doc = task.result
                                book.id = doc?.id ?: UUID.randomUUID().toString()
                                doc?.update(mapOf(USER_ID_KEY to currentUser.uid, ID_KEY to book.id))
                            }
                } else {
                    collection.document(book.id)
                            .set(book, SetOptions.merge())
                }
                saveTask
                        .continueWith { task ->
                            if (task.isSuccessful) {
                                if (book.coverUrl.startsWith("file://")) {
                                    uploadFile(book)
                                }
                            } else {
                                continuation.resumeWithException(RuntimeException("Fail to save book."))
                            }
                        }
                        .addOnSuccessListener { continuation.resume(Unit) }
                        .addOnFailureListener { e -> continuation.resumeWithException(e) }
            }
        }
    }

    private fun uploadFile(book: BookData) {
        uploadPhoto(book).continueWithTask { urlTask ->
            File(book.coverUrl).delete()
            book.coverUrl = urlTask.result.toString()
            firestore.collection(BOOKS_KEY)
                    .document(book.id)
                    .update(COVER_URL_KEY, book.coverUrl)
        }.addOnFailureListener {
            throw RuntimeException("Fail to upload book's cover.")
        }
    }

    override fun loadBooks(): Flow<List<BookData>> {
        return callbackFlow {
            val currentUser = fbAuth.currentUser
            val subscription = firestore.collection(BOOKS_KEY)
                    .whereEqualTo(USER_ID_KEY, currentUser?.uid)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            close(e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null && !snapshot.isEmpty) {
                            val books = snapshot.map { document ->
                                document.toObject(BookData::class.java)
                            }
                            books.let {
                                offer(it)
                            }
                        } else {
                            offer(emptyList<BookData>())
                        }
                    }
            awaitClose {
                subscription.remove()
            }
        }
    }

    override fun loadBook(bookId: String): Flow<BookData?> {
        return callbackFlow {
            val subscription = firestore.collection(BOOKS_KEY)
                    .document(bookId)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            close(e)
                            return@addSnapshotListener
                        }
                        if (snapshot != null && snapshot.exists()) {
                            val book = snapshot.toObject(BookData::class.java)
                            book?.let {
                                offer(it)
                            }
                        } else {
                            offer(null)
                        }
                    }
            awaitClose { subscription.remove() }
        }
    }

    override suspend fun remove(book: BookData) {
        return suspendCoroutine { continuation ->
            val db = firestore
            db.collection(BOOKS_KEY)
                    .document(book.id)
                    .delete()
                    .addOnCompleteListener {
                        if (book.coverUrl.isNotBlank()) {
                            storageRef.child(book.id)
                                    .delete()
                                    .addOnSuccessListener {
                                        continuation.resume(Unit)
                                    }
                                    .addOnFailureListener { e ->
                                        continuation.resumeWithException(e)
                                    }
                        } else {
                            continuation.resume(Unit)
                        }
                    }
                    .addOnFailureListener { e ->
                        continuation.resumeWithException(e)
                    }
        }
    }

    private fun uploadPhoto(book: BookData): Task<Uri> {
        compressPhoto(book.coverUrl)
        val storageRef = storageRef.child(book.id)
        return storageRef.putFile(Uri.parse(book.coverUrl))
                .continueWithTask { uploadTask ->
                    uploadTask.result?.storage?.downloadUrl
                }
    }

    private fun compressPhoto(path: String) {
        val imgFile = File(path.substringAfter("file://"))
        val bos = ByteArrayOutputStream()
        val bmp = BitmapFactory.decodeFile(imgFile.absolutePath)
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos)
        val fos = FileOutputStream(imgFile)
        fos.write(bos.toByteArray())
        fos.flush()
        fos.close()
    }

    companion object {
        const val BOOKS_KEY = "books"
        const val USER_ID_KEY = "userId"
        const val ID_KEY = "id"
        const val COVER_URL_KEY = "coverUrl"
    }
}
