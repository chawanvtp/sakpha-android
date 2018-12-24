package com.firebaseapp.sakpha_thailand.sakpha


import android.content.Context
import android.graphics.Color
import android.support.annotation.LayoutRes
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.firebaseapp.sakpha_thailand.sakpha.R.id.*
import com.firebaseapp.sakpha_thailand.sakpha.R.layout.order_list_item
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.order_list_item.*
import kotlinx.android.synthetic.main.*
import kotlinx.android.synthetic.main.order_list_item.view.*

class OrderAdapter(val items : ArrayList<Order>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        public const val TAG = "KotlinActivity"
    }
    var msg = ""
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("message")
    val orderListRef = database.getReference("OrderDetail")

    override fun getItemCount(): Int {
        Log.d(MainActivity.TAG,"item.SIZE ====: ${items.size}")
        return items.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        Log.d(MainActivity.TAG,"onBind p1 ==: ${p1}")
        p0?.order_id?.text = items.get(p1).key.toString()
        p0?.order_status_btn?.text = items.get(p1).status.toString()
        p0?.order_detail?.text = items.get(p1).detail.toString()
        p0?.order_timestamp?.text = items.get(p1).timestamp.toString()
        p0?.order_mobile?.text = items.get(p1).mobile.toString()
        p0?.order_email?.text = items.get(p1).email.toString()

        var stat = items.get(p1).status.toString()
        if(stat=="ready") {
            p0?.parent_view.setBackgroundColor(Color.rgb(0,153,51))
        }else if(stat=="inprogress"){
            p0?.parent_view.setBackgroundColor(Color.rgb(235,248,175))
        }else if(stat=="complete"){
            p0?.parent_view.setBackgroundColor(Color.WHITE)
        }
        p0?.order_status_btn.setOnClickListener {
            Log.d(MainActivity.TAG,"Adapter - CLICKED :==: ${p0.order_id.text}")
            var key: String = p0.order_id.text.toString()
            var timestamp: String = p0.order_timestamp.text.toString()
            statusBtnClicked(key,timestamp)
        }


    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        Log.d(MainActivity.TAG,"onCreate p1 ==: ${p1}")
        Log.d(MainActivity.TAG,"onCreate p1 ==: ${p0}")
        return ViewHolder(LayoutInflater.from(context).inflate(order_list_item, p0, false))
    }



    private fun showToast(context: Context , message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

//    // Gets the number of animals in the list
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    // Inflates the item views
//    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
//        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false))
//    }
//
//    // Binds each animal in the ArrayList to a view
//    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
//        holder?.order_list_item?.text = items.get(position)
//    }


    fun statusBtnClicked(key: String, timestamp: String){
        // Initialize a new instance of
        val builder = AlertDialog.Builder(context)
        var month = timestamp.substring(3)
        var day: String = timestamp.substring(0,2)


        // Set the alert dialog title
        builder.setTitle("Updating Status")

        // Display a message on alert dialog
        builder.setMessage("Order Key: ${day} ${month}")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Complete"){dialog, which ->

            orderListRef.child("2018/"+month+"/"+day+"/"+key+"/status").setValue("complete")
            // Do something when user press the positive button
            Toast.makeText(context,"Status updated to: Complete"+msg, Toast.LENGTH_SHORT).show()

            // Change the app background color
            //root_layout.setBackgroundColor(Color.RED)
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("Ready"){dialog,which ->
            orderListRef.child("2018/"+month+"/"+day+"/"+key+"/status").setValue("ready")
            Toast.makeText(context,"Status updated to: Ready", Toast.LENGTH_SHORT).show()
        }


        // Display a neutral button on alert dialog
        builder.setNeutralButton("inProgress"){_,_ ->
            orderListRef.child("2018/"+month+"/"+day+"/"+key+"/status").setValue("inprogress")
            Toast.makeText(context,"Status updated to: inProgress", Toast.LENGTH_SHORT).show()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}


class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val order_id = view.order_id
    val order_status_btn = view.order_status_btn
    val order_detail = view.order_detail
    val order_timestamp = view.order_timestamp
    val order_mobile = view.order_mobile
    val order_email = view.order_email

    val parent_view: View = view
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(getAdapterPosition(), getItemViewType())
    }
    return this
}

