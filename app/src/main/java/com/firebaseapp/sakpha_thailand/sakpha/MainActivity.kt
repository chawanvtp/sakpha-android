package com.firebaseapp.sakpha_thailand.sakpha

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.*
import com.google.android.gms.tasks.Task
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.order_list_item.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Comment


class MainActivity : AppCompatActivity() {


    companion object {
        internal const val TAG = "KotlinActivity"
    }

    var msg = ""
    //private var my_view = order_list
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("message")
    val orderListRef = database.getReference("OrderDetail")
    var orderList: ArrayList<Order> = ArrayList()
    var order = Order()
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {


        // Write a message to the database
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("message")

//        myRef.setValue("Hello, World!")

        //basicReadWrite()

        // My top posts by number of stars
        // [START basic_query_value_listener]
        // My top posts by number of stars
        orderListRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (yearSnapshot in dataSnapshot.children) {
                    // TODO: handle the post
                    orderList.clear()
                    val year = yearSnapshot.key

                    for (monthSnapshot in yearSnapshot.children){
                        val month = monthSnapshot.key
                        for (daySnapshot in monthSnapshot.children){
                            val day = daySnapshot.key
                            for ( keySnapshot in daySnapshot.children){
                                var value = keySnapshot.value
                                var orderKey = keySnapshot.key
//                                Log.d(TAG,"Key is: $key")
//                                Log.d(TAG,"Value is: $value")

                                var email: String? = ""
                                var mobile: String? = ""
                                var price: String? = ""
                                var remark: String? = ""
                                var rfidNum: String? = ""
                                var status: String? = ""
                                var timestamp: String? = ""
                                //var detail: List<String?>
                                var detail: String? = ""



                                if(orderKey != "dailyCount"){
                                   for ( orderSnapshot in keySnapshot.children){
                                       val value = orderSnapshot.value
                                       val key = orderSnapshot.key
                                       if(key=="detail"){
                                           detail = value.toString()
                                       }else if(key=="email"){
                                           email = value.toString()
                                       }else if(key=="mobile"){
                                           mobile = value.toString()
                                       }else if(key=="price"){
                                           price = value.toString()
                                       }else if(key=="remark"){
                                           remark = value.toString()
                                       }else if(key=="rfidNum"){
                                           rfidNum = value.toString()
                                       }else if(key=="status"){
                                           status = value.toString()
                                       }else if(key=="timestamp"){
                                           timestamp = value.toString()
                                           order = Order(orderKey,email,mobile,price,remark,rfidNum,status,timestamp,detail)
//                                           Log.d(TAG,"Created ORDER: $order")
//                                           //orderList.add(order)
                                           addOrder(order)
////                                           Log.d(TAG,"LIST: ${orderList.size}")
                                       }


//                                       order = Order(orderKey,email,mobile,price,remark,rfidNum,status,timestamp,detail)

//                                       Log.d(TAG,"Key is: $orderList")
                                       //Log.d(TAG,"Value is: $value")
                                   }
                                }

                            }
                        }
                    }
                }

                //order_list.adapter = OrderAdapter(orderList, this)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
        // [END basic_query_value_listener]

        //Log.d(TAG,"LIST Count ===: ${orderList.size}")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Creates a vertical Layout Manager
        order_list.layoutManager = LinearLayoutManager(this)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
        order_list.layoutManager = GridLayoutManager(this, 1)

        // Access the RecyclerView Adapter and load the data into it
        order_list.adapter = OrderAdapter(orderList, this)



//        order_list.layoutManager = LinearLayoutManager(this)
//        order_list.hasFixedSize()
//        order_list.adapter = OrderAdapter(orderList, this)
    }


//    fun basicReadWrite() {
//        // [START write_message]
//        // Write a message to the database
//        myRef.setValue("Hello, World!")
//        // [END write_message]
//
//        // [START read_message]
//        // Read from the database
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                val value = dataSnapshot.getValue(String::class.java)
//                Log.d(TAG, "Value is: $value")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException())
//            }
//        })
//        // [END read_message]
//    }

    fun addOrder(item: Order){
        orderList.add(item)
        Log.d(TAG,"Created ORDER: $item")

                                           Log.d(TAG,"LIST: ${orderList.size}")
        order_list.adapter = OrderAdapter(orderList, this)
        return
    }




}

interface JSONConvertable {
    fun toJSON(): String = Gson().toJson(this)
}

inline fun <reified T: JSONConvertable> String.toObject(): T = Gson().fromJson(this, T::class.java)

