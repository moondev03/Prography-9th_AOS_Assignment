package com.weave.project.view

import android.graphics.Rect
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var photoAdapter1: HouseRvAdapter
    private lateinit var photoAdapter2: HouseRvAdapter

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

        viewModel.photoItems.observe(viewLifecycleOwner) { photoList ->
            val halfSize = photoList.size / 2
            val firstHalf = photoList.subList(0, halfSize)
            val secondHalf = photoList.subList(halfSize, photoList.size)

            photoAdapter1.rangeInsert(photoAdapter1.itemCount+1, firstHalf.size, firstHalf)
            photoAdapter2.rangeInsert(photoAdapter2.itemCount+1, secondHalf.size, secondHalf)
        }

        val layoutManager1 = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val layoutManager2 = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.rvLatest.layoutManager = layoutManager1
        binding.rvLatest.addItemDecoration(VerticalSpaceItemDecoration())
        photoAdapter1 = HouseRvAdapter()
        binding.rvLatest.adapter = photoAdapter1

        binding.rvLatest2.layoutManager = layoutManager2
        binding.rvLatest2.addItemDecoration(VerticalSpaceItemDecoration())
        photoAdapter2 = HouseRvAdapter()
        binding.rvLatest2.adapter = photoAdapter2

        binding.rvLatest.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var loading = true
            private var previousTotal = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.rvLatest2.scrollBy(dx/3, dy/3)

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

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
        })

        binding.rvLatest2.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.rvLatest.scrollBy(dx/3, dy/3)
            }
        })
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
