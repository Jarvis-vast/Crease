package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AuditLogEntity
import com.example.data.model.DailyBriefingEntity
import com.example.data.model.InningsEntity
import com.example.data.model.MatchEntity
import com.example.data.model.PlayerProfileEntity
import com.example.data.model.UploadRecordEntity
import com.example.data.parser.ParsedScorecardResult
import com.example.data.parser.ScorecardParser
import com.example.data.repository.CricketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    OVERVIEW,
    MATCH_CENTER,
    SCORECARDS,
    PLAYERS,
    TEAMS_TOURNAMENTS,
    ANALYTICS_LAB,
    UPLOAD_REVIEW,
    REPORTS,
    INTEGRATIONS,
    SETTINGS_AUDIT
}

class CricketViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CricketRepository(application)

    // Navigation & Global UI State
    private val _currentTab = MutableStateFlow(ScreenTab.OVERVIEW)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedMatchId = MutableStateFlow<String?>(null)
    val selectedMatchId: StateFlow<String?> = _selectedMatchId.asStateFlow()

    private val _selectedPlayerId = MutableStateFlow<String?>(null)
    val selectedPlayerId: StateFlow<String?> = _selectedPlayerId.asStateFlow()

    // Analytics Comparison Players
    private val _comparePlayer1Id = MutableStateFlow<String?>("p_virat_kohli")
    val comparePlayer1Id: StateFlow<String?> = _comparePlayer1Id.asStateFlow()

    private val _comparePlayer2Id = MutableStateFlow<String?>("p_rohit_sharma")
    val comparePlayer2Id: StateFlow<String?> = _comparePlayer2Id.asStateFlow()

    // Upload & Review state
    private val _rawUploadText = MutableStateFlow("")
    val rawUploadText: StateFlow<String> = _rawUploadText.asStateFlow()

    private val _selectedPlatform = MutableStateFlow("Real Cricket 24")
    val selectedPlatform: StateFlow<String> = _selectedPlatform.asStateFlow()

    private val _parsedPreview = MutableStateFlow<ParsedScorecardResult?>(null)
    val parsedPreview: StateFlow<ParsedScorecardResult?> = _parsedPreview.asStateFlow()

    private val _uploadNotification = MutableStateFlow<String?>(null)
    val uploadNotification: StateFlow<String?> = _uploadNotification.asStateFlow()

    // Data Flows from Repository
    val allMatches: StateFlow<List<MatchEntity>> = repository.getAllMatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPlayers: StateFlow<List<PlayerProfileEntity>> = repository.getAllPlayers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUploads: StateFlow<List<UploadRecordEntity>> = repository.getAllUploadRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLogEntity>> = repository.getAuditLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val briefings: StateFlow<List<DailyBriefingEntity>> = repository.getAllBriefings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered matches
    val filteredMatches = combine(allMatches, _searchQuery) { matches, query ->
        if (query.isBlank()) matches
        else matches.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.tournament.contains(query, ignoreCase = true) ||
            it.sourcePlatform.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered players
    val filteredPlayers = combine(allPlayers, _searchQuery) { players, query ->
        if (query.isBlank()) players
        else players.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.team.contains(query, ignoreCase = true) ||
            it.role.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()
        }
    }

    fun selectTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectMatch(matchId: String?) {
        _selectedMatchId.value = matchId
        if (matchId != null) {
            _currentTab.value = ScreenTab.SCORECARDS
        }
    }

    fun selectPlayer(playerId: String?) {
        _selectedPlayerId.value = playerId
    }

    fun setComparePlayer1(id: String) {
        _comparePlayer1Id.value = id
    }

    fun setComparePlayer2(id: String) {
        _comparePlayer2Id.value = id
    }

    fun setRawUploadText(text: String) {
        _rawUploadText.value = text
    }

    fun setSelectedPlatform(platform: String) {
        _selectedPlatform.value = platform
    }

    fun loadSamplePreset(presetName: String) {
        val presets = ScorecardParser.getSampleGamingPresets()
        val found = presets.find { it.first == presetName }
        if (found != null) {
            _rawUploadText.value = found.second
            _selectedPlatform.value = if (presetName.contains("Real Cricket")) "Real Cricket 24"
                                      else if (presetName.contains("WCC")) "WCC 3"
                                      else "Stick Cricket Live"
            parseCurrentUpload()
        }
    }

    fun parseCurrentUpload() {
        val text = _rawUploadText.value
        if (text.isBlank()) {
            _uploadNotification.value = "Please enter scorecard text or select a gaming preset"
            return
        }
        val result = repository.parseScorecard(text, _selectedPlatform.value)
        _parsedPreview.value = result
        _uploadNotification.value = "Extracted successfully! Confidence: ${(result.confidence * 100).toInt()}%"
    }

    fun commitParsedScorecard(fileName: String = "gaming_scorecard_upload.txt") {
        val current = _parsedPreview.value ?: return
        viewModelScope.launch {
            val matchId = repository.commitScorecard(current, fileName)
            _uploadNotification.value = "Scorecard successfully validated and imported! Match ID: $matchId"
            _parsedPreview.value = null
            _rawUploadText.value = ""
            _selectedMatchId.value = matchId
            _currentTab.value = ScreenTab.SCORECARDS
        }
    }

    fun dismissNotification() {
        _uploadNotification.value = null
    }

    fun triggerMorningBriefing() {
        viewModelScope.launch {
            val briefing = repository.triggerDailyMorningBriefing()
            _uploadNotification.value = "Morning briefing triggered! Sent at ${briefing.deliveredAt}"
        }
    }

    fun resetData() {
        viewModelScope.launch {
            repository.resetDatabase()
            _uploadNotification.value = "Database calibrated and reset to initial state"
        }
    }

    fun deleteMatch(id: String) {
        viewModelScope.launch {
            repository.deleteMatch(id)
            if (_selectedMatchId.value == id) {
                _selectedMatchId.value = null
            }
            _uploadNotification.value = "Match removed from platform"
        }
    }

    fun mergePlayers(sourceId: String, targetId: String) {
        viewModelScope.launch {
            repository.mergePlayers(sourceId, targetId)
            _uploadNotification.value = "Players merged successfully"
        }
    }
}
