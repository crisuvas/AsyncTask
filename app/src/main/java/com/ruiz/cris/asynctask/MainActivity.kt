package com.ruiz.cris.asynctask

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.Request

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        tvResult = findViewById(R.id.tv_result)
        getQuestion().execute()
    }

    internal inner class getQuestion : AsyncTask<Void, Void, String>(){

        lateinit var  progressDialog : ProgressDialog
        var hasInternet = false

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Downloading Question...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

        override fun doInBackground(vararg params: Void?): String {

            if(isNetworkAvailable()){

                hasInternet = true
                val client = OkHttpClient()
                val url = "https://script.googleusercontent.com/macros/echo?user_content_key=1tgBN-ES-vsiLin8Lggs7R094sUSEWlBY3Lv7yLt0KnrexUuaTvreORsTenxGH0HaPDQ0rUkXVqmkc903P_gQrpXCbi98gcsm5_BxDlH2jW0nuo2oDemN9CCS2h10ox_1xSncGQajx_ryfhECjZEnBg4Wj9So2Q_mI0_S0Bm21-AGmcRnplmVaRcxvVzvCi9cnQQJegsnVb9TgJzPufw35cdv3aNHr6K&lib=MKMzvVvSFmMa3ZLOyg67WCThf1WVRYg6Z"
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                return response.body()?.string().toString()

            }else {
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()

            if(hasInternet){
                tvResult.text = result
            }else{
                tvResult.text = "No internet"
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}