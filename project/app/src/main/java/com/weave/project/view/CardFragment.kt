package com.weave.project.view

import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.weave.project.R
import com.weave.project.data.local.BookMarkDatabase
import com.weave.project.databinding.FragmentCardBinding
import com.weave.project.model.PhotoEntity
import com.weave.project.util.asBookMark
import com.weave.project.view.adapter.CardStackAdapter
import com.weave.project.view.base.BaseFragment
import com.weave.project.viewmodel.CardViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CardFragment: BaseFragment<FragmentCardBinding>(R.layout.fragment_card) {
    private lateinit var cardStackAdapter: CardStackAdapter
    private lateinit var manager: CardStackLayoutManager
    private val viewModel by viewModels<CardViewModel>()
    private var db: BookMarkDatabase? = null
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
        // 처음에만 3개 로드하고 스와이프 시마다 하나식 추가해 총 3개 유지되도록 함
        viewModel.getRandomPhoto(3)

        db = BookMarkDatabase.getInstance(requireContext())
        binding.lifecycleOwner = viewLifecycleOwner
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setAdapter()

        // PhotoItems의 변화를 감지하고 DiffUtil를 통해 아이템 추가하도록 함
        viewModel.photoItems.observe(this){
            cardStackAdapter.changeList(viewModel.photoItems.value!!)
        }
    }

    // CardStackView 라이브러리를 사용해 구현함
    // 좌로 스와이프 -> 다음 아이템
    // 우로 스와이프 -> 북마크 저장 후 다음 아이템
    private fun setAdapter(){
        manager = CardStackLayoutManager(requireActivity().baseContext,
            object : CardStackListener {
                override fun onCardDragging(direction: Direction?, ratio: Float) {}

                override fun onCardSwiped(direction: Direction?) {
                    if(direction == Direction.Right){
                        CoroutineScope(Dispatchers.IO).launch {
                            db!!.bookMarkDao().saveBookMark(viewModel.photoItems.value!![manager.topPosition-1].asBookMark())
                        }

                        Toast.makeText(requireContext(), "북마크 추가!", Toast.LENGTH_SHORT).show()
                    }

                    // 스와이프 마다 새로운 아이템을 추가 총 3개가 유지되도록 함
                    viewModel.getRandomPhoto(1)
                }

                override fun onCardRewound() {}

                override fun onCardCanceled() {}

                override fun onCardAppeared(view: View?, position: Int) {}

                override fun onCardDisappeared(view: View?, position: Int) {}
            })

        // 스택 쌓이는 형태 지정
        manager.setStackFrom(StackFrom.TopAndRight)

        // RecyclerView Adapter에 ItemClickListener 추가해서 Room DB에 사진 Id와 Url를 저장
        // 이후 자동 스와이프 진행
        cardStackAdapter = CardStackAdapter(requireActivity(), viewModel.photoItems.value!!).apply {
            this.setItemClickListener(object: CardStackAdapter.OnItemClickListener{
                override fun onClick(v: View, pos: Int, data: PhotoEntity) {

                    CoroutineScope(Dispatchers.IO).launch {
                        db!!.bookMarkDao().saveBookMark(data.asBookMark())
                    }

                    binding.cardStackView.swipe()
                }
            })
        }
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = cardStackAdapter
    }
}