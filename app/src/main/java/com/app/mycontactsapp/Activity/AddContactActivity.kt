package com.app.mycontactsapp.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.mycontactsapp.Others.*
import com.app.mycontactsapp.R

import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.content_add_contact.*

class AddContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        setSupportActionBar(toolbar)
        initView()
    }

    private fun initView() {
        setClickListeners()
        setPermissionChecks()
    }

    private fun setPermissionChecks() {

        PermissionsHelper.checkPermission(applicationContext,
            this@AddContactActivity,
            WRITE_CONTACTS_PERMISSION,{
                /**
                 * On Permission Granted
                 */

            }){ requestPermissionLauncher, neverShowAgainSet ->
            /**
             *  on Permission Denied
             */
            showPermissionReqdAlert(WRITE_CONTACTS_PERMISSION,{
                /**
                 * On Ok Btn Click
                 */
                if(!neverShowAgainSet)
                    requestPermissionLauncher?.launch(WRITE_CONTACTS_PERMISSION)
                else
                    showGivePermissionsFromSettingAlert{
                        onBackPressed()
                    }
            }){
                /**
                 * On No Thanks Btn Click
                 */
                onBackPressed()
            }
        }

    }

    private fun setClickListeners() {
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
        addBtn.setOnClickListener(clickListener)
    }

    private val clickListener = View.OnClickListener {
        when(it.id){
            R.id.addBtn -> {
                addContact()
            }
        }
    }

    private fun addContact(){

        val name = contactName.text.toString()
        val phone = contactMobileNo.text.toString()

        val validation = performValidations(name, phone)

        if(validation is Boolean){
            val success = ContactsHelper.addContact(applicationContext
                ,name,phone,{
                    /**
                     * Show Loader
                     */
                    viewFlipper.displayedChild = 1
                },{
                    /**
                     * Hide Loader
                     */
                    viewFlipper.displayedChild = 1
                })

            if(success){
                getString(R.string.add_contact_success).makeToast(applicationContext)
                onBackPressed()
            }else{
                getString(R.string.error).makeToast(applicationContext)
            }
        }else
            (validation as String).makeToast(applicationContext)

    }

    private fun performValidations(name: String, phone: String): Any{
        // As it is contact name and id didn't add more validations coz it allows to take any input in the default contacts app
        return when {
            name.isEmpty() -> getString(R.string.enter_name)
            phone.isEmpty() -> getString(R.string.enter_contact_number)
            else -> true
        }
    }

}
