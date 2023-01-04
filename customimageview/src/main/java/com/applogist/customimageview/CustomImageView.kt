package com.applogist.customimageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class CustomImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var isProgress = true
    var placeHolder: Drawable? = null
    var url = ""
    var progressBar: ProgressBar? = null
    var ivImageView: ImageView? = null

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {

        inflate(context, R.layout.alf_custom_imageview, this)

        val customAttributesStyle =
            context.obtainStyledAttributes(attrs, R.styleable.CustomImageView, 0, 0)

        progressBar = findViewById(R.id.progressBar)
        ivImageView = findViewById(R.id.ivImageView)

        try {
            isProgress =
                customAttributesStyle.getBoolean(R.styleable.CustomImageView_isProgress, true)
            isProgress =
                customAttributesStyle.getBoolean(R.styleable.CustomImageView_isProgress, true)
            url = customAttributesStyle.getString(R.styleable.CustomImageView_loadUrl)!!
            placeHolder =
                customAttributesStyle.getDrawable(R.styleable.CustomImageView_placeHolder)!!
            progressBar?.isVisible = isProgress
            loadImage(url, placeHolder!!)
        } finally {
            customAttributesStyle.recycle()
        }

    }

    fun loadImage(url: String?, placeHolder: Drawable) {
        url?.let {
            Glide.with(this.context)
                .asBitmap()
                .load(url)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                )
                .error(placeHolder)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar?.isVisible = false
                        ivImageView?.isVisible = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar?.isVisible = false
                        ivImageView?.isVisible = true
                        ivImageView?.setImageBitmap(resource)
                        return true
                    }

                })
                .into(ivImageView!!)
        }
    }

}