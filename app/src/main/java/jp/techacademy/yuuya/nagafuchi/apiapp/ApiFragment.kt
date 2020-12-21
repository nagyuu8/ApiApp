package jp.techacademy.yuuya.nagafuchi.apiapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_api.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.lang.StringBuilder

class ApiFragment: Fragment() {
    private val apiAdapter by lazy { ApiAdapter(requireContext()) }
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_api,container,false)// fragment_api.xmlが反映されたViewを作成して、returnします
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初期化処理
        //RecyclerViewの初期化
        recyclerView.apply {
            adapter = apiAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        swipeRefreshLayout.setOnRefreshListener {
            updateData()
        }
        updateData()
    }
    private fun updateData(){
        val url = StringBuilder()
                .append(getString(R.string.base_url))
                .append("?key=").append(getString(R.string.api_key))//Apiを使うためのApiKey
                .append("&start=").append(1)//何件目のデータを取得するか
                .append("&count=").append(COUNT) //1回でCOUNT(20)件取得する
                .append("&keyword=").append(getString(R.string.api_keyword))
                .append("&format=json") //ここで利用しているAPIは戻り形をxml or jsonを選択
                .toString()
    Log.d("test",url.toString())
        val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        val request = Request.Builder()
                .url(url)
                .build()
        client.newCall(request).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                handler.post{
                    updateRecyclerView(listOf())
                }
            }

            override fun onResponse(call: Call, response: Response) {
                var list = listOf<Shop>()
                response.body?.string()?.also {
                    val apiResponse = Gson().fromJson(it,ApiResponse::class.java)
                    list = apiResponse.results.shop
                }
                handler.post {
                    updateRecyclerView(list)
                }
            }
        })
    }
    private fun updateRecyclerView(list: List<Shop>) {
        apiAdapter.refresh(list)
        swipeRefreshLayout.isRefreshing = false // SwipeRefreshLayoutのくるくるを消す
    }
    companion object {
        private const val COUNT = 20 // 1回のAPIで取得する件数
    }
}