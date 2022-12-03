package com.danda.danda.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.danda.danda.R


class CustomProfileAdapter(
    var context: Context,
    private var listButton: ArrayList<ListButton>
):BaseAdapter() {
    private class ViewHolder (row: View?){
        var txtMenu: TextView
        var txtImage: ImageView
        init {
            this.txtMenu = row?.findViewById(R.id.txt_edit_account) as TextView
            this.txtImage = row.findViewById(R.id.ic_edit) as ImageView
        }
    }

    override fun getCount(): Int {
        return listButton.size
    }

    override fun getItem(position: Int): Any {
        return listButton[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.account_button_list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder

        }
        val listButton: ListButton = getItem(position) as ListButton
        viewHolder.txtMenu.text = listButton.listButton
        viewHolder.txtImage.setImageResource(listButton.image)
        return view as View
    }
}