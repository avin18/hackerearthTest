package com.app.mycontactsapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mycontactsapp.Adapter.ContactsAdapter
import com.app.mycontactsapp.Model.Contact
import com.app.mycontactsapp.Others.*
import com.app.mycontactsapp.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main_activity.*

class MainActivity : AppCompatActivity() {

    private lateinit var contactsAdapter: ContactsAdapter
    private var contactList = arrayListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initView()
    }

    private fun initView() {

        setRecyclerView()
        setClickListeners()

    }

    private fun setPermissionChecks() {
        PermissionsHelper.checkPermission(applicationContext,
            this@MainActivity,
            READ_CONTACTS_PERMISSION,{
            /**
             * On Permission Granted (Load Contacts)
             */
            contactList = ContactsHelper.fetchContacts(applicationContext,{
                /**
                 * Show Loader
                 */
                viewFlipper.displayedChild = 0
            }){
                /**
                 *  Hide Loader
                 */
                viewFlipper.displayedChild = 1
            }
            setContacts()

        }){ requestPermissionLauncher, neverShowAgainSet ->
            /**
             *  on Permission Denied
             */
            showPermissionReqdAlert(READ_CONTACTS_PERMISSION,{
                /**
                 * On Ok Btn Click
                 */
                if(!neverShowAgainSet)
                    requestPermissionLauncher?.launch(READ_CONTACTS_PERMISSION)
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

    override fun onStart() {
        super.onStart()
        setPermissionChecks()
    }

    private fun setRecyclerView() {

        contactsAdapter = ContactsAdapter{
            /**
             * On Contact Item Click
             */
            // Perform any operation if reqd.
        }

        contactsRecyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = contactsAdapter
        }

    }

    private fun setClickListeners() {
        addContactFAB.setOnClickListener(clickListener)
    }

    private fun setContacts() {
        contactsAdapter.updateList(contactList)
    }

    private var clickListener = View.OnClickListener {
        when(it.id){
            R.id.addContactFAB -> {
                // Add contact functionality
                startActivity(Intent(this, AddContactActivity::class.java))
            }
        }
    }

}
