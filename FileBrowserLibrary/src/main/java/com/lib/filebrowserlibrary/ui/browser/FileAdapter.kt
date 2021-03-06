package com.lib.filebrowserlibrary.ui.browser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lib.filebrowserlibrary.R
import com.lib.filebrowserlibrary.utils.CommonUtils
import com.lib.filebrowserlibrary.utils.ScreenUtils
import java.io.File

open class FileAdapter (var listener : FileListener) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    var listFile = ArrayList<File>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_lib_item_file_browser,parent,false),listener)
    }

    override fun getItemCount() = listFile.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bindViewHolder(listFile[position])
    }

    interface FileListener {
        fun isContentItem(file: File):Boolean
        fun removeListItem(file: File)
        fun setListItem(file: File)
        fun isSingleImage():Boolean
        fun openFolder(file : File)
        fun addFileAlbums(file: File)
    }

    fun setList(list : List<File>){
        if (list.isEmpty())
            return
        listFile.clear()
        listFile.addAll(list)
        notifyDataSetChanged()
    }

    inner class FileViewHolder (itemView : View ,var listener : FileListener) : RecyclerView.ViewHolder(itemView),
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

        private val ivPdf = itemView.findViewById<ImageView>(R.id.LibItemFileBrowser_ivFile)
        private val tvNamePdf = itemView.findViewById<TextView>(R.id.LibItemFileBrowser_tvNameFile)
        private val tvSizePdf = itemView.findViewById<TextView>(R.id.LibItemFileBrowser_tvSizeFile)
        private val cbImage = itemView.findViewById<CheckBox>(R.id.LibItemFileBrowser_cbImage)
        private val tvFileInfo = itemView.findViewById<RelativeLayout>(R.id.LibItemFileBrowser_tvFileInfo)
        private var isFolder : Boolean = false
        private var isCheck = false
        private var ready = false

        fun bindViewHolder(file:File){
            tvFileInfo.setOnClickListener(this)
            tvSizePdf.visibility = View.GONE
            if (!listener.isSingleImage()) {
                ready = false
                cbImage.setOnCheckedChangeListener(this)
                isCheck = listener.isContentItem(file)
                cbImage.isChecked = isCheck
            }
            cbImage.visibility = if (listener.isSingleImage()) View.GONE else View.VISIBLE

            if(file.name.toLowerCase().endsWith(".jpg") || file.name.toLowerCase().endsWith(".png") || file.name.toLowerCase().endsWith(".jpeg")){
                Glide.with(ivPdf.context)
                    .load(file)
                    .override(ScreenUtils.convertDpToPx(ivPdf.context,68).toInt())
                    .into(ivPdf)
                tvSizePdf.visibility = View.VISIBLE
                tvSizePdf.text = CommonUtils.sizeFile(file.length())
                isFolder = false
            }else if (file.name.toLowerCase().endsWith(".pdf") ){
                Glide.with(ivPdf.context)
                    .load(R.drawable.ic_pdf)
                    .override(ScreenUtils.convertDpToPx(ivPdf.context,68).toInt())
                    .into(ivPdf)
                tvSizePdf.visibility = View.VISIBLE
                tvSizePdf.text = CommonUtils.sizeFile(file.length())
                isFolder = false
            }else{
                Glide.with(ivPdf.context)
                    .load(R.drawable.ic_folder_white)
                    .override(ScreenUtils.convertDpToPx(ivPdf.context,68).toInt())
                    .into(ivPdf)
                isFolder = true
                cbImage.visibility = View.GONE
            }
            tvNamePdf.text = file.name
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            when(buttonView?.id){
                R.id.LibItemFileBrowser_cbImage->{
                    if (isChecked){
                        if (!this.isCheck)
                            listener.setListItem(listFile[adapterPosition])
                        ready=true
                    }else{
                        if(ready)
                            listener.removeListItem(listFile[adapterPosition])
                        else
                            ready=true
                    }
                }
            }
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.LibItemFileBrowser_tvFileInfo->{
                    if(isFolder){
                        listener.openFolder(listFile[adapterPosition])
                    }else if (listener.isSingleImage()){
                        listener.addFileAlbums(listFile[adapterPosition])
                    }
                }
            }
        }
    }
}