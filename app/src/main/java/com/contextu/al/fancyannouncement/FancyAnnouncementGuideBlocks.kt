package com.contextu.al.fancyannouncement

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.contextu.al.R
import com.contextu.al.common.extensions.clickedOutside
import com.contextu.al.common.extensions.complete
import com.contextu.al.common.extensions.dismiss
import com.contextu.al.common.extensions.previous
import com.contextu.al.model.customguide.ContextualContainer


class FancyAnnouncementGuideBlocks(private val activity: Activity,private val contextualContainer:ContextualContainer): AlertDialog(activity) {

    private var closed_manually=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.fancy_announcement)

    }
    fun show(
             negativeButtonListener: View.OnClickListener,
             positiveButtonListener: View.OnClickListener,) {


        this.window?.setLayout((activity.resources.displayMetrics.widthPixels * 0.90).toInt(),
            (activity.resources.displayMetrics.heightPixels * 0.55).toInt())

        this.show()


        val title = contextualContainer.guidePayload.guide.titleText.text ?: ""
        val message = contextualContainer.guidePayload.guide.contentText.text ?: ""

        val buttons = contextualContainer.guidePayload.guide.buttons
        var prevButtonText = "back"
        var nextButtonText = "next"

        buttons.prevButton?.let { button ->
            prevButtonText = button.text ?: "back"
        }

        buttons.nextButton?.let { button ->
            nextButtonText = button.text ?: "next"
        }
        val negativeText = prevButtonText
        val positiveText = nextButtonText

        var imageURL: String? = null

        val images = contextualContainer.guidePayload.guide.images
        if (images.isNotEmpty())
        {
            imageURL = images[0].resource
        }

        val fancyAnnouncementImage = this.findViewById<ImageView>(R.id.announcementImage)
        fancyAnnouncementImage?.let {
            Glide.with(activity.baseContext).load(imageURL).into(fancyAnnouncementImage)
        }

        val fancyAnnouncementTitle = this.findViewById<TextView>(R.id.title)
        val closeView = this.findViewById<View>(R.id.pz_dismissImageView)
        closeView?.setOnClickListener {
            contextualContainer.dismiss()
            closed_manually=true
            this.dismiss()
        }
        fancyAnnouncementTitle?.text = title


        val fancyAnnouncementContent = this.findViewById<TextView>(R.id.content)
        fancyAnnouncementContent?.text = message

        val createAccountButton = this.findViewById<Button>(R.id.create_button)
        createAccountButton?.text = positiveText
        createAccountButton?.setOnClickListener{
            Log.d(
                    "FancyAnnouncementGuideBlocks",
                    "ON CLICK NEXT: "
            )
            closed_manually=true;
            positiveButtonListener.onClick(it)
            contextualContainer.complete()
        }

        val cancelButton = this.findViewById<Button>(R.id.cancel_button)
        cancelButton?.text = negativeText
        cancelButton?.setOnClickListener{
            closed_manually=true;
            Log.d(
                    "FancyAnnouncementGuideBlocks",
                    "ON BACK BUTTON : "
            )
            contextualContainer.dismiss()
            negativeButtonListener.onClick(it)
        }
        this.setOnDismissListener {
            Log.d(
                    "FancyAnnouncementGuideBlocks",
                    "ON Dismiss : "
            )
            if(!closed_manually)
            {
                contextualContainer.clickedOutside()

            }
        }


    }

}