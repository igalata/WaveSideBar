package com.igalata.wavesidebar

import android.animation.Animator

/**
 * Created by irinagalata on 1/29/18.
 */
interface OnAnimationFinishedListener : Animator.AnimatorListener {

    override fun onAnimationCancel(animation: Animator?) = Unit

    override fun onAnimationStart(animation: Animator?) = Unit

    override fun onAnimationRepeat(animation: Animator?) = Unit

}