package com.example.ui.navigation

object CreaseDestinations {
    const val OVERVIEW = "overview"
    const val MATCH_CENTER = "match_center"
    const val SCORECARD = "scorecard?matchId={matchId}"
    const val PLAYERS = "players"
    const val TEAMS_TOURNAMENTS = "teams_tournaments"
    const val ANALYTICS_LAB = "analytics_lab"
    const val UPLOAD_REVIEW = "upload_review"
    const val REPORTS = "reports"
    const val INTEGRATIONS = "integrations"
    const val SETTINGS_AUDIT = "settings_audit"

    fun scorecardRoute(matchId: String? = null): String {
        return if (matchId != null) "scorecard?matchId=$matchId" else "scorecard"
    }
}
