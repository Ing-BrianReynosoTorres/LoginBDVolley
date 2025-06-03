package com.example.loginbdvolley

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        var login_usuario=findViewById<TextView>(R.id.login_usuario)
        var id_usuario=findViewById<TextView>(R.id.id_usuario)
        var nombre_usuario=findViewById<TextView>(R.id.nombre_usuario)
        var correo_usuario=findViewById<TextView>(R.id.correo_usuario)
        var boton_salir=findViewById<Button>(R.id.boton_salir)

        var intent_params=getIntent()
        login_usuario.text=intent_params.getStringExtra("Usuario")
        id_usuario.text="ID: "+intent_params.getStringExtra("ID")
        nombre_usuario.text="Usuario: "+intent_params.getStringExtra("Usuario")
        correo_usuario.text="Correo: "+intent_params.getStringExtra("Correo")

        boton_salir.setOnClickListener{
            eliminar_sesion()

            var intent=Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun eliminar_sesion(){
        var eliminar_valores_sesion: SharedPreferences.Editor=this.getSharedPreferences(MainActivity.Global.sharedPrefFile, Context.MODE_PRIVATE).edit()
        eliminar_valores_sesion.clear()
        eliminar_valores_sesion.apply()
        eliminar_valores_sesion.commit()
    }
}