package com.example.mydrawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.iterator

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var drawingView : DrawingView? = null
    private var mImageButtonCurrentPaint : ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.ll_paint_colors)
        for(  paint_color in linearLayoutPaintColors ) (paint_color as ImageButton).setOnClickListener(this)

        mImageButtonCurrentPaint = linearLayoutPaintColors[0] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable( this, R.drawable.pallet_pressed )
        )


        drawingView = findViewById(R.id.dvDrawingView)
        drawingView?.setSizeBrush( 10.toFloat() )
        drawingView?.setColorBrush( mImageButtonCurrentPaint!!.tag.toString() )
        var btnOpenBrushDlg : ImageButton = findViewById(R.id.btnOpenBrushSizeDlg)
        btnOpenBrushDlg.setOnClickListener{
            showBrushSizeDialog()
        }
    }

    private fun showBrushSizeDialog(){
        val brushDlg = Dialog(this)
        brushDlg.setContentView(R.layout.dialog_brush_size)
        brushDlg.setTitle("Brush Size: ")
        val btnSmallBtn : ImageButton = brushDlg.findViewById(R.id.ib_small_brush)
        val btnMediumBtn : ImageButton =brushDlg.findViewById(R.id.ib_medium_brush)
        val btnLargeBtn : ImageButton =brushDlg.findViewById(R.id.ib_large_brush)
        btnSmallBtn.setOnClickListener{
            drawingView?.setSizeBrush( 10.toFloat() )
            brushDlg.dismiss()
        }

        btnMediumBtn.setOnClickListener{
            drawingView?.setSizeBrush( 15.toFloat() )
            brushDlg.dismiss()
        }

        btnLargeBtn.setOnClickListener{
            drawingView?.setSizeBrush( 20.toFloat() )
            brushDlg.dismiss()
        }



        brushDlg.show()
    }


    override fun onClick(view: View?) {
        val clickedBtn = view as ImageButton
        if( clickedBtn != mImageButtonCurrentPaint ){
            clickedBtn.setImageDrawable(
                ContextCompat.getDrawable( this, R.drawable.pallet_pressed )
            )
            mImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable( this, R.drawable.pallet_normal )
            )
            var colorTag = clickedBtn.tag.toString();
            drawingView?.setColorBrush(colorTag)
            mImageButtonCurrentPaint = clickedBtn
        }


    }
}