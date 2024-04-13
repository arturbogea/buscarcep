package com.arturbogea.buscarcep.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arturbogea.buscarcep.api.AddressAPI
import com.arturbogea.buscarcep.api.RetrofitAddress
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
        RetrofitAddress.apiCep
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

        binding!!.btnClear.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                clearAddress()
            }
        }

        return view
    }

    suspend fun getAddress(){
        var retornoApi: Response<Address>? = null
        val cepDigitado = binding!!.editCEP.text.toString()

        try {
            val addressAPI = retrofit.create(AddressAPI::class.java)
            retornoApi = addressAPI.recuperarEndereco(cepDigitado)

        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(context, "Erro ao buscar o CEP informado", Toast.LENGTH_SHORT).show()
            Log.i("info_endereco", "Erro ao recuperar endereço")
        }

        if (retornoApi != null){

            if (retornoApi.isSuccessful){
                val address = retornoApi.body()
                val rua = address?.rua
                val bairro = address?.bairro
                val cidade = address?.cidade
                val estado = address?.estado

                withContext(Dispatchers.Main){
                    binding!!.txtRua.text = rua
                    binding!!.txtBairro.text = bairro
                    binding!!.txtCidade.text = cidade
                    binding!!.txtUF.text = estado
                }

            }else{

                withContext(Dispatchers.Main){
                    Toast.makeText(context, "CEP invalido", Toast.LENGTH_LONG).show()
                }
            }

        }else{
            withContext(Dispatchers.Main){
                Toast.makeText(context, "Erro", Toast.LENGTH_LONG).show()
            }
        }

    }

    suspend fun clearAddress(){
        val limparCep = binding!!.editCEP
        val limparRua = binding!!.txtRua
        val limparBairro = binding!!.txtBairro
        val limparCidade = binding!!.txtCidade
        val limparEstado = binding!!.txtUF



        withContext(Dispatchers.Main){
            limparCep.setText("")
            limparRua.setText("")
            limparBairro.setText("")
            limparCidade.setText("")
            limparEstado.setText("")
        }
    }

}