package com.documentationplease

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

abstract class holder (view: View): RecyclerView.ViewHolder(view) {

    abstract fun ele(data: datas, click: (data: datas) -> Unit)
}


class adapter_general (var list: List<datas>, val view_type_id: Int, val click: (data: datas) -> Unit): RecyclerView.Adapter<holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val inflater = LayoutInflater.from(parent.context)

        return when (view_type_id) {
            0 -> country_holder(inflater.inflate(R.layout.recy_country, null))
            1 -> city_holder(inflater.inflate(R.layout.recy_city, null))
            else -> mis_holder(inflater.inflate(R.layout.recy_cons_misions, null))
        }

    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.ele(list[position], click)
    }

    override fun getItemCount(): Int = list.size

    fun update (new_list: List<datas>) {
        this.list = new_list

        notifyDataSetChanged()
    }

}

class country_holder (view: View): holder(view) {

    val global_click = view.findViewById<ConstraintLayout>(R.id.clik_global)
    val country = view.findViewById<TextView>(R.id.country)

    override fun ele(data: datas, click: (data: datas) -> Unit) {
        global_click.setOnClickListener {
            click(data)
        }

        country.text = (data as datas.country).country
    }


}

class city_holder (view: View): holder(view) {

    val city = view.findViewById<TextView>(R.id.city)

    override fun ele(data: datas, click: (data: datas) -> Unit) {
        city.text = (data as datas.ci_mi).ci_mi
    }

}

class mis_holder (view: View): holder(view) {

    val mision = view.findViewById<TextView>(R.id.mision)

    override fun ele(data: datas, click: (data: datas) -> Unit) {
        mision.text = (data as datas.ci_mi).ci_mi
    }
}