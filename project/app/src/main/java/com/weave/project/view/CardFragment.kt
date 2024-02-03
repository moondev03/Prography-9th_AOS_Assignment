package com.weave.project.view

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.weave.project.R
import com.weave.project.databinding.FragmentCardBinding
import com.weave.project.model.PhotoEntity
import com.weave.project.view.adapter.CardStackAdapter
import com.weave.project.view.base.BaseFragment
import com.weave.project.viewmodel.CardViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom

class CardFragment: BaseFragment<FragmentCardBinding>(R.layout.fragment_card) {
    private lateinit var cardStackAdapter: CardStackAdapter
    private lateinit var manager: CardStackLayoutManager
    private val viewModel by viewModels<CardViewModel>()

    override fun init() {
        viewModel.getRandomPhoto(3)
        setAdapter()

        viewModel.photoItems.observe(this){
            cardStackAdapter.changeList(viewModel.photoItems.value!!)
        }
    }

    private fun setAdapter(){
        manager = CardStackLayoutManager(requireActivity().baseContext,
            object : CardStackListener {
                override fun onCardDragging(direction: Direction?, ratio: Float) {}

                override fun onCardSwiped(direction: Direction?) {
                    if(direction == Direction.Right){
                        Log.e(TAG, "bookmark")
                    }

                    viewModel.getRandomPhoto(1)
                }

                override fun onCardRewound() {}

                override fun onCardCanceled() {}

                override fun onCardAppeared(view: View?, position: Int) {}

                override fun onCardDisappeared(view: View?, position: Int) {}
            })
        manager.setStackFrom(StackFrom.TopAndRight)

        cardStackAdapter = CardStackAdapter(viewModel.photoItems.value!!).apply {
            this.setItemClickListener(object: CardStackAdapter.OnItemClickListener{
                override fun onClick(v: View, pos: Int, data: PhotoEntity) {
                    binding.cardStackView.swipe()
                }
            })
        }
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = cardStackAdapter
    }
}