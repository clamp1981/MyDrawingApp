package com.example.mydrawingapp


import android.Manifest
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.iterator
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var drawingView : DrawingView? = null
    private var mImageButtonCurrentPaint : ImageButton? = null
    private var btnUndo : ImageButton? = null
    private var btnRedo : ImageButton? = null
    val openGalleryLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult( ActivityResultContracts.StartActivityForResult() ){
            result ->
            if( result.resultCode == RESULT_OK && result.data != null ){
                val imageBackground : ImageView = findViewById(R.id.iv_image )
                imageBackground.setImageURI(result.data?.data )
            }
        }
    val requestPermission : ActivityResultLauncher<Array<String>> =
        registerForActivityResult( ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val permissionKey = it.key
                val isGranted = it.value

                if( isGranted){
                    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                    openGalleryLauncher.launch(pickIntent)
                    //Toast.makeText(
                    //    this@MainActivity,
                    //   "Permission granted now you can read the storage files.",
                    //    Toast.LENGTH_LONG
                    //).show()
                }else{
                    if( permissionKey == Manifest.permission.READ_EXTERNAL_STORAGE ){
                        Toast.makeText(
                            this@MainActivity,
                            "Permission denied now you can't read the storage files.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

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
        var btnOpenGallery : ImageButton =  findViewById(R.id.btnOpenGallery)
        btnOpenGallery.setOnClickListener{
            requestPermisson()
        }

        btnUndo = findViewById(R.id.btnUndo )
        btnUndo?.setOnClickListener{
            drawingView?.undoDrawing()
        }

        btnRedo = findViewById(R.id.btnRedo )
        btnRedo?.setOnClickListener{
            drawingView?.redoDrawing()
        }
    }

    private fun requestPermisson(){

        if( ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ){
            showRationaleDialog("MyDrawingApp",
            "MyDrawingApp needs to Access Your External Storage!")
        }else{
            requestPermission.launch( arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE  ))
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

    private fun showRationaleDialog( title : String, message: String ){
        val builder : AlertDialog.Builder = AlertDialog.Builder( this )
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _->
                dialog.dismiss()
            }

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


