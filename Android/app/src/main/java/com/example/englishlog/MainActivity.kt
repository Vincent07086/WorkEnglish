package com.example.englishlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.englishlog.ui.MainViewModel
import com.example.englishlog.ui.UiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EnglishLogScreen()
                }
            }
        }
    }
}

@Composable
fun EnglishLogScreen(vm: MainViewModel = viewModel()) {
    val state by vm.state.collectAsStateWithLifecycle()
    if (!state.loggedIn) {
        LoginScreen(vm, state)
    } else {
        HomeScreen(vm, state)
    }
}

@Composable
fun LoginScreen(vm: MainViewModel, state: UiState) {
    var register by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("demo@example.com") }
    var password by remember { mutableStateOf("password123") }
    var name by remember { mutableStateOf("English Learner") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text("EnglishLog", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Keep an English record you can search and review.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(20.dp))
        if (register) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = {
                if (register) vm.register(email, password, name) else vm.login(email, password)
            },
            enabled = !state.loading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (register) "Register" else "Sign in")
        }
        OutlinedButton(
            onClick = { vm.clearError() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Continue with Google")
        }
        TextButton(onClick = { register = !register }) {
            Text(if (register) "Already have an account? Sign in" else "Create account")
        }
        if (state.loading) {
            Spacer(Modifier.height(12.dp))
            CircularProgressIndicator()
        }
        state.error?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun HomeScreen(vm: MainViewModel, state: UiState) {
    var folder by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedFolder by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Your learning log", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = vm::logout) { Text("Sign out") }
        }
        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        }
        Text("Folders", style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = folder,
                onValueChange = { folder = it },
                label = { Text("New folder") },
                modifier = Modifier.weight(1f),
                singleLine = true,
            )
            Button(onClick = {
                vm.addFolder(folder)
                folder = ""
            }) {
                Text("Add")
            }
        }
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                selected = selectedFolder == null,
                onClick = { selectedFolder = null },
                label = { Text("All") },
            )
            state.folders.forEach { item ->
                FilterChip(
                    selected = selectedFolder == item.id,
                    onClick = { selectedFolder = item.id },
                    label = { Text(item.name) },
                )
            }
        }
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("English text / reflection") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
        )
        Button(
            onClick = {
                val folderId = selectedFolder ?: state.folders.firstOrNull()?.id ?: 0L
                vm.addEntry(folderId, title, content)
                title = ""
                content = ""
            },
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            Text("Save note")
        }
        Text("Notes", style = MaterialTheme.typography.titleLarge)
        val notes = state.entries.filter { selectedFolder == null || it.folderId == selectedFolder }
        if (notes.isEmpty()) {
            Text("No notes yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            notes.forEach { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(entry.title, style = MaterialTheme.typography.titleMedium)
                        Text(entry.content)
                    }
                }
            }
        }
        Text(
            "Interview skills",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp),
        )
        state.skills.forEach { skill ->
            Text("${skill.name} → ${skill.file}", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}
