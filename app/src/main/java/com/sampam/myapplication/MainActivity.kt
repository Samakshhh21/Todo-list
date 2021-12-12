package com.sampam.myapplication

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    val list = arrayListOf<TodoModel>()
    var adapter = todoadapter(list)
    val db by lazy {
        tododb.getdatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(materialToolbar)
        fab.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }

        todorv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
        db.tododao().get().observe(this,
            Observer {
                if (!it.isNullOrEmpty()) {
                    list.clear()
                    list.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            })


    }
    fun initswipes() {
        val simpleitemtouch = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    db.tododao().deletetask(adapter.getItemId(position))
                } else if (direction == ItemTouchHelper.RIGHT) {
                    db.tododao().finishtask(adapter.getItemId(position))
                }
            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemview = viewHolder.itemView
                    val paint = Paint()
                    val icon: Bitmap
                    if (dX > 0) {

                        icon = BitmapFactory.decodeResource(
                            resources,
                            android.R.drawable.checkbox_on_background
                        )
                        paint.color = Color.parseColor("#339B35")
                        canvas.drawRect(
                            itemview.left.toFloat(), itemview.top.toFloat(),
                            itemview.left.toFloat() + dX, itemview.bottom.toFloat(), paint
                        )
                        canvas.drawBitmap(
                            icon,
                            itemview.left.toFloat(),
                            itemview.top.toFloat() + (itemview.bottom.toFloat() - itemview.top.toFloat() - icon.height.toFloat()) / 2,
                            paint
                        )
                    } else {
                        icon = BitmapFactory.decodeResource(
                            resources,
                            android.R.drawable.ic_menu_delete
                        )
                        paint.color = Color.parseColor("#D32F2F")
                        canvas.drawRect(
                            itemview.right.toFloat() + dX, itemview.top.toFloat(),
                            itemview.right.toFloat(), itemview.bottom.toFloat(), paint
                        )
                        canvas.drawBitmap(
                            icon,
                            itemview.right.toFloat() - icon.width,
                            itemview.top.toFloat() + (itemview.bottom.toFloat() - itemview.top.toFloat() - icon.height.toFloat()) / 2,
                            paint
                        )
                    }
                    viewHolder.itemView.translationX = dX
                } else {
                    super.onChildDraw(
                        canvas,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

            }

        }
        val itemTouchHelper = ItemTouchHelper(simpleitemtouch)
        itemTouchHelper.attachToRecyclerView(todorv)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.history -> startActivity(Intent(this, HistoryActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

}