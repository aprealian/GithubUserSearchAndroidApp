package com.cermati.test.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cermati.test.R
import com.cermati.test.di.Injection
import com.cermati.test.model.User
import com.cermati.test.viewmodel.UserViewModel
import com.cermati.test.viewmodel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var adapter:UserAdapter
    private lateinit var layoutManager:LinearLayoutManager
    private var isInit: Boolean = true
    private var isLastPage: Boolean = false

    companion object {
        const val TAG= "CONSOLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        initAdapter()
        initListener()
    }

    private fun initListener() {
        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // part of search suggestion
                adapter.clear()
                isLastPage = false

                val keyword = s.toString().trim()
                viewModel.field.search = keyword
                viewModel.field.page = 1
                viewModel.loadUsers()
            }
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) { //check for scroll down
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if (!isLastPage && viewModel.isViewLoading.value != null && viewModel.isViewLoading.value == false){
                            viewModel.field.page += 1
                            viewModel.loadUsers()
                        }
                    }
                }
            }
        })
    }

    private fun initAdapter() {
        adapter= UserAdapter(viewModel.users.value?: emptyList())
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter= adapter
    }

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this, ViewModelFactory(Injection.providerRepository(applicationContext))).get(
            UserViewModel::class.java)
        viewModel.users.observe(this,renderUsers)

        viewModel.isViewLoading.observe(this,isViewLoadingObserver)
        viewModel.onMessageError.observe(this,onMessageErrorObserver)
        viewModel.isEmptyList.observe(this,emptyListObserver)
    }


    //observers
    private val renderUsers= Observer<List<User>> {
        Log.v(TAG, "data updated $it")
        progressBar.visibility = View.GONE
        adapter.addAll(it)
        isEmptyList()
        isInit = false
    }

    private val isViewLoadingObserver= Observer<Boolean> {
        Log.v(TAG, "isViewLoading $it")
        val visibility=if(it)View.VISIBLE else View.GONE
        progressBar.visibility= visibility
    }

    private val onMessageErrorObserver= Observer<Any> {
        Log.v(TAG, "onMessageError $it")
        progressBar.visibility = View.GONE

        val message = it as String?
        if (message != null){
            Snackbar.make(findViewById(R.id.placeSnackBar), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private val emptyListObserver= Observer<Boolean> {
        Log.v(TAG, "emptyListObserver $it")
        progressBar.visibility = View.GONE
        isEmptyList()
        isLastPage = true
    }

    private fun isEmptyList(){

        if (isInit) return

        if (adapter.itemCount == 0)
            textEmpty.visibility = View.VISIBLE
        else
            textEmpty.visibility = View.GONE
    }

}
