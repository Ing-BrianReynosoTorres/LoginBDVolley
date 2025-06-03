package com.example.loginbdvolley

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class DialogoCrearCuenta : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View=inflater.inflate(R.layout.fragment_dialogo_crear_cuenta, container, false)

        var usuario=view.findViewById<EditText>(R.id.edittext_usuario)
        var correo=view.findViewById<EditText>(R.id.edittext_correo)
        var pass=view.findViewById<EditText>(R.id.edittext_pass)
        var boton_enviar=view.findViewById<Button>(R.id.boton_enviar_cuenta)

        boton_enviar.setOnClickListener{
            if(usuario.text.toString()!="" && pass.text.toString()!=""){
                if(correo.text.toString()!="" && Patterns.EMAIL_ADDRESS.matcher(correo.text.toString()).matches()){
                    crear_cuenta_bd_volley(usuario.text.toString(),correo.text.toString(), pass.text.toString())
                }
                else{
                    Toast.makeText(requireContext(),"Formato de correo incorrecto.",Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(requireContext(),"Escriba su usuario, correo y contraseña.",Toast.LENGTH_LONG).show()
            }
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return view
    }

    fun crear_cuenta_bd_volley(usuario:String, correo:String, pass:String){
        var url="http://192.168.1.70/AndroidLogin/conexion_bd_crear_cuenta.php"

        var peticion_post=object: StringRequest(Method.POST,url, Response.Listener { response->
            try{
                var respuesta= JSONArray(response)
                var valores=respuesta.getJSONObject(0)

                var intent=Intent(requireContext(),Bienvenida::class.java)
                intent.putExtra("ID",valores.get("ID").toString())
                intent.putExtra("Usuario",usuario)
                intent.putExtra("Correo",correo)
                startActivity(intent)

                Toast.makeText(requireContext(),"Cuenta creada.", Toast.LENGTH_LONG).show()

                var guardar_sesion:MainActivity=activity as MainActivity
                guardar_sesion.guardar_sesion(valores.get("ID").toString(),usuario,correo)

                dismiss()
            }
            catch(ex:Exception){
                Toast.makeText(requireContext(),"Usuario existente en la base de datos.", Toast.LENGTH_LONG).show()
            }

        }, Response.ErrorListener { error->
            Toast.makeText(requireContext(),error.message, Toast.LENGTH_LONG).show()
        })
        {
            override fun getParams():MutableMap<String,String>{
                var params=HashMap<String,String>()
                params.put("usuario",usuario)
                params.put("correo",correo)
                params.put("contrasenia",pass)

                return params
            }
        }
        Volley.newRequestQueue(requireContext()).add(peticion_post)
    }
}