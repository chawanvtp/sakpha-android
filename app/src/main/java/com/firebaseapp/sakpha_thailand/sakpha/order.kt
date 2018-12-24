package com.firebaseapp.sakpha_thailand.sakpha

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Order(
    var key: String? = "",
    var email: String? = "",
    var mobile: String? = "",
    var price: String? = "",
    var remark: String? = "",
    var rfidNum: String? = "",
    var status: String? = "",
    var timestamp: String? = "",
    //var detail: List<String?>
    var detail: String? = ""

    
)