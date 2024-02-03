package com.weave.project.view

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.weave.project.R
import com.weave.project.data.local.BookMarkDatabase
import com.weave.project.databinding.DialogDetailBinding
import com.weave.project.model.BookMarkEntity
import com.weave.project.viewmodel.DetailDialogViewModel
import com.weave.project.viewmodel.HouseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailDialog(private val id: String, private val vm: HouseViewModel?) : DialogFragment() {
    companion object {
        private var instance: DetailDialog? = null

        fun getInstance(id: String, vm: HouseViewModel?): DetailDialog {
            return instance ?: synchronized(this) {
                instance ?: DetailDialog(id, vm).also { instance = it }
            }
        }
    }

    private var _binding: DialogDetailBinding? = null
    private val binding get() = _binding!!

    private var db: BookMarkDatabase? = null
    private val viewModel by viewModels<DetailDialogViewModel>()

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        setIsBookMark()

        binding.ibCancel.setOnClickListener {
            dismiss()
        }

        binding.ibBookmark.setOnClickListener {
            clickBookMark()
        }

        binding.ibDownload.setOnClickListener {
            if(checkStoragePermission()){
                useDownloadManager()
            } else {
                requestStoragePermission()
            }
        }

        viewModel.url.observe(this){
            Glide.with(binding.ivDetailPhoto)
                .load(it)
                .transform(RoundedCorners(15*4))
                .into(binding.ivDetailPhoto)
        }

        viewModel.setPhotoData(id)

        return binding.root
    }

    // 처음 로드 시 Id를 비교해 북마크 여부를 표시
    private fun setIsBookMark(){
        db = BookMarkDatabase.getInstance(requireContext())

        CoroutineScope(Dispatchers.IO).launch {
            val isBookMark = db!!.bookMarkDao().getBookMark(id)

            launch(Dispatchers.Main) {
                if(isBookMark != 0){
                    viewModel.setBookMark(true)
                } else {
                    viewModel.setBookMark(false)
                }
            }
        }
    }


    // 북마크 아이콘 클릭 시 북마크 여부를 검사
    // DB에 해당 아이템이 없을 시 추가, 있다면 제거
    private fun clickBookMark(){
        CoroutineScope(Dispatchers.Main).launch {
            val temp: Deferred<Boolean> = async(Dispatchers.IO){
                val isBookMarked = db!!.bookMarkDao().getBookMark(id)
                if(isBookMarked != 0){
                    db!!.bookMarkDao().deleteBookMark(id)
                    false
                } else {
                    db!!.bookMarkDao().saveBookMark(BookMarkEntity(id, viewModel.photoData.value!!.url))
                    true
                }
            }

            val result = temp.await()
            if(result){
                Toast.makeText(requireContext(), "북마크 추가", Toast.LENGTH_SHORT).show()
                vm?.setAddRefresh(BookMarkEntity(
                    id = viewModel.photoData.value!!.id,
                    url = viewModel.photoData.value!!.url
                ))
            } else {
                Toast.makeText(requireContext(), "북마크 삭제", Toast.LENGTH_SHORT).show()
                vm?.setRemoveRefresh(viewModel.photoData.value!!.id)
            }

            viewModel.setBookMark(result)
        }

    }

    // 사진 다운로드를 위한 권한 여부 체크
    private fun checkStoragePermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    // 최초 또는 재요청 기능을 하는 함수
    // 권한을 거부한 기록이 있다면 다이얼로그를 띄워 설정에서 수동 변경 유도
    private fun requestStoragePermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (shouldShowRequestPermissionRationale(permission[0])) {
            showPermissionRationale()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permission, 100)
        }
    }

    // 최초 요청 시 뜨는 권한 요청 창은 재요청 시 띄워지지 않기 때문에
    // 설정으로 유도시켜 권한 수정 요청
    private fun showPermissionRationale() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setMessage("설정에서 수동으로 변경 필요")
        alertDialog.setPositiveButton("확인") { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        alertDialog.setNegativeButton("취소") { _, _ ->
            dismiss()
        }
        alertDialog.show()
    }

    // DownloadManager를 사용해 이미지 다운로드
    private fun useDownloadManager(){
        val currentTimeMillis = System.currentTimeMillis()
        val fileName = currentTimeMillis.toString()

        val request = DownloadManager.Request(Uri.parse(viewModel.url.value))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)

        val downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(requireContext(), "다운로드 완료", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        Log.i("dialog", "destroy")
    }
}