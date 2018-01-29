import android.support.annotation.DimenRes
import android.view.View

fun View.dpToPx(@DimenRes res: Int) = context.resources.getDimensionPixelOffset(res).toFloat()