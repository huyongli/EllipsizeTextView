package com.laohu.textviewextend.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import android.view.MotionEvent
import android.view.View
import com.laohu.textviewextend.R
import java.lang.Exception

open class EllipsizeTextView: TextView {
    var ellipsizeColor: Int = -1//默认采用TextView设置的字体颜色
        set(value) {
            field = value
            ellipsizePaint.apply { if(field != -1) color = field }
        }
    var isUnderLineEnable: Boolean = false
        set(value) {
            field = value
            ellipsizePaint.apply { isUnderlineText = field }
        }
    var ellipsizeText: String?
    var isAutoFillBlankChar: Boolean = true//当最后一行不够显示满行的话，自动填充空白，将ellipsizeText显示在此行的最右边
    var isEllipsizeTextNewLine: Boolean = false//是否将Ellipsize单独换行显示

    private var ellipsizeX: Float = 0f
    private var ellipsizeY: Float = 0f
    private var ellipsizeTextWidth: Int = 0
    private var ellipsizeTextBaseLineHeight: Float = 0f//ellipsisText基线离该行顶部的距离

    private val ellipsizePaint: Paint by lazy {
        Paint(paint) .apply {
            isUnderlineText = isUnderLineEnable
            if(ellipsizeColor != -1) color = ellipsizeColor
        }
    }

    private var ellipsizeClickListener: (() -> Unit)? = null
    private var viewClickListener: View.OnClickListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.EllipsizeTextView)
        ellipsizeColor = ta.getColor(R.styleable.EllipsizeTextView_ellipsizeColor, ellipsizeColor)
        ellipsizeText = ta.getString(R.styleable.EllipsizeTextView_ellipsizeText)
        isUnderLineEnable = ta.getBoolean(R.styleable.EllipsizeTextView_ellipsizeUnderlineEnable, isUnderLineEnable)
        isAutoFillBlankChar = ta.getBoolean(R.styleable.EllipsizeTextView_ellipsizeAutoFillBlankChar, isAutoFillBlankChar)
        isEllipsizeTextNewLine = ta.getBoolean(R.styleable.EllipsizeTextView_isEllipsizeTextNewLine, isEllipsizeTextNewLine)
        ta.recycle()
        gravity = Gravity.TOP or Gravity.START
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(TextUtils.isEmpty(ellipsizeText)) {
           return
        }
        try {
            when {
                isEllipsizeTextNewLine -> {
                    setMeasuredDimension(measuredWidth, measuredHeight + lineHeight)
                }
                isAutoFillBlankChar -> {
                    val staticLayout = StaticLayout("$text...$ellipsizeText", paint, measuredWidth, Layout.Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineSpacingExtra, true)

                    val largeMaxOneLine = if(maxLines == Integer.MAX_VALUE) maxLines else maxLines + 1
                    when {
                        staticLayout.lineCount == largeMaxOneLine -> {
                            val newText = measureText(text.toString())
                            text = "$newText..."
                        }
                        staticLayout.lineCount > largeMaxOneLine -> {
                            val largeOneLineLastIndex = staticLayout.getLineEnd(maxLines + 1)
                            val largeOneLineText = text.subSequence(0, largeOneLineLastIndex).toString()
                            val newText = measureText(largeOneLineText)
                            text = "$newText..."
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun measureText(content: String): String {
        val subText = content.substring(0, content.length - 1)
        val staticLayout = StaticLayout("$subText...$ellipsizeText", paint, measuredWidth, Layout.Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineSpacingExtra, true)
        return when {
            staticLayout.lineCount > maxLines -> measureText(subText)
            else -> subText
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if(!TextUtils.isEmpty(ellipsizeText)) {
            this.measureEllipsizeText()
            canvas?.drawText(ellipsizeText, ellipsizeX, ellipsizeY, ellipsizePaint)
        }
    }

    open fun measureEllipsizeText() {
        if(TextUtils.isEmpty(ellipsizeText)) {
            return
        }
        ellipsizeTextWidth = Layout.getDesiredWidth(ellipsizeText, paint).toInt()
        ellipsizeTextBaseLineHeight = lineHeight / 2 + (paint.fontMetrics.descent - paint.fontMetrics.ascent) / 2 - paint.fontMetrics.descent
        ellipsizeY = height - paddingBottom - (lineHeight - ellipsizeTextBaseLineHeight)
        ellipsizeX = (width - paddingRight - ellipsizeTextWidth).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null) {
            return false
        }
        if(isTouchOnEllipsizeText(event) && event.action == MotionEvent.ACTION_UP) {
            this.ellipsizeClickListener?.invoke()
        } else {
            this.viewClickListener?.onClick(this)
        }
        return true
    }

    open fun isTouchOnEllipsizeText(event: MotionEvent) =
            event.y >= height - paddingBottom - lineHeight &&
            event.y <= height - paddingBottom &&
            event.x >= width - paddingRight - ellipsizeTextWidth &&
            event.x <= width - paddingRight

    override fun setOnClickListener(l: OnClickListener?) {
        this.viewClickListener = l
    }

    fun setOnEllipsizeClickListener(ellipsisClickListener: (()-> Unit)?) {
        this.ellipsizeClickListener = ellipsisClickListener
    }
}