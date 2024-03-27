package com.contextu.al.fancyannouncement

import android.app.Activity
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.contextu.al.R


class FancyAnnouncementGuideBlocks(private val activity: Activity): AlertDialog(activity) {

    fun show(title: String,
             content: String,
             negativeText: String,
             negativeButtonListener: View.OnClickListener,
             positiveText: String,
             positiveButtonListener: View.OnClickListener,
             imageUrl: String) {

        this.setContentView(R.layout.fancy_announcement)
        this.window?.setLayout((activity.resources.displayMetrics.widthPixels * 0.90).toInt(),
            (activity.resources.displayMetrics.heightPixels * 0.55).toInt())
        this.show()

        val fancyAnnouncementImage = this.findViewById<ImageView>(R.id.announcementImage)
        fancyAnnouncementImage?.let {
            Glide.with(activity.baseContext).load(imageUrl).into(fancyAnnouncementImage)
        }

        val fancyAnnouncementTitle = this.findViewById<TextView>(R.id.title)
        fancyAnnouncementTitle?.text = title

        val fancyAnnouncementContent = this.findViewById<TextView>(R.id.content)
        fancyAnnouncementContent?.text = content

        val createAccountButton = this.findViewById<Button>(R.id.create_button)
        createAccountButton?.text = positiveText
        createAccountButton?.setOnClickListener(positiveButtonListener)

        val cancelButton = this.findViewById<Button>(R.id.cancel_button)
        cancelButton?.text = negativeText
        cancelButton?.setOnClickListener(negativeButtonListener)
    }

}