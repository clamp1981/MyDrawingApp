package com.example.mydrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView( context : Context, attributeSet : AttributeSet ) : View( context, attributeSet ) {

    private var mDrawingPath : CustomPath? = null
    private var mCanvasBitmap : Bitmap? = null
    private var mDrawPaint : Paint? = null
    private var mCanvasPaint : Paint? = null
    private var mBrushSize : Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas : Canvas? = null
    private var mDrawPaths = ArrayList<CustomPath>()

    init{
        setUpDrawing()
    }


    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mDrawingPath = CustomPath(color,mBrushSize)
        mCanvasPaint = Paint( Paint.DITHER_FLAG )
        //mBrushSize = 20.toFloat()

    }

    public fun setSizeBrush( newSize : Float ){
        mBrushSize = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP,
                                                newSize, resources.displayMetrics )
        mDrawPaint!!.strokeWidth = mBrushSize
    }

    //view 사이즈가 변경 될 때 마다 Canvas를불러옴
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888 )
        canvas = Canvas( mCanvasBitmap!!)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap( mCanvasBitmap!!, 0f,0f, mCanvasPaint )

        for( path in mDrawPaths ){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas?.drawPath( path, mDrawPaint!! )
        }
        if( !mDrawingPath!!.isEmpty ){
            mDrawPaint!!.strokeWidth = mDrawingPath!!.brushThickness
            mDrawPaint!!.color = mDrawingPath!!.color
            canvas?.drawPath( mDrawingPath!!, mDrawPaint!! )
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y
        when( event?.action ){
            MotionEvent.ACTION_DOWN -> {
                mDrawingPath!!.color = color
                mDrawingPath!!.brushThickness = mBrushSize

                mDrawingPath!!.reset()
                if( touchX != null && touchY != null )
                    mDrawingPath!!.moveTo( touchX, touchY )
            }
            MotionEvent.ACTION_MOVE ->{
                if( touchX != null && touchY != null )
                    mDrawingPath!!.lineTo( touchX, touchY )
            }

            MotionEvent.ACTION_UP ->{
                mDrawPaths.add(mDrawingPath!!)
                mDrawingPath = CustomPath( color, mBrushSize )
            }
            else -> return false

        }
        invalidate()
        
        return true
    }

    internal inner class CustomPath( var color : Int, var brushThickness : Float ) : Path(){

    }
}