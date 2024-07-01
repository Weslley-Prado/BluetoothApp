package com.wrprado.bluetoothapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 1001
    private lateinit var btnOne: Button
    private lateinit var btnTwo: Button
    private lateinit var btnThree: Button

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var connectedDevice: BluetoothDevice? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

        btnOne = findViewById(R.id.buttonA)
        btnTwo = findViewById(R.id.buttonB)
        btnThree = findViewById(R.id.buttonC)

        btnOne.setOnClickListener {
            showBluetoothDevices()
            Toast.makeText(this, "Botão A clicado e solicitado acesso via bluetooth", Toast.LENGTH_SHORT).show()
            sendData("a")
        }

        btnTwo.setOnClickListener {
            showBluetoothDevices()
            Toast.makeText(this, "Botão B clicado e solicitado acesso via bluetooth", Toast.LENGTH_SHORT).show()
            sendData("b")
        }

        btnThree.setOnClickListener {
            showBluetoothDevices()
            Toast.makeText(this, "Botão C clicado e solicitado acesso via bluetooth", Toast.LENGTH_SHORT).show()
            sendData("c")
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Permissões concedidas, continuar com o código que requer as permissões
                } else {
                    // Permissões negadas, lidar com isso (ex.: mostrar uma mensagem ao usuário)
                    Toast.makeText(this, "Permissões necessárias não concedidas", Toast.LENGTH_SHORT).show()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showBluetoothDevices() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permissão BLUETOOTH_CONNECT não concedida", Toast.LENGTH_SHORT).show()
            return
        }

        if (bluetoothAdapter?.isEnabled == true) {
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            if (pairedDevices != null && pairedDevices.isNotEmpty()) {
                for (device in pairedDevices) {
                    Toast.makeText(this, "Dispositivo: ${device.name}, ${device.address}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Nenhum dispositivo Bluetooth pareado encontrado", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Bluetooth não está ativado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendData(data: String) {
        if (connectedDevice != null) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return
                }
                val socket: BluetoothSocket = connectedDevice!!.createInsecureRfcommSocketToServiceRecord(UUID.randomUUID())
                socket.connect()
                val outputStream = socket.outputStream
                val inputStream = socket.inputStream

                val dataBytes = data.toByteArray()
                outputStream.write(dataBytes)

                // Ler a resposta do dispositivo para garantir que a comunicação está funcionando
                val buffer = ByteArray(1024)
                val bytes = inputStream.read(buffer)
                val response = String(buffer, 0, bytes)
                Toast.makeText(this, "Resposta: $response", Toast.LENGTH_SHORT).show()

                outputStream.close()
                inputStream.close()
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao enviar dados via Bluetooth", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Nenhum dispositivo Bluetooth conectado", Toast.LENGTH_SHORT).show()
        }
    }
}
