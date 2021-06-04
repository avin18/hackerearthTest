package com.app.mycontactsapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mycontactsapp.Model.Contact
import com.app.mycontactsapp.Others.getInitialsFromName
import com.app.mycontactsapp.Others.getRandomColor
import com.app.mycontactsapp.R
import kotlinx.android.synthetic.main.contact_item.view.*

class ContactsAdapter(private val onItemClick: (Contact) -> Unit) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    private var contactList = arrayListOf<Contact>()

    fun updateList(contactList: ArrayList<Contact>){
        this.contactList = contactList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactsViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {

        val contact = contactList[position]

        holder.itemView.apply {

            contactName.text = contact.name
            contactMobileNo.text = contact.contactNumber
            contactImgCardView.setCardBackgroundColor(getRandomColor())
            nameInitials.text = contact.name.getInitialsFromName()

            setOnClickListener {
                onItemClick(contact)
            }
        }

    }

    override fun getItemCount() = contactList.size

    inner class ContactsViewHolder(view: View) : RecyclerView.ViewHolder(view)
}