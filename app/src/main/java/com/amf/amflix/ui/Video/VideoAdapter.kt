import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.retrofit.models.Video.Video
import com.bumptech.glide.Glide

class VideoAdapter(
    private val context: Context,
    private val videos: List<Video>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(video: Video)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video, itemClickListener)
    }

    override fun getItemCount(): Int = videos.size

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val videoThumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)
        private val videoTitle: TextView = itemView.findViewById(R.id.videoTitle)

        fun bind(video: Video, clickListener: OnItemClickListener) {
            videoTitle.text = video.name
            // Utiliza Glide para cargar la miniatura del video
            val thumbnailUrl = "https://img.youtube.com/vi/${video.key}/hqdefault.jpg"
            Glide.with(itemView.context)
                .load(thumbnailUrl)
                .into(videoThumbnail)

            itemView.setOnClickListener {
                clickListener.onItemClick(video)
            }
        }
    }
}

