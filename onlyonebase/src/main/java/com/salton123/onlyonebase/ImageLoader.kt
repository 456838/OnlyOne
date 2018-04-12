package com.salton123.onlyonebase

import android.widget.ImageView
import com.shuyu.gsyimageloader.GSYImageLoaderManager
import com.shuyu.gsyimageloader.GSYLoadOption

/**
 * User: newSalton@outlook.com
 * Date: 2018/4/12 15:57
 * ModifyTime: 15:57
 * Description:
 */
class ImageLoader {
    companion object {
        fun display(imageView: ImageView, url: String) {
            val loadOption = GSYLoadOption()
                    .setDefaultImg(R.drawable.salton_load_pic)
                    .setErrorImg(R.drawable.salton_load_pic_failed)
                    .setUri(url)
            GSYImageLoaderManager.sInstance.imageLoader().loadImage(loadOption, imageView, null)
        }

        fun displayCircle(imageView: ImageView, url: String) {
            val loadOption = GSYLoadOption()
                    .setDefaultImg(R.drawable.salton_load_pic)
                    .setErrorImg(R.drawable.salton_load_pic_failed)
                    .setCircle(true)
                    .setUri(url)
            GSYImageLoaderManager.sInstance.imageLoader().loadImage(loadOption, imageView, null)
        }
    }

}
