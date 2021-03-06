package jp.techacademy.yuuya.nagafuchi.apiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(),FragmentCallback {
    private val viewPagerAdapter by lazy{ViewPagerAdapter(this)}
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("test","onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     //   ViewPager2の初期化
        viewPager2.apply {
            adapter = viewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = viewPagerAdapter.itemCount
        }

        TabLayoutMediator(tabLayout,viewPager2){
            tab,position ->
            tab.setText(viewPagerAdapter.titleIds[position])
        }.attach()


    }

    override fun onRestart() {
        Log.d("test","onRestart")
        super.onRestart()
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITTION_FAVORITE] as FavoriteFragment).updateData()
    }


    override fun onClickItem(shop: Shop) {
        Log.d("test","新着一覧Itemがクリックされました。")
        WebViewActivity.start(this, shop)
    }

    override fun onClickFavorite(favoriteShop: FavoriteShop) {
        Log.d("test","お気に入り済み一覧Itemがクリックされました。")
        WebViewActivity.startFavorite(this,favoriteShop)
    }

    override fun onAddFavorite(shop: Shop) {
        //Favoriteに追加するときのメソッド
        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            imageUrl = shop.logoImage
            url = if(shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        })
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITTION_FAVORITE] as FavoriteFragment).updateData()
    }

    override fun onDeleteFavorite(id: String) {
        //Favoriteから削除する時のメソッド
        showConfirmDeleteFavoriteDialog(id)
    }
    private fun showConfirmDeleteFavoriteDialog(id:String){
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok){_,_ ->
           deleteFavorite(id)
            }
            .setNegativeButton(android.R.string.cancel){_,_ ->}
            .create()
            .show()
    }
    private fun deleteFavorite(id:String){
        FavoriteShop.delete(id)
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITTION_FAVORITE] as FavoriteFragment).updateData()
    }
companion object{
    private const val VIEW_PAGER_POSITION_API = 0
    private const val VIEW_PAGER_POSITTION_FAVORITE = 1
}
}