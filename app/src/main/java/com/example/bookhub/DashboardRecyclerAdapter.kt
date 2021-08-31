package com.example.bookhub

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context:Context,val itemList :ArrayList<Book>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
      //val text=itemList[position]
       // holder.textView.text=text
        val book=itemList[position]
        holder.txtBookName.text=book.bookName
        holder.txtBookAuther.text=book.bookAuthor
        holder.txtBookCost.text=book.bookPrice
        holder.txtBookRating.text=book.bookRating
        //holder.imgBookImage.setImageResource(book.bookImage)
       Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)
        holder.mainContenr.setOnClickListener {
        val intent=Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        //  Toast.makeText(context,"Clicked On ${holder.txtBookName.text}",Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //////////////////////// to apply view holder////////////
    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val textView: TextView = view.findViewById(R.id.txtBookName)
        val txtBookName:TextView=view.findViewById(R.id.txtBookName)
        val txtBookAuther:TextView=view.findViewById(R.id.txtBookAuther)
        val txtBookRating:TextView=view.findViewById(R.id.txtBookRating)
        val txtBookCost:TextView=view.findViewById(R.id.txtBookPrice)
        val imgBookImage:ImageView=view.findViewById(R.id.imgBookImage)
        val mainContenr:LinearLayout=view.findViewById(R.id.mainContent)
    }
}