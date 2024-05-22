package com.amf.amflix.ui.Video

import VideoAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amf.amflix.R
import com.amf.amflix.retrofit.models.Video.Video

class VideoDialogFragment(private val videos: List<Video>) : DialogFragment(), VideoAdapter.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_video_dialog, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewVideos)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = VideoAdapter(requireContext(), videos, this)

        return view
    }

    override fun onItemClick(video: Video) {
        // Reproduce el video seleccionado en un nuevo fragmento de diálogo
        val videoPlayerFragment = VideoPlayerDialogFragment(video.key)
        videoPlayerFragment.show(parentFragmentManager, "videoPlayerDialog")
    }
}