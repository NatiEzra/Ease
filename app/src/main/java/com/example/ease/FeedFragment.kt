package com.example.ease

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ease.model.Model
import com.example.ease.model.Post
import java.text.SimpleDateFormat
import java.util.Locale

class PostsViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    var profileNameTextView: TextView? = null
    var postTextView: TextView? = null
    var imageProfile: ImageView? = null
    var dateTextView: TextView? = null



    init {
        profileNameTextView = itemView.findViewById(R.id.profileName)
        postTextView = itemView.findViewById(R.id.textPost)
        dateTextView = itemView.findViewById(R.id.postDate)

    }
    /*itemView.setOnClickListener{
            adapterPosition
        }
        itemView.findViewById<View>(R.id.student_row).setOnClickListener {
            val intent = Intent(itemView.context, StudentDetailsActivity::class.java).apply {
                putExtra("STUDENT_ID", student?.id)
                putExtra("STUDENT_NAME", student?.name)
                putExtra("STUDENT_ADDRESS", student?.address)
                putExtra("STUDENT_PHONE", student?.phone)
                putExtra("STUDENT_IS_CHECKED", student?.isChecked)
                putExtra("STUDENT_INDEX", adapterPosition)
            }
            itemView.context.startActivity(intent)
        }
*/


    fun bind(post: Post) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        profileNameTextView?.text = post.profileName
        postTextView?.text = post.textPost
        dateTextView?.text = dateFormat.format(post.date)
    }

}

class PostRecycleAdapter(private var posts : List<Post>?): RecyclerView.Adapter<PostsViewHolder>() {
    override fun getItemCount(): Int {
        return posts?.size ?: 0
    }
    fun set(_posts: List<Post>) {
        posts = _posts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val inflation=LayoutInflater.from(parent.context)
        val view = inflation.inflate(R.layout.post_row, parent, false)
        return PostsViewHolder(view);
    }


    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.bind(posts?.get(position) ?: return)
    }


}


class FeedFragment : Fragment() {
    //var adapter: PostRecycleAdapter? = null
    var adapter = PostRecycleAdapter(Model.shared.posts)
    var posts: MutableList<Post> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_feed, container, false)

        posts = Model.shared.posts
        val recyclerView: RecyclerView = view.findViewById(R.id.fragment_feed_recycler_view)
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        adapter = PostRecycleAdapter(posts)
        recyclerView.adapter = adapter
        getAllPosts()
        return view
    }

    override fun onResume() {
        super.onResume()
        getAllPosts()

    }

    private fun getAllPosts() {

        Model.shared.getPosts { fetchedPosts ->
            // Ensure posts are updated only after fetching data from the database
            posts.clear()
            posts.addAll(fetchedPosts)

            adapter?.set(posts)
            adapter?.notifyDataSetChanged()
        }
    }
}