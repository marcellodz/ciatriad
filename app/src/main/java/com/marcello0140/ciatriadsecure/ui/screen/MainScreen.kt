package com.marcello0140.ciatriadsecure.ui.screen

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.marcello0140.ciatriadsecure.R
import com.marcello0140.ciatriadsecure.helper.BackupHelper
import com.marcello0140.ciatriadsecure.helper.SecurityHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    var sender by remember { mutableStateOf("") }
    var receiver by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var encryptedData by remember { mutableStateOf("") }
    var hashedTransaction by remember { mutableStateOf("") }
    var decryptedData by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    var senderError by remember { mutableStateOf(false) }
    var receiverError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.masukkan_data),
                style = MaterialTheme.typography.headlineSmall
            )

            // Input Fields with Validation
            OutlinedTextField(
                value = sender,
                onValueChange = {
                    sender = it
                    senderError = it.isBlank()
                },
                label = { Text(stringResource(R.string.reciever)) },
                isError = senderError,
                modifier = Modifier.fillMaxWidth()
            )
            if (senderError) Text("⚠ Harap isi pengirim", color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = receiver,
                onValueChange = {
                    receiver = it
                    receiverError = it.isBlank()
                },
                label = { Text(stringResource(R.string.sender)) },
                isError = receiverError,
                modifier = Modifier.fillMaxWidth()
            )
            if (receiverError) Text("⚠ Harap isi penerima", color = MaterialTheme.colorScheme.error)

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    amountError = it.isBlank() || it.toDoubleOrNull() == null || it.toDouble() == 0.0
                },
                label = { Text("Nominal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = amountError,
                modifier = Modifier.fillMaxWidth()
            )
            if (amountError) Text("⚠ Harap isi nominal yang valid", color = MaterialTheme.colorScheme.error)

            // Encryption & Hashing Button with Sanity Check
            Button(
                onClick = {
                    senderError = sender.isBlank()
                    receiverError = receiver.isBlank()
                    amountError = amount.isBlank() || amount.toDoubleOrNull() == null || amount.toDouble() == 0.0

                    if (!senderError && !receiverError && !amountError) {
                        encryptedData = SecurityHelper.encryptData("$sender:$receiver:$amount")
                        hashedTransaction = SecurityHelper.hashTransaction("$sender:$receiver:$amount")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🔒 Kirimkan")
            }

            // Encrypted & Hashed Results
            if (encryptedData.isNotEmpty() && hashedTransaction.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🔐 Enkripsi:", style = MaterialTheme.typography.bodyLarge)
                    Text(encryptedData)

                    Text("🔗 Hash:", style = MaterialTheme.typography.bodyLarge)
                    Text(hashedTransaction)
                }
            }

            // Decryption Button
            Button(
                onClick = { decryptedData = SecurityHelper.decryptData(encryptedData) },
                modifier = Modifier.fillMaxWidth(),
                enabled = encryptedData.isNotEmpty()
            ) {
                Text("🔓 Dekripsi Data")
            }

            // Decrypted Data
            if (decryptedData.isNotEmpty()) {
                val (decryptedSender, decryptedReceiver, decryptedAmount) = decryptedData.split(":")
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("📜 Data Asli:", style = MaterialTheme.typography.bodyLarge)
                    Text("Pengirim: $decryptedSender")
                    Text("Penerima: $decryptedReceiver")
                    Text("Nominal: $decryptedAmount")
                }
            }

            // Backup Button
            Button(
                onClick = {
                    (context as? ComponentActivity)?.lifecycleScope?.launch {
                        BackupHelper.saveBackup(context, decryptedData)
                        Toast.makeText(context, "Backup Berhasil", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = decryptedData.isNotEmpty()
            ) {
                Text("💾 Simpan Backup")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}
