package com.example.mydrawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private var drawingView : DrawingView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.dvDrawingView)
        drawingView?.setSizeBrush( 10.toFloat() )
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
}