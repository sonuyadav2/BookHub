package com.example.bookhub

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.sql.Connection
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerDashborad: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager


    //lateinit var btnCheckInternet: Button

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    //val booklist = arrayListOf("sonu ", "yadav")
    val bookInfoList = arrayListOf<Book>()
    /*
        Book("P.S. I love You", "Cecelia Ahern", "Rs. 299", "4.5", R.drawable.ps_ily),
        Book("The Great Gatsby", "F. Scott Fitzgerald", "Rs. 399", "4.1", R.drawable.great_gatsby),
        Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
        Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
        Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
        Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
        Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
        Book("The Adventures of Huckleberry Finn","Mark Twain","Rs. 699","4.5", R.drawable.adventures_finn),
        Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
        Book("The Lord of the Rings", "J.R.R Tolkien", "Rs. 749", "5.0", R.drawable.lord_of_rings)
*/


    ///////////////////this is for rating comparision /////////
    var ratingComparison= kotlin.Comparator<Book>{book1,book2->
        if(book1.bookRating.compareTo(book2.bookRating,true)==0){
            book1.bookName.compareTo(book2.bookName,true)
        }else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }

    lateinit var recycleAdapter: DashboardRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        recyclerDashborad = view.findViewById(R.id.recyclerDashboard)

        //    btnCheckInternet = view.findViewById(R.id.btnCheckInternet)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.VISIBLE //it set the progress bar visible


        /*  btnCheckInternet.setOnClickListener {
              if (ConnectionManager().checkConnectivity(activity as Context)) {
                  val dialog = AlertDialog.Builder(activity as Context)
                  dialog.setTitle("Success")
                  dialog.setMessage("Internet Connection Found")
                  dialog.setPositiveButton("ok") { text, listener ->
                  }
                  dialog.setNegativeButton("Cancel") { text, listener ->
                  }
                  dialog.create()
                  dialog.show()
              } else {
                  val dialog = AlertDialog.Builder(activity as Context)
                  dialog.setTitle("Error")
                  dialog.setMessage("Internet Connection is not Found")
                  dialog.setPositiveButton("ok") { text, listener ->
                  }
                  dialog.setNegativeButton("Cancel") { text, listener ->

                  }
                  dialog.create()
                  dialog.show()

              }
          }
  */
        layoutManager = LinearLayoutManager(activity)
        recycleAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
////////////////////here we will handel the response
                    try {

                        progressLayout.visibility = View.GONE //this will hide the progress bar
                        val success = it.getBoolean("success")
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")

                                )
                                bookInfoList.add(bookObject)
                                recyclerDashborad.adapter = recycleAdapter

                                recyclerDashborad.layoutManager = layoutManager

                            /*    recyclerDashborad.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerDashborad.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )*/

                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "some error occur",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "some unexpected error occurred!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }


                }, Response.ErrorListener {
////////////////////////// here we take a errors/////////////
                    Toast.makeText(
                        activity as Context,
                        "Volley Error Ocurred!!!",
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
            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Setting") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()//it recreat the file
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)//this will close the app

            }
            dialog.create()
            dialog.show()


        }
        return view
    }
////////////////this is for the sort button
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.main_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparison)
            bookInfoList.reverse()
        }
        recycleAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}