package com.contextu.al.appFieldEdit.ui

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.contextu.al.Contextual
import com.contextu.al.ContextualBase
import com.contextu.al.Player
import com.contextu.al.R
import com.contextu.al.appFieldEdit.interfaces.AppFieldValidationInterface
import com.contextu.al.appFieldEdit.model.AppFieldEditValidation
import com.contextu.al.appFieldEdit.utils.AppFieldStepHandler
import com.contextu.al.appFieldEdit.utils.AppFieldValidator
import com.contextu.al.customtip.TipAttachHelper
import com.contextu.al.model.Trigger
import com.contextu.al.model.customguide.ContextualContainer
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException

class AppFieldEditGB(val activity: Activity, val contextualContainer: ContextualContainer) :
    AppFieldValidationInterface
{

    companion object
    {
        val GB_KEY = "AppFieldEdit"
        @JvmStatic
        var mOverLays = ArrayList<AppFieldEditOverlay>()
    }

    private var tipAttachHelper: TipAttachHelper? = null
    private lateinit var mAnchor: View
    private var onDismiss: (() -> Unit)? = null
    private var onComplete: (() -> Unit)? = null
    private var widget: ContextualBase? = null
    private var nextButton: Button? = null
    private var appFieldStepHandler: AppFieldStepHandler? = null
    private var appFieldUiHandler: AppFieldUiHandler = AppFieldUiHandler()
    private  var feed:Trigger ? = null
    init
    {
        clear()
        widget = contextualContainer.guidePayload.guide
        feed = Contextual.state.player.feeds.realMode[widget!!.feedID]
            ?: Contextual.state.player.feeds.previewMode[widget!!.feedID]
        feed?.let { appFieldStepHandler = AppFieldStepHandler(it.feedID!!) }
        AppFieldValidator().init(this)
    }


    fun show(): AppFieldEditGB
    {
        if(getCurrentStep()==0)
        {
            AppFieldValidator().clear()
        }
        tipAttachHelper = TipAttachHelper(
                activity,
                widget!!
        )
        mAnchor = contextualContainer.operations.findTarget(widget!!) ?: run {
            Log.i(
                    this.javaClass.name,
                    "Anchor not found"
            )
            this.onDismiss?.invoke()
            return this
        }
//
//        rootView().viewTreeObserver.addOnScrollChangedListener {
//
//            clear()
//            if (appFieldUiHandler.isViewVisible(mAnchor).not())
//            {
//                showTargetTip()
//            }
//        }

        if (appFieldUiHandler.isViewVisible(mAnchor).not())
        {
            appFieldUiHandler.scrollToView(view = mAnchor, tipViewHeight = setupView(widget!!).measuredHeight,onScrollDone = {
                showTargetTip()
            })

        } else {
            showTargetTip()
        }

        return this
    }

    private fun showTargetTip()
    {
        val view = setupView(widget!!)
        drawOverLay(mAnchor,
                onClose = {
                    resetStep()
                    this.dismiss()
                    this.onDismiss?.invoke()
                })
        tipAttachHelper?.drawOverLay(false)?.showTooltip(
                anchor = mAnchor,
                tipContent = view,
                tipPointerColor = widget!!.baseView.backgroundColor,
        )
    }


    fun setOnDismissListener(onDismiss: () -> Unit): AppFieldEditGB
    {
        this.onDismiss = onDismiss
        return this
    }

    fun setOnCompletedListener(onComplete: () -> Unit): AppFieldEditGB
    {
        this.onComplete = onComplete
        return this
    }


    private fun setupView(
        widget: ContextualBase,
    ): View
    {
        val view = AppFieldUiHandler().getView(
                activity,
                widget
        )
        nextButton = view.findViewById<Button>(R.id.pz_nextButton)
        nextButton?.setOnClickListener {

            if (!appFieldStepHandler!!.hasNextStep()) return@setOnClickListener

            if (::mAnchor.isInitialized.not()) return@setOnClickListener

            if (AppFieldValidator().isStepValid(getCurrentStep()).not())
            {
                Toast.makeText(
                        activity,
                        "Invalid value",
                        Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            dismiss()
            nextOrDismiss()
        }
        view.findViewById<Button>(R.id.pz_prevButton)?.setOnClickListener {
            this.onDismiss?.invoke()
            this.dismiss()
        }
        view.setBackgroundColor(widget.baseView.backgroundColor)
        return view
    }

    private fun resetStep()
    {
        appFieldStepHandler?.updateStepCount(0)
    }

    private fun dismiss()
    {
        clear()
        tipAttachHelper?.dismiss()
        widget = null

    }

    /**
     * Used to draw background black overlay which prevents outside click instead focus on target view
     * It doesn't draw black overlay on the provided target view
     */
    private fun drawOverLay(target: View, onClose: () -> Unit)
    {
        val view = AppFieldEditOverlay(activity).setExcludedView(target).drawCloseButton()
            .setOnCloseListener(onClose).show()
        mOverLays.add(view)
    }


    override fun getCurrentStep(): Int
    {
        return appFieldStepHandler?.getCurrentStep() ?: 0
    }

    override fun getTotalSteps(): Int
    {
        return appFieldStepHandler?.getTotalStepsCount() ?: 0
    }

    override fun getGuide(): ContextualBase?
    {
        return widget
    }

    override fun onDismiss()
    {
        dismiss()
    }

    override fun nextOrDismiss()
    {
        appFieldStepHandler?.nextStepOrDismiss {
            resetStep()
            dismiss()
            this.onComplete?.invoke()
        }
    }

    override fun getValidation(step: Int): AppFieldEditValidation?
    {
        return getValidationForStep(step)
    }
     private fun getValidationForStep(step:Int): AppFieldEditValidation? {
        return try {
            val array = JSONArray(feed?.json)
            if (array.length() > 0) {
                val jsonOverlay=array.getJSONObject(step-1)
                return Gson().fromJson(jsonOverlay.getJSONObject("extra_json").getJSONObject("validation").toString(),AppFieldEditValidation::class.java)
            } else {
                null
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }
    private fun clear()
    {
        mOverLays.forEach {
            rootView().removeView(it)
        }
    }

    private fun rootView(): ViewGroup
    {
        return Player.getActivity().findViewById<View>(android.R.id.content).rootView as ViewGroup
    }


}