package com.example.bookhub

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {
    lateinit var descriptionBookName: TextView
    lateinit var descriptionBookAuther: TextView
    lateinit var descriptionBookPrice: TextView
    lateinit var descriptionBookRating: TextView
    lateinit var descriptionDes: TextView
    lateinit var descriptionBookImage: ImageView
    lateinit var btnAddToFav: Button
    lateinit var descriptionProcessLayout: RelativeLayout
    lateinit var descriptionProcessBar: ProgressBar
    lateinit var descriptionToolbar: Toolbar
    var bookid: String? = "100"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        descriptionBookName = findViewById(R.id.descriptionBookName)
        descriptionBookAuther = findViewById(R.id.descriptionBookAuther)
        descriptionBookPrice = findViewById(R.id.descriptionBookPrice)
        descriptionBookRating = findViewById(R.id.descriptionBookRating)
        descriptionDes = findViewById(R.id.descriptionDes)
        descriptionBookImage = findViewById(R.id.descriptionBookImage)
       descriptionProcessBar = findViewById(R.id.descriptionProgressBar)
        descriptionProcessBar.visibility = View.VISIBLE
        descriptionProcessLayout = findViewById(R.id.descriptionProgressLayout)
        descriptionProcessLayout.visibility = View.VISIBLE
        btnAddToFav = findViewById(R.id.btnAddToFav)
        descriptionToolbar = findViewById(R.id.descriptionToolBar)
        setSupportActionBar(descriptionToolbar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookid = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some UnExpected Error Occurs",
                Toast.LENGTH_LONG
            ).show()
        }
        if (bookid == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some UnExpected Error Occurs",
                Toast.LENGTH_LONG
            ).show()

        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        ///////////////this is to send somethink
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookid)

        val jsonRequest =
            object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                try {

                    val success = it.getBoolean("success")
                    if (success) {
                        val bookJsonObject = it.getJSONObject("book_data")
                        var a = bookJsonObject.getString("name")
                        val bookImageUrl = bookJsonObject.getString("image")

                        descriptionProcessLayout.visibility = View.GONE //this will hide the progress bar
                        descriptionBookName.text = bookJsonObject.getString("name")
                        descriptionBookAuther.text = bookJsonObject.getString("author")
                        descriptionBookPrice.text = bookJsonObject.getString("price")
                        descriptionBookRating.text = bookJsonObject.getString("rating")
                        descriptionDes.text = bookJsonObject.getString("description")
                        Picasso.get().load(bookJsonObject.getString("image"))
                            .error(R.drawable.default_book_cover).into(descriptionBookImage)
                        val bookEntity=BookEntity(
                            bookid?.toInt() as Int,
                            descriptionBookName.text.toString(),
                            descriptionBookAuther.text.toString(),
                            descriptionBookPrice.text.toString(),
                            descriptionBookRating.text.toString(),
                            descriptionDes.text.toString(),
                            bookImageUrl

                        )

              //////////////here checking for book a it is present in fav or not /////////////

                        val checkFav=DBAsyncTask(applicationContext,bookEntity,1).execute()
                        val isfav=checkFav.get()
                        if(isfav){
                            btnAddToFav.text="Remove From Favourite"
                            val favColor=ContextCompat.getColor(applicationContext,R.color.colorFav)
                            btnAddToFav.setBackgroundColor(favColor)
                        }else{
                            btnAddToFav.text="Add to Favourite"
                            val noFavColor=ContextCompat.getColor(applicationContext,R.color.black)
                            btnAddToFav.setBackgroundColor(noFavColor)
                        }
                        ///click on fav button
                        btnAddToFav.setOnClickListener {
                            if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){
                                val async=DBAsyncTask(applicationContext,bookEntity,2).execute()
                                val result=async.get()
                                if(result){
                                    Toast.makeText(this@DescriptionActivity,"Book Added to Fav",Toast.LENGTH_LONG).show()
                                    btnAddToFav.text="Remove to Favourite"
                                    val favColor=ContextCompat.getColor(applicationContext,R.color.colorFav)
                                    btnAddToFav.setBackgroundColor(favColor)
                                }else{
                                    Toast.makeText(this@DescriptionActivity,"some error occures!!",Toast.LENGTH_LONG).show()
                                }
                            }else{
                                val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                                val result=async.get()
                                if(result){
                                    Toast.makeText(this@DescriptionActivity,"Book Removed Added to Fav",Toast.LENGTH_LONG).show()
                                    btnAddToFav.text="Added to Favourite"
                                    val nofavColor=ContextCompat.getColor(applicationContext,R.color.black)
                                    btnAddToFav.setBackgroundColor(nofavColor)
                                }else{
                                    Toast.makeText(this@DescriptionActivity,"some error occures!!",Toast.LENGTH_LONG).show()
                                }
                            }
                        }


                    } else {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "some error occur",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "some unexpected error occurred!!",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }, Response.ErrorListener {
                Toast.makeText(
                    this@DescriptionActivity,
                    "volley error !!",
                    Toast.LENGTH_LONG
                ).show()

            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "7a09e6f4415445"
                    return headers
                }

            }
        queue.add(jsonRequest)
    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        var db = Room.databaseBuilder(context, BookDatabase::class.java, "book_db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    // check Db if book is fav or not
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                }
                2 -> {
                    //save the book into db as fav
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    // remove  the fav book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false

        }

    }
}