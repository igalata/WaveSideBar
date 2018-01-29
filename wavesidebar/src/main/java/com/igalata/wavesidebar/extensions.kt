import android.support.annotation.DimenRes
import android.view.MotionEvent
import android.view.View

fun View.dpToPx(@DimenRes res: Int) = context.resources.getDimensionPixelOffset(res).toFloat()

fun MotionEvent.isClick(startX: Float, startY: Float): Boolean {
    return Math.abs(this.x - startX) < 10 && Math.abs(this.y - startY) < 10
}

fun MotionEvent.isPulled(startX: Float, pullOffset: Float): Boolean {
    return this.x - startX > pullOffset
}

fun MotionEvent.isPulledBack(startX: Float, pullOffset: Float): Boolean {
    return startX - this.x > pullOffset
}