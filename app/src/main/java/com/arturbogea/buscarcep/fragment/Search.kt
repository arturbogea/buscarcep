package com.arturbogea.buscarcep.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arturbogea.buscarcep.api.AndressAPI
import com.arturbogea.buscarcep.api.RetrofitAndress
import com.arturbogea.buscarcep.databinding.FragmentSearchBinding
import com.arturbogea.buscarcep.model.Address
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.create

class Search : Fragment() {

    private var binding: FragmentSearchBinding? = null

    private val retrofit by lazy {
        RetrofitAndress.apiCep
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding!!.root

        binding!!.btnBuscar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                getAddress()
            }
        }

        return view
    }

    suspend fun getAddress(){
        var retornoApi: Response<Address>? = null
        val cepDigitado = binding!!.editCEP.text.toString()

        try {
            val andressAPI = retrofit.create(AndressAPI::class.java)
            retornoApi = andressAPI.recuperarEndereco(cepDigitado)

        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(context, "Erro ao buscar o CEP informado", Toast.LENGTH_SHORT).show()
            Log.i("info_endereco", "Erro ao recuperar endereço")
        }

        if (retornoApi != null){

            if (retornoApi.isSuccessful){
                val andress = retornoApi.body()
                val rua = andress?.rua
                val bairro = andress?.bairro
                val cidade = andress?.cidade
                val estado = andress?.estado

                withContext(Dispatchers.Main){
                    binding!!.txtRua.text = rua
                    binding!!.txtBairro.text = bairro
                    binding!!.txtCidade.text = cidade
                    binding!!.txtUF.text = estado
                }

            }

        }else{
            Toast.makeText(context, "Digite um CEP valido", Toast.LENGTH_SHORT).show()
        }

    }


}