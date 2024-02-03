package com.weave.project.view

import android.graphics.Rect
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.weave.project.R
import com.weave.project.data.local.BookMarkDatabase
import com.weave.project.databinding.FragmentHouseBinding
import com.weave.project.model.BookMarkEntity
import com.weave.project.view.adapter.HouseBookMarkRvAdapter
import com.weave.project.view.base.BaseFragment
import com.weave.project.view.adapter.HouseRvAdapter
import com.weave.project.viewmodel.HouseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class HouseFragment : BaseFragment<FragmentHouseBinding>(R.layout.fragment_house) {
    private lateinit var viewModel: HouseViewModel
    private lateinit var bookMarkAdapter: HouseBookMarkRvAdapter
    private lateinit var photoAdapter: HouseRvAdapter
    private var rvState: Parcelable? = null
    private var db: BookMarkDatabase? = null
    private var bookMarkList: MutableList<BookMarkEntity> = mutableListOf()
    private var backPressedTime: Long = 0L

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                requireActivity().finishAffinity()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(requireContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun init() {
        viewModel = ViewModelProvider(this)[HouseViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        db = BookMarkDatabase.getInstance(requireContext())

        viewModel.getPhotos()
        setBookMark()
        setRecent()

        // 북마크 제거 시 업데이트를 위함
        viewModel.removeRefresh.observe(this){ id ->
            if(id.isNotEmpty()){
                val userToRemove = bookMarkList.find { it.id == id }
                bookMarkList.remove(userToRemove)
                bookMarkAdapter.changeList(bookMarkList)
                viewModel.setRemoveRefresh("")

                checkEmpty()
            }
        }

        // 북마크 추가 시 업데이트를 위함
        viewModel.addRefresh.observe(this){
            if(it.id.isNotEmpty()){
                bookMarkList.add(it)
                bookMarkAdapter.changeList(bookMarkList)
                viewModel.setAddRefresh(BookMarkEntity("", ""))

                checkEmpty()
            }
        }
    }

    // 북마크 세션 visibility
    private fun checkEmpty(){
        if (bookMarkList.isEmpty()) {
            binding.tvBookmark.visibility = View.GONE
            binding.rvBookmark.visibility = View.GONE
        } else {
            binding.tvBookmark.visibility = View.VISIBLE
            binding.rvBookmark.visibility = View.VISIBLE
        }
    }


    // 북마크 세션에 대한 세팅
    private fun setBookMark(){
        runBlocking(Dispatchers.IO){
            bookMarkList.addAll(db!!.bookMarkDao().getAll())
        }

        checkEmpty()

        binding.rvBookmark.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvBookmark.addItemDecoration(HorizontalSpaceItemDecoration())
        val temp = arrayListOf<BookMarkEntity>()
        val list = bookMarkList.listIterator()
        while (list.hasNext()){
            temp.add(list.next())
        }

        bookMarkAdapter = HouseBookMarkRvAdapter(temp).apply {
            this.setItemClickListener(object: HouseBookMarkRvAdapter.OnItemClickListener{
                override fun onClick(id: String) {
                    DetailDialog.getInstance(id, viewModel).show(requireActivity().supportFragmentManager, "")
                }
            })
        }
        binding.rvBookmark.adapter = bookMarkAdapter
    }


    // 최근 이미지에 대한 세팅
    // StaggeredGridLayoutManager를 사용함
    // 최하단 아이템으로 부터 visibleThreshold값 떨어진 아이템이 로드되면 새로운 getPhotos() 호출
    private fun setRecent(){
        val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)

        binding.rvRecent.layoutManager = layoutManager
        binding.rvRecent.addItemDecoration(VerticalSpaceItemDecoration())
        photoAdapter = HouseRvAdapter().apply {
            this.setItemClickListener(object: HouseRvAdapter.OnItemClickListener{
                override fun onClick(id: String) {
                    DetailDialog.getInstance(id, viewModel).show(requireActivity().supportFragmentManager, "")
                }
            })
        }
        binding.rvRecent.adapter = photoAdapter

        binding.rvRecent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var loading = true // 중복 요청 방지
            private var previousTotal = 0 // 이전 총 아이템 수

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lm = recyclerView.layoutManager as? StaggeredGridLayoutManager ?: return

                if(dy > 0){
                    val visibleItemCount = lm.childCount // 현재 보이는 아이템 수
                    val totalItemCount = lm.itemCount // 총 아이템 수
                    val firstVisibleItem = lm.findFirstVisibleItemPositions(IntArray(2))[0] // 첫번째로 보이는 아이템 위치

                    if (loading && totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }

                    val visibleThreshold = 3 // 남은 아이템 수 (해당 값 만큼 남으면 로드)

                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        viewModel.getPhotos()

                        loading = true
                    }
                }
            }
        })

        // photoItems가 변경됨을 감지하고 DiffUtil를 통해 RecyclerView 아이템 변경
        viewModel.photoItems.observe(viewLifecycleOwner) { photoList ->
            rvState = binding.rvRecent.layoutManager?.onSaveInstanceState()!!
            photoAdapter.changeList(photoList)
            if(rvState != null){
                binding.rvRecent.layoutManager?.onRestoreInstanceState(rvState)
            }
        }
    }


    inner class HorizontalSpaceItemDecoration :
        RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = 20
            outRect.left = 20
        }
    }

    inner class VerticalSpaceItemDecoration :
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
