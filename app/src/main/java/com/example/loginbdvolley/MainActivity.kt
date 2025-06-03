package com.example.loginbdvolley

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    object Global{
        val sharedPrefFile = "sharedpreference"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var usuario=findViewById<EditText>(R.id.edittext_usuario)
        var pass=findViewById<EditText>(R.id.edittext_pass)
        var boton_logear=findViewById<Button>(R.id.boton_logear)
        var boton_crear_cuenta=findViewById<Button>(R.id.boton_crear_cuenta)

        verificar_session_abierta()

        boton_logear.setOnClickListener{
            if(usuario.text.toString()!="" && pass.text.toString()!=""){
                login_bd_volley(usuario.text.toString(),pass.text.toString())
            }
            else{
                Toast.makeText(applicationContext,"Escriba el usuario/contraseña.",Toast.LENGTH_LONG).show()
            }
        }

        boton_crear_cuenta.setOnClickListener{
            var dialogo_cuenta=DialogoCrearCuenta()
            dialogo_cuenta.show(supportFragmentManager,null)
        }


    }

    fun login_bd_volley(usuario:String,pass:String){
        var url="http://192.168.1.70/AndroidLogin/conexion_bd.php"

        var peticion_post=object:StringRequest(Method.POST,url,Response.Listener { response->
            try{
                var respuesta=JSONArray(response)
                var valores=respuesta.getJSONObject(0)

                var intent=Intent(applicationContext,Bienvenida::class.java)
                intent.putExtra("ID",valores.get("ID").toString())
                intent.putExtra("Usuario",valores.get("Nombre_Usuario").toString())
                intent.putExtra("Correo",valores.get("Correo").toString())
                startActivity(intent)

                guardar_sesion(valores.get("ID").toString(), valores.get("Nombre_Usuario").toString(), valores.get("Correo").toString())
            }
            catch(ex:Exception){
                Toast.makeText(applicationContext,"Usuario/contraseña inválido(s).",Toast.LENGTH_LONG).show()
            }

        },Response.ErrorListener { error->
            Toast.makeText(applicationContext,error.message,Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams():MutableMap<String,String>{
                var params=HashMap<String,String>()
                params.put("usuario",usuario)
                params.put("contrasenia",pass)

                return params
            }
        }
        Volley.newRequestQueue(this).add(peticion_post)
    }

    fun verificar_session_abierta(){
        var verificar_valores_sesion: SharedPreferences=this.getSharedPreferences(Global.sharedPrefFile, Context.MODE_PRIVATE)
        var id: String?=verificar_valores_sesion.getString("ID",null)
        var usuario: String?= verificar_valores_sesion.getString("Usuario",null)
        var correo: String?= verificar_valores_sesion.getString("Correo",null)

        if(id!=null && usuario!=null && correo!=null){
            var intent=Intent(applicationContext,Bienvenida::class.java)
            intent.putExtra("ID",id)
            intent.putExtra("Usuario",usuario)
            intent.putExtra("Correo",correo)
            startActivity(intent)
        }
    }

    fun guardar_sesion(id: String, usuario:String, correo:String){
        var guardar_valores_sesion: SharedPreferences.Editor=this.getSharedPreferences(Global.sharedPrefFile, Context.MODE_PRIVATE).edit()
        guardar_valores_sesion.putString("ID",id)
        guardar_valores_sesion.putString("Usuario",usuario)
        guardar_valores_sesion.putString("Correo",correo)
        guardar_valores_sesion.apply()
        guardar_valores_sesion.commit()
    }
}