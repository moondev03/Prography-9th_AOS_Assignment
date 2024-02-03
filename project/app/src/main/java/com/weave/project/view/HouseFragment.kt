package com.weave.project.view

import android.graphics.Rect
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.weave.project.R
import com.weave.project.databinding.FragmentHouseBinding
import com.weave.project.model.PhotoEntity
import com.weave.project.view.adapter.HouseBookMarkRvAdapter
import com.weave.project.view.base.BaseFragment
import com.weave.project.view.adapter.HouseRvAdapter
import com.weave.project.viewmodel.HouseViewModel

class HouseFragment : BaseFragment<FragmentHouseBinding>(R.layout.fragment_house) {
    private lateinit var viewModel: HouseViewModel
    private lateinit var bookMarkAdapter: HouseBookMarkRvAdapter
    private var rvState: Parcelable? = null
    private lateinit var photoAdapter: HouseRvAdapter

    override fun init() {
        viewModel = ViewModelProvider(this)[HouseViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.init()

        viewModel.bookMarkItems.observe(viewLifecycleOwner) { bookMarkList ->
            if (bookMarkList.isEmpty()) {
                binding.tvBookmark.visibility = View.GONE
                binding.rvBookmark.visibility = View.GONE
            } else {
                binding.rvBookmark.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                binding.rvBookmark.addItemDecoration(HorizontalSpaceItemDecoration())
                val temp = arrayListOf<PhotoEntity>()
                val list = bookMarkList.listIterator()
                while (list.hasNext()){
                    temp.add(list.next())
                }

                bookMarkAdapter = HouseBookMarkRvAdapter(temp)
                binding.rvBookmark.adapter = bookMarkAdapter
            }
        }

        val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)

        binding.rvLatest.layoutManager = layoutManager
        binding.rvLatest.addItemDecoration(VerticalSpaceItemDecoration())
        photoAdapter = HouseRvAdapter()
        binding.rvLatest.adapter = photoAdapter

        binding.rvLatest.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var loading = true
            private var previousTotal = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lm = recyclerView.layoutManager as? StaggeredGridLayoutManager ?: return

                if(dy > 0){
                    val visibleItemCount = lm.childCount
                    val totalItemCount = lm.itemCount
                    val firstVisibleItem = lm.findFirstVisibleItemPositions(IntArray(2))[0]

                    if (loading && totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }

                    val visibleThreshold = 10

                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        viewModel.getPhotos()

                        loading = true
                    }
                }
            }
        })

        viewModel.photoItems.observe(viewLifecycleOwner) { photoList ->
            rvState = binding.rvLatest.layoutManager?.onSaveInstanceState()!!
            photoAdapter.changeList(photoList)
            if(rvState != null){
                binding.rvLatest.layoutManager?.onRestoreInstanceState(rvState)
            }
        }
    }

    inner class HorizontalSpaceItemDecoration() :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = 20
            outRect.left = 20
        }
    }

    inner class VerticalSpaceItemDecoration() :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.top = 20
            outRect.bottom = 20
        }
    }
}
