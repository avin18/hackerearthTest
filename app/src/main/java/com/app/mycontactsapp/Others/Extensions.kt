package com.app.mycontactsapp.Others

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.widget.Toast
import com.app.mycontactsapp.R
import java.util.*

fun String.getInitialsFromName(): String{
    val initial = this.firstOrNull()
    if(initial != null){
        return initial.toUpperCase().toString()
    }
    return ""
}

fun getRandomColor(): Int {
    val random = Random()
    return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
}

fun Context.showPermissionReqdAlert(
    permission: String,
    onPositiveBtnClick: () -> Unit = {},
    onNegativeBtnClick: () -> Unit = {}
){
    this.showAlert(
        title = getString(R.string.permission_reqd),
        message = if(permission == READ_CONTACTS_PERMISSION) getString(R.string.permission_reqd_read_msg)
        else getString(R.string.permission_reqd_write_msg),
        positiveBtnText = getString(R.string.ok),
        negativeBtnText = getString(R.string.no_thnx),
        onPositiveBtnClick = {
            onPositiveBtnClick()
        },
        onNegativeBtnClick = {
            onNegativeBtnClick()
        }
    )
}

fun Context.showGivePermissionsFromSettingAlert(
    onPositiveBtnClick: () -> Unit = {}
){
    this.showAlert(
        title = getString(R.string.permission_reqd),
        message = getString(R.string.permission_reqd_settings_msg),
        showNegativeBtn = false,
        positiveBtnText = getString(R.string.ok),
        onPositiveBtnClick = {
            onPositiveBtnClick()
        }
    )
}

fun Context.showAlert(showTitle: Boolean = true, title: String = "", message: String,
                                  showNegativeBtn: Boolean = true, positiveBtnText: String = getString(R.string.yes),
                                  negativeBtnText: String = getString(R.string.no),
                                  onPositiveBtnClick: () -> Unit = {}, onNegativeBtnClick: () -> Unit = {}){
    val builder = AlertDialog.Builder(this)

    if(showTitle)
        builder.setTitle(title)

    builder.setMessage(message)

    builder.setPositiveButton(positiveBtnText){dialogInterface, which ->
        onPositiveBtnClick()
        dialogInterface.dismiss()
    }
    if(showNegativeBtn)
        builder.setNegativeButton(negativeBtnText){dialogInterface, which ->
            onNegativeBtnClick()
            dialogInterface.dismiss()
        }
    val alertDialog: AlertDialog = builder.create()

    alertDialog.setCancelable(false)
    alertDialog.show()
}

fun String.makeToast(context: Context, moreTime: Boolean = false)
        = Toast.makeText(context, this, if(moreTime) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()