package org.d3if0070.bookingtable.navigation

import org.d3if0070.bookingtable.ui.screen.KEY_ID_TABLE

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_TABLE}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}