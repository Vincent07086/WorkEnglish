package com.example.englishlog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.englishlog.data.ApiFactory
import com.example.englishlog.data.AuthRequest
import com.example.englishlog.data.EnglishLogApi
import com.example.englishlog.data.Entry
import com.example.englishlog.data.Folder
import com.example.englishlog.data.Skill
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val loggedIn: Boolean = false,
    val folders: List<Folder> = emptyList(),
    val entries: List<Entry> = emptyList(),
    val skills: List<Skill> = emptyList(),
    val error: String? = null,
    val loading: Boolean = false,
)

class MainViewModel @JvmOverloads constructor(
    private val api: EnglishLogApi = ApiFactory.create(),
) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching { api.login(AuthRequest(email.trim(), password)) }
                .onSuccess { load() }
                .onFailure { failure ->
                    _state.update {
                        it.copy(loading = false, error = "Login failed: ${failure.message}")
                    }
                }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            runCatching { api.register(AuthRequest(email.trim(), password, name.trim())) }
                .onSuccess { load() }
                .onFailure { failure ->
                    _state.update {
                        it.copy(loading = false, error = "Registration failed: ${failure.message}")
                    }
                }
        }
    }

    private suspend fun load() {
        runCatching {
            Triple(api.folders(), api.entries(), api.skills())
        }.onSuccess { (folders, entries, skills) ->
            _state.value = UiState(
                loggedIn = true,
                folders = folders,
                entries = entries,
                skills = skills,
            )
        }.onFailure { failure ->
            _state.update {
                it.copy(loggedIn = true, loading = false, error = failure.message)
            }
        }
    }

    fun addFolder(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            runCatching { api.createFolder(Folder(name = trimmed)) }
                .onSuccess { folder ->
                    _state.update { it.copy(folders = it.folders + folder, error = null) }
                }
                .onFailure { failure ->
                    _state.update { it.copy(error = failure.message) }
                }
        }
    }

    fun addEntry(folderId: Long, title: String, content: String) {
        val trimmedTitle = title.trim()
        val trimmedContent = content.trim()
        if (trimmedTitle.isEmpty() || trimmedContent.isEmpty()) return
        viewModelScope.launch {
            runCatching {
                api.createEntry(
                    Entry(folderId = folderId, title = trimmedTitle, content = trimmedContent),
                )
            }.onSuccess { entry ->
                _state.update { it.copy(entries = listOf(entry) + it.entries, error = null) }
            }.onFailure { failure ->
                _state.update { it.copy(error = failure.message) }
            }
        }
    }

    fun logout() {
        _state.value = UiState()
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}
