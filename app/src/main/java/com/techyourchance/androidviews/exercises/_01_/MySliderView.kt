package com.techyourchance.androidviews.exercises._01_

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.BaseSavedState
import androidx.core.content.ContextCompat
import com.techyourchance.androidviews.CustomViewScaffold
import com.techyourchance.androidviews.R
import com.techyourchance.androidviews.exercises._03_.SliderChangeListener
import kotlin.math.sqrt

class MySliderView : CustomViewScaffold {

    private var lineXLeft: Float = 0f
    private var lineXRight: Float = 0f
    private var lineYPos: Float = 0f
    private var lineHeight: Float = 0f

    private var circleXCenter: Float = 0f
    private var circleYCenter: Float = 0f
    private var circleRadius: Float = 0f

    private var isDragged = false
    private var lastActionEventX = 0f
    var value = 0.5f
        private set
    private var onValueChangeListener: SliderChangeListener? = null

    private val paint = Paint()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(
        context, attrs, defStyleAttr, defStyleRes
    )

    fun setOnValueChangeListener(listener: SliderChangeListener) {
        onValueChangeListener = listener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }

        if (event.action == MotionEvent.ACTION_DOWN && isCircleTouched(event.x, event.y)) {
            isDragged = true
            lastActionEventX = event.x
        } else if (isDragged && event.action == MotionEvent.ACTION_MOVE) {

            val newCircleCenter = circleXCenter + event.x - lastActionEventX

            if (newCircleCenter in lineXLeft..lineXRight) {
                circleXCenter = newCircleCenter
                lastActionEventX = event.x
                invalidate()
            } else if (lastActionEventX > lineXLeft && newCircleCenter < lineXLeft) {
                circleXCenter = lineXLeft
                lastActionEventX = lineXLeft
                invalidate()
            } else if (lastActionEventX < lineXRight && newCircleCenter > lineXRight) {
                circleXCenter = lineXRight
                lastActionEventX = lineXRight
                invalidate()
            }
            val newValue = (circleXCenter - lineXLeft) / lineHeight
            if (newValue != value) {
                onValueChangeListener?.onValueChanged(value = newValue)
                value = newValue
            }
        } else {
            isDragged = false
        }
        return true
    }

    private fun isCircleTouched(x: Float, y: Float): Boolean {
        val dx = x - circleXCenter
        val dy = y - circleYCenter
        return sqrt(dx * dx + dy * dy) <= circleRadius
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        circleRadius = dpToPx(15)
        lineYPos = h.toFloat() / 2

        val paddingHorizontal = dpToPx(10)
        lineHeight = w - (paddingHorizontal + circleRadius) * 2
        lineXLeft = paddingHorizontal + circleRadius
        lineXRight = w - paddingHorizontal - circleRadius

        circleXCenter = w.toFloat() * value
        circleYCenter = lineYPos
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = Color.GRAY
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE

        canvas.drawLine(lineXLeft, lineYPos, lineXRight, lineYPos, paint)

        paint.strokeWidth = 1f
        paint.color = Color.RED
        canvas.drawLine(lineXLeft, 0f, lineXLeft, height.toFloat(), paint)
        canvas.drawLine(lineXRight, 0f, lineXRight, height.toFloat(), paint)

        paint.color = ContextCompat.getColor(context, R.color.primary)
        paint.style = Paint.Style.FILL

        canvas.drawCircle(circleXCenter, circleYCenter, circleRadius, paint)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return super.onSaveInstanceState()?.let { superState ->
            val updatedState = MySliderSavedState(superState)
            updatedState.save(value)
            updatedState
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        if (state is MySliderSavedState) {
            value = state.restore()
        }
    }

    private class MySliderSavedState: BaseSavedState {

        private var value = 0f

        constructor(superState: Parcelable) : super(superState)

        constructor(parcel: Parcel) : super(parcel) {
            value = parcel.readFloat()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(value)
        }

        fun save(value: Float) {
            this.value = value
        }

        fun restore() = value

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<MySliderSavedState> {
            override fun createFromParcel(parcel: Parcel): MySliderSavedState {
                return MySliderSavedState(parcel)
            }

            override fun newArray(size: Int): Array<MySliderSavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}

