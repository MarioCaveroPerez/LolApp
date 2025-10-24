package com.example.lolapp.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lolapp.Data.Skins
import com.example.lolapp.R
import com.example.lolapp.databinding.ItemSkinBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SkinsAdapter(private val championId: String, private var skinsList: List<Skins>) :
    RecyclerView.Adapter<SkinsAdapter.SkinsViewHolder>() {

    inner class SkinsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSkin = itemView.findViewById<ImageView>(R.id.ivSkin)
        val btnDownload = itemView.findViewById<ImageButton>(R.id.btnDownload)

        val tvSkinName = itemView.findViewById<TextView>(R.id.tvSkinName)

        var imgChroma = itemView.findViewById<ImageView>(R.id.imageChroma)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkinsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_skin, parent, false)
        return SkinsViewHolder(view)
    }

    override fun getItemCount(): Int = skinsList.size

    override fun onBindViewHolder(holder: SkinsViewHolder, position: Int) {
        val skin = skinsList[position]
        holder.tvSkinName.text = if (skin.name == "default") "Predeterminado" else skin.name

        val splashUrl = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${championId}_${skin.num}.jpg"
        Picasso.get()
            .load(splashUrl)
            .fit()
            .centerCrop()
            .into(holder.ivSkin)
        holder.btnDownload.setOnClickListener {
            val context = holder.itemView.context

            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Descargar Imagen")
                .setMessage("Se descargará la imagen en su dispositivo móvil")
                .setPositiveButton("Sí") { dialog, _ ->
                    downloadImage(context, splashUrl, skin.name)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        if (skin.chromas != false){
            holder.imgChroma.visibility = View.VISIBLE
        }

    }

    private fun downloadImage(context: Context, imageUrl: String, skinName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Descargar la imagen como Bitmap
                val bitmap = Picasso.get().load(imageUrl).get()
                val filename = "$skinName.jpg"
                var savedUri: android.net.Uri? = null

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    // Android 10+ Scoped Storage
                    val contentValues = android.content.ContentValues().apply {
                        put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(android.provider.MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/LolSkins")
                    }

                    val resolver = context.contentResolver
                    savedUri = resolver.insert(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )

                    savedUri?.let { uri ->
                        resolver.openOutputStream(uri)?.use { out ->
                            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
                        }
                    }

                } else {
                    // Android 9 o inferior
                    val path = android.os.Environment.getExternalStoragePublicDirectory(
                        android.os.Environment.DIRECTORY_PICTURES
                    ).toString()
                    val file = java.io.File(path, filename)
                    java.io.FileOutputStream(file).use { out ->
                        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
                    }
                    savedUri = android.net.Uri.fromFile(file)
                }

                // Mostrar Toast en el hilo principal
                withContext(Dispatchers.Main) {
                    if (savedUri != null) {
                        android.widget.Toast.makeText(context, "Imagen Descargada Correctamente!", android.widget.Toast.LENGTH_SHORT).show()
                    } else {
                        android.widget.Toast.makeText(context, "Error al guardar imagen", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(context, "Error: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    fun updateList(newList: List<Skins>) {
        skinsList = newList
        notifyDataSetChanged()
    }
}