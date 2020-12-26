package jp.techacademy.yuuya.nagafuchi.apiapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FavoriteAdapter(private val context: Context):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    //お気に入り登録したShopを格納
    private val items = mutableListOf<FavoriteShop>()

    //お気に入り画面から削除するときのコールバック(ApiFragmentへ通知するメソッド）
    var onClickDeleteFavorite:((FavoriteShop) -> Unit)? = null

    // Itemを押したときのメソッド
    var onClickItem:((String) -> Unit)? = null


    /**
     * 更新用メソッド
     */
    fun refresh(list:List<FavoriteShop>){
        items.apply {
            clear()//itemsを空に
            addAll(list)//itemsをlistをすべて追加
        }
        notifyDataSetChanged()//recylerViewを再描画させる
    }
    /**
     * お気に入りが1件もない時に、「お気に入りはありません」を出すための処理
     */
    override fun getItemCount(): Int {
        return if(items.isEmpty()) 1 else items.size
    }

    /**
     *  onCreateViewHolderの台に引数はここできめる。
     *  //条件によってViewTypeを返すようにすると、一つのRecyclerViewで様々なViewがある物が作れる
     */
    override fun getItemViewType(position: Int): Int {
        return if(items.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    /**
     * お気に入り画面のViewHolderオブジェクトの生成
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            // ViewTypeがVIEW_TYPE_EMPTY(お気に入り登録　０の時）
            VIEW_TYPE_EMPTY -> EmptyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_favorite_empty,parent,false))
            else -> FavoriteItemViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_favorite,parent,false))
        }
    }

    /**
     * ViewHolderの結合用メソッド
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FavoriteItemViewHolder){
            updateFavoriteItemViewHolder(holder,position)
        }
    }
    //ViewHolder内のUI部品などをセットする
    private fun updateFavoriteItemViewHolder(holder: FavoriteItemViewHolder,position: Int){
        val data = items[position]
        holder.apply {
            rootView.apply {
                setBackgroundColor(ContextCompat.getColor(context,if(position % 2 == 0)
                    android.R.color.white else android.R.color.darker_gray))
                setOnClickListener { onClickItem?.invoke(data.url) }
            }
            nameTextView.text = data.name
            Picasso.get().load(data.imageUrl).into(imageView)
            favoriteImageView.setOnClickListener{
                onClickDeleteFavorite?.invoke(data)
                notifyItemChanged(position)
            }
        }
    }

    //お気に入りが登録されているときのリスト
    class FavoriteItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        val rootView: ConstraintLayout = view.findViewById(R.id.rootView)
        val nameTextView :TextView = view.findViewById(R.id.nameTextView)
        val imageView:ImageView = view.findViewById(R.id.imageView)
        val favoriteImageView:ImageView = view.findViewById(R.id.favoriteImageView)
    }
    //お気に入り登録がまだ行われていないとき
    class EmptyViewHolder(view:View):RecyclerView.ViewHolder(view)

    companion object{
        // Viewの種類を表現する定数、お気に入りのお店
        private const val VIEW_TYPE_ITEM = 0
        //Viewの種類を表現する定数、お気に入りが1件もないとき
        private const val VIEW_TYPE_EMPTY = 1
    }

}