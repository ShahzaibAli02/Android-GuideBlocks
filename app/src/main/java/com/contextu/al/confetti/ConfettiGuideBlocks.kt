package com.contextu.al.confetti

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.contextu.al.R
import com.contextu.al.common.extensions.clickedOutside
import com.contextu.al.common.extensions.dismiss
import com.contextu.al.model.customguide.ContextualContainer
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import nl.dionsegijn.konfetti.xml.listeners.OnParticleSystemUpdateListener
import java.util.concurrent.TimeUnit


class ConfettiGuideBlocks(private val activity: Activity,private val contextualContainer: ContextualContainer): Dialog(activity) {

    private lateinit var viewKonfetti: KonfettiView
    private var closed_manually:Boolean=false
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0f)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.konfetti)
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )
        viewKonfetti = findViewById<KonfettiView>(R.id.konfettiView)
        viewKonfetti.start(party)
    }

    fun show(onStart: (inputStart: Unit) -> Unit, onEnd: (inputEnd: Unit) -> Unit){
        viewKonfetti.onParticleSystemUpdateListener = object : OnParticleSystemUpdateListener{
            override fun onParticleSystemEnded(view: KonfettiView, party: Party, activeSystems: Int) {
                onEnd
                closed_manually=true
                dismiss()
            }

            override fun onParticleSystemStarted(view: KonfettiView, party: Party, activeSystems: Int) {
                onStart
            }

        }
    }
    override fun dismiss() {

        if(closed_manually)
            contextualContainer.dismiss()
        else
            contextualContainer.clickedOutside()
        super.dismiss()
    }
}