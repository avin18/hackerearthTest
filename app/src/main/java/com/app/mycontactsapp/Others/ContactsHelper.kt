package com.app.mycontactsapp.Others

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.app.mycontactsapp.Model.Contact
import java.lang.Exception

object ContactsHelper{

    private val TAG = "ContactsHelper"

    private val PROJECTION = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    fun fetchContacts(context: Context,
                      showLoader: () -> Unit = {},
                      hideLoader: () -> Unit = {}): ArrayList<Contact>{

        showLoader()
        val contacts = arrayListOf<Contact>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE NOCASE ASC"
        )

        if(cursor != null){
            try{
                val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                var id = ""
                var name = ""
                var phone = ""
                while (cursor.moveToNext()){
                    id = cursor.getString(idIndex)
                    name = cursor.getString(nameIndex)
                    phone = cursor.getString(phoneNumberIndex)
                    val contact = Contact(id,name, phone)
                    contacts.add(contact)
                }
            }catch (e: Exception){
                Log.d(TAG, e.localizedMessage ?: "")
            }

        }
        hideLoader()
        return contacts
    }

    fun addContact(context: Context,
                   contactName: String,
                   contactNumber: String,
                   showLoader: () -> Unit = {},
                   hideLoader: () -> Unit = {}): Boolean{

        showLoader()
        var success = false
        val contactDetails = ArrayList<ContentProviderOperation>()

        contactDetails.apply {
            add(
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build()
            )
            add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contactName)
                    .build()
            )
            add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build()
            )

            try {
                val results = context.contentResolver.applyBatch(ContactsContract.AUTHORITY, contactDetails)
                success = true
            }catch (e: Exception){
                success = false
                Log.d(TAG, e.localizedMessage ?: "")
            }
            hideLoader()
            return success
        }


    }

}