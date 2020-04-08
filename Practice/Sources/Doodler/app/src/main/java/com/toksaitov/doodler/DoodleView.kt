package com.toksaitov.doodler

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.File
import java.io.FileOutputStream
import kotlin.math.hypot

class DoodleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    // State
    private val brushPaint: Paint
    private val brush: Path

    private var loadedImage: Bitmap? = null
    private lateinit var backBuffer: Bitmap
    private lateinit var backBufferCanvas: Canvas
    private val backBufferPaint: Paint = Paint()

    init {
        brushPaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = Color.argb(255, 255, 0, 0)
            strokeWidth = 30f
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }
        brush = Path()
    }

    override fun onDraw(frontBufferCanvas: Canvas) {
        super.onDraw(frontBufferCanvas)

        frontBufferCanvas.drawBitmap(backBuffer, 0.0f, 0.0f, backBufferPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w > 0 && h > 0) {
            backBuffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            backBufferCanvas = Canvas(backBuffer)
            if (loadedImage != null) {
                backBufferCanvas.drawBitmap(loadedImage!!, 0.0f, 0.0f, backBufferPaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> onTouchStarted(event)
            MotionEvent.ACTION_MOVE -> onTouchMoved(event)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> onTouchEnded(event)
        }

        return true
    }

    private fun onTouchStarted(event: MotionEvent) {
        brush.moveTo(event.x, event.y)
    }

    private fun onTouchMoved(event: MotionEvent) {
        val x = event.x
        val previousX = if (event.historySize > 0) event.getHistoricalX(0) else x
        val dx = x - previousX

        val y = event.y
        val previousY = if (event.historySize > 0) event.getHistoricalY(0) else y
        val dy = y - previousY

        if (hypot(dx, dy) > 2.0f) {
            brush.quadTo(x + dx * 0.5f, y + dy * 0.5f, x, y)
            backBufferCanvas.drawPath(brush, brushPaint)
        }

        invalidate()
    }

    private fun onTouchEnded(event: MotionEvent) {
        brush.reset()
    }

    fun loadDoodle(path: String) {
        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        loadedImage = BitmapFactory.decodeFile(path, options)
    }

    @SuppressLint("WrongThread")
    fun saveDoodle(path: String) {
        val file = File(path)
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            backBuffer.compress(Bitmap.CompressFormat.WEBP, 100, fileOutputStream)
            fileOutputStream.flush()
        } finally {
            fileOutputStream?.close()
        }
    }
}
