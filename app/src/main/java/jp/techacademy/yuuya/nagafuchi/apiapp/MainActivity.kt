package jp.techacademy.yuuya.nagafuchi.apiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator



class MainActivity : AppCompatActivity() {


    private val viewPagerAdapter by lazy{ViewPagerAdapter(this)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ViewPager2の初期化
//        R.id.viewPager2.apply {
//            var adapter = viewPagerAdapter
//            var orientation = ViewPager2.ORIENTATION_HORIZONTAL
//            var offscreenPageLimit = viewPagerAdapter.itemCount
//        }

//        TabLayoutMediator(tabLayout,viewPager2){
//            tab,position ->
//            tab.setText(viewPagerAdapter.titleIds[position])
//        }.attach()


    }

}