package br.senai.sp.jandira.upload_firebase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import br.senai.sp.jandira.upload_firebase.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    //DECLARAÇÃO DOS ATRIBUTOS
    //ACTIVETYMAINBINDING - MANIPULAÇÃO DOS ELEMENTOS GRÁFICOS DO MATERIAL DESING
    private lateinit var binding: ActivityMainBinding

    //ACTIVETYMAINBINDING - PERMITE A MANIPULAÇÃO DOS CLOUD STORAGE (ARMAZENA ARQUIVOS)
    private lateinit var storageRef: StorageReference

    //ACTIVETYMAINBINDING - PERMITE A MANIPULAÇÃO DOS BANCO DE DADOS NOSQL
    private lateinit var firebaseFireStone: FirebaseFirestore

    //URI - PERMITE A MANIPULAÇÃO DE ARQUIVOS DO SEU ENDEREÇAMENTO
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initiVars()
        registreclickEvents()

    }

    private fun initiVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("images")
        firebaseFireStone = FirebaseFirestore.getInstance()
    }

    //funcao lancador de imagens  da galeria
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.imageView.setImageURI(it)
    }

    private fun registreclickEvents() {
        // TRATA O EVENTO DE CLICK DO COMPONENTE IMAGEVIEW
        binding.imageView.setOnClickListener {
            resultLauncher.launch("image/*")

        }
        binding.uploadBtn.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {

        binding.progressBar.visibility = View.VISIBLE

        storageRef = storageRef.child(System.currentTimeMillis().toString())

        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "UPLOAD CONCLUÍDO!!!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "ERRO AO REALIZAR O UPLOAD!!!", Toast.LENGTH_LONG).show()
                }
                binding.progressBar.visibility = View.VISIBLE
            }

        }
    }
}