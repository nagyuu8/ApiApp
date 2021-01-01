package jp.techacademy.yuuya.nagafuchi.apiapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val shop = intent.getSerializableExtra(KEY_SHOP) as? FavoriteShop
        if (shop != null){
            webView.loadUrl(shop.url)
            val isFavorite = (FavoriteShop.findBy(shop.id) != null)
            favButton.setTextColor(Color.YELLOW)
            if (isFavorite) favButton.text = "★" else favButton.text = "☆"
        }
        favButton.setOnClickListener {
            if(shop == null)return@setOnClickListener
            val isFavorite = (FavoriteShop.findBy(shop.id) != null)
            if(isFavorite){
                Log.d("test","消しました")
                favButton.text = "☆"
                FavoriteShop.delete(shop.id)
            }else{
                Log.d("test","加えました")
                favButton.text = "★"
                FavoriteShop.insert(shop)
            }



        }

    }
    companion object{
        private const val KEY_SHOP = "key_shop"
        fun start(activity: Activity,shop: Shop){
            Log.d("test","新着ページWebViewAcitivityが呼ばれました。"+shop.toString())
            val favoriteShop = FavoriteShop().apply {
                id = shop.id
                name = shop.name
                imageUrl = shop.logoImage
                url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
            }
            activity.startActivity(Intent(activity,WebViewActivity::class.java).putExtra(KEY_SHOP,favoriteShop))
        }
        fun startFavorite(activity: Activity,favoriteShop: FavoriteShop){
            Log.d("test","お気に入り済み一覧ページitemからのWebViewAcitivityが呼ばれました"+favoriteShop.toString())
            activity.startActivity(Intent(activity,WebViewActivity::class.java).putExtra(KEY_SHOP,favoriteShop))
        }
    }
}