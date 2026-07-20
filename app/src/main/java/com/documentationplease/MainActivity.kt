package com.documentationplease

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    private lateinit var city_list: List<datas.ci_mi>
    private var global_data_day: global_data? = null
    private lateinit var actu_misions: List<datas.ci_mi>

    private lateinit var city_adapter: adapter_general
    private lateinit var norms_adapter: adapter_general
    private lateinit var data: datas.country

    enum class skip_mode { text, edit }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val main = findViewById<ConstraintLayout>(R.id.main)

        val next_day = findViewById<ConstraintLayout>(R.id.next_day)
        val day_expre = findViewById<TextView>(R.id.day_expre)
        val name_expre = findViewById<TextView>(R.id.name)
        val day_search = findViewById<SearchView>(R.id.search)

        val recy_country = findViewById<RecyclerView>(R.id.recy_count)

        val global_mision = findViewById<TextView>(R.id.global_misio)
        val passport = findViewById<ShapeableImageView>(R.id.pass_image)
        val seal_1 = findViewById<ShapeableImageView>(R.id.seal_1)
        val seal_2 = findViewById<ShapeableImageView>(R.id.seal_2)

        val back_day_info = findViewById<ConstraintLayout>(R.id.back_day_info)
        val day_mision = findViewById<TextView>(R.id.day_misio)

        val recy_norms = findViewById<RecyclerView>(R.id.cons_misions)

        val recy_city = findViewById<RecyclerView>(R.id.recy_city)
        val back_city = findViewById<ConstraintLayout>(R.id.back_city)

        val country_actu = findViewById<TextView>(R.id.country_actu)

        val idea = findViewById<ShapeableImageView>(R.id.idea)
        val note_but = findViewById<ShapeableImageView>(R.id.new_note)


        val db = db(this)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        fun db_values (name: String, day: String, note: String): ContentValues {

            return ContentValues().apply {
                put("name", name)
                put("day", day)
                put("note", note)
            }

        }

        fun var_init () {
            day_expre.text = db.catch(arrayOf("day"))

            data = country_info[pref.getInt("l_c", 0)]
            global_data_day = global_info.get(day_expre.text.toString().toInt())

            if (data.cities != null) {
                city_list = country_info[pref.getInt("l_c", 0)].cities!!
            } else {
                city_list = listOf()
            }
            actu_misions = acu_misions.take(global_data_day!!.take_misions)
        }

        fun country_values_init () {

            var_init()

            passport.setImageResource(data.passport)
            global_mision.text = global_data_day!!.day_mision
            country_actu.text = data.country

            if (data.seal_1 != null) {
                seal_1.visibility = View.VISIBLE
                seal_2.visibility = View.VISIBLE

                seal_1.setImageResource(data.seal_1!!)
                seal_2.setImageResource(data.seal_2!!)
            } else {
                seal_1.visibility = View.INVISIBLE
                seal_2.visibility = View.INVISIBLE
            }

            val mision_ex = global_data_day!!.mision_extra

            if ( mision_ex != null) {
                back_day_info.visibility = View.VISIBLE

                if (data.country == mision_ex.country) {
                    day_mision.text = mision_ex.mision
                } else {
                    if (mision_ex.second_conutry != null && data.country == mision_ex.second_conutry) {
                        day_mision.text = mision_ex.second_mision
                    } else if (mision_ex.rest_country != null) {
                        day_mision.text = mision_ex.rest_country
                    } else {
                        back_day_info.visibility = View.GONE
                    }
                }

            } else {
                back_day_info.visibility = View.GONE
            }

            if (data.cities != null) {
                back_city.visibility = View.VISIBLE
                city_adapter.update(data.cities!!)
                recy_city.layoutManager = GridLayoutManager(this, if (data.cities!!.size == 2) {2} else {3}, RecyclerView.HORIZONTAL, false)
            } else {
                back_city.visibility = View.INVISIBLE
            }


            city_adapter.update(city_list)
            norms_adapter.update(actu_misions)

        }

        fun init () {
            name_expre.text = db.catch(arrayOf("name"))

            var_init()

            norms_adapter = adapter_general(actu_misions, 2) {}
            city_adapter = adapter_general(city_list, 1) {}

            recy_city.adapter = city_adapter
            recy_norms.adapter = norms_adapter

            recy_country.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            recy_norms.layoutManager = GridLayoutManager(this, 3, RecyclerView.HORIZONTAL, false)

            recy_country.adapter = adapter_general(country_info, 0) { data ->
                val country_unic = country_info.filter { it.country == (data as datas.country).country }[0]

                if (country_unic != country_info[pref.getInt("l_c", 0)]) {
                    pref.edit().putInt("l_c", country_info.indexOf(country_unic)).commit()

                    country_values_init()
                }
            }
            recy_country.scrollToPosition(pref.getInt("l_c", 0))

            country_values_init()

        }

        if (!pref.getBoolean("init", false)) {

            main.visibility = View.INVISIBLE


            val view = LayoutInflater.from(this).inflate(R.layout.dialog_init_values, null)
            val dialog = Dialog(this).apply {
                setContentView(view)
                setCancelable(false)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            val name_input = view.findViewById<AppCompatEditText>(R.id.name)
            val day_input = view.findViewById<AppCompatEditText>(R.id.day)
            val sign_but = view.findViewById<ShapeableImageView>(R.id.sign_but)

            sign_but.setOnClickListener {
                if (name_input.text!!.isNotEmpty() && day_input.text!!.isNotEmpty() && day_input.text.toString().toInt() <= 31) {
                    db.add(db_values(name_input.text.toString(), day_input.text.toString(), ""))
                    main.visibility = View.VISIBLE

                    pref.edit().putBoolean("init", true).commit()
                    init()

                    dialog.dismiss()

                } else {
                    Toast.makeText(this, "A value is incorrect", Toast.LENGTH_SHORT).show()
                }
            }


            dialog.show()

        } else {
            init()
        }

        day_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String?): Boolean {
                city_adapter.update(city_list.filter { Regex(".*$newText.*").matches(it.ci_mi!!) })
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean = false

        })

        next_day.setOnClickListener {
            val day_actu = if (day_expre.text.toString().toInt() <= 30) { (day_expre.text.toString().toInt() + 1).toString() } else { "31" }
            var mode = skip_mode.text

            val view = LayoutInflater.from(this).inflate(R.layout.next_day_dialog, null)
            val dialog = Dialog(this).apply {
                setContentView(view)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            val day_ani = view.findViewById<TextView>(R.id.day_ani)
            val edit_day_ani = view.findViewById<EditText>(R.id.edit_day_ani)

            val edit_day = view.findViewById<ConstraintLayout>(R.id.edit_day)
            val edit_icon = view.findViewById<ShapeableImageView>(R.id.edit_icon)

            val skip_day = view.findViewById<ConstraintLayout>(R.id.skip_day)

            edit_day_ani.addTextChangedListener {
                if (it.toString().isNotEmpty() && it.toString().toInt() > 31){
                    edit_day_ani.setText("31")
                    edit_day_ani.setSelection(edit_day_ani.text.length)
                    Toast.makeText(this, "The game only lasts 31 days", Toast.LENGTH_SHORT).show()
                }
            }

            edit_day_ani.visibility = View.INVISIBLE
            day_ani.text = day_actu

            edit_day.setOnClickListener {
                if (mode == skip_mode.text) {
                    day_ani.visibility = View.INVISIBLE
                    edit_day_ani.visibility = View.VISIBLE
                    mode = skip_mode.edit
                    edit_icon.setImageResource(R.drawable.back)
                } else {
                    day_ani.visibility = View.VISIBLE
                    edit_day_ani.visibility = View.INVISIBLE
                    mode = skip_mode.text
                    edit_icon.setImageResource(R.drawable.edit)
                }
            }


            val animation = AnimationUtils.loadAnimation(this, R.anim.rotate_360).apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        db.update(
                            ContentValues().apply {
                                put("day", day_ani.text.toString())
                            }
                        )
                        dialog.dismiss()
                        country_values_init()
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}

                    override fun onAnimationStart(animation: Animation?) {}

                })
            }

            skip_day.setOnClickListener {
                if (day_ani.text.toString().toInt() <= 31) {

                    if (mode == skip_mode.edit) {
                        edit_day_ani.visibility = View.INVISIBLE
                        day_ani.visibility = View.VISIBLE
                        day_ani.text = edit_day_ani.text.toString()
                    }

                    day_ani.startAnimation(animation)

                } else {
                    Toast.makeText(this, "The game only lasts 31 days", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.show()
        }


        note_but.setOnClickListener {
            startActivity(Intent(this, noteActivity::class.java))
        }

        idea.setOnClickListener {
            MaterialAlertDialogBuilder(this).apply {
                setMessage("Do you want to propose a new idea for \"Documentation, please\"?")

                setPositiveButton("Yeah") {_, _ ->
                    startActivity(Intent(Intent.ACTION_VIEW, "https://docs.google.com/forms/d/e/1FAIpQLSf4eTDvzU4-FRUgm7ttftAiw-nGkFEkI-E2wYZs5-QuKt_zPQ/viewform?usp=sharing&ouid=117192912503551591162".toUri()))
                }
                setNegativeButton("No, thanks") {_, _ ->}

            }.show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}