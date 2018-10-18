package dominando.android.data_fb

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import dominando.android.data.BooksRepository
import dominando.android.data.model.Book
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FbRepository : BooksRepository {
    private val fbAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference.child(BOOKS_KEY)

    override fun saveBook(book: Book): Completable {
        return Completable.create { emitter ->
            val currentUser = fbAuth.currentUser
            if (currentUser == null) {
                Completable.error(RuntimeException("Unauthorized used."))
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
                                } else {
                                    emitter.onComplete()
                                }
                            } else {
                                emitter.onError(RuntimeException("Fail to upload book's cover."))
                            }
                        }
                        .addOnSuccessListener { emitter.onComplete() }
                        .addOnFailureListener { e -> emitter.onError(e) }

            }
        }
    }

    private fun uploadFile(book: Book) {
        uploadPhoto(book).continueWithTask { urlTask ->
            File(book.coverUrl).delete()
            book.coverUrl = urlTask.result.toString()
            firestore.collection(BOOKS_KEY)
                    .document(book.id)
                    .update(COVER_URL_KEY, book.coverUrl)
        }.addOnCompleteListener { task ->
            Completable.complete()
        }.addOnFailureListener {
            Completable.error(RuntimeException("Fail to upload book's cover."))
        }
    }

    override fun loadBooks(): Flowable<List<Book>> {
        return Flowable.create({ emitter ->
            val currentUser = fbAuth.currentUser
            firestore.collection(BOOKS_KEY)
                    .whereEqualTo(USER_ID_KEY, currentUser?.uid)
                    .addSnapshotListener { snapshot, e ->
                        if (e == null) {
                            val books = snapshot?.map { document ->
                                document.toObject(Book::class.java)
                            }
                            books?.let { emitter.onNext(it) }

                        } else {
                            emitter.onError(e)
                        }
                    }
        }, BackpressureStrategy.LATEST)
    }

    override fun loadBook(bookId: String): Flowable<Book> {
        return Flowable.create({ emitter ->
            firestore.collection(BOOKS_KEY)
                    .document(bookId)
                    .addSnapshotListener { snapshot, e ->
                        if (e == null) {
                            val book = snapshot?.toObject(Book::class.java)
                            book?.let { emitter.onNext(it) }
                        } else {
                            emitter.onError(e)
                        }
                    }
        }, BackpressureStrategy.LATEST)
    }

    override fun remove(book: Book): Completable {
        return Completable.create { emitter ->
            val db = firestore
            db.collection(BOOKS_KEY)
                    .document(book.id)
                    .delete()
                    .addOnCompleteListener {
                        if (book.coverUrl.isNotBlank()) {
                            storageRef.child(book.id)
                                    .delete()
                                    .addOnSuccessListener {
                                        emitter.onComplete()
                                    }
                                    .addOnFailureListener { e ->
                                        emitter.onError(e)
                                    }
                        }
                    }
                    .addOnFailureListener { e ->
                        emitter.onError(e)
                    }
        }
    }

    private fun uploadPhoto(book: Book): Task<Uri> {
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