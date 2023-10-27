package com.example.baeguard

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.baseline_home_24, "Home")
    object Perfil : NavigationItem("perfil", R.drawable.baseline_person_24, "Perfil")
    object Historico : NavigationItem("historico", R.drawable.baseline_history_24, "Historico")
    object Ed_senha : NavigationItem("ed_senha", R.drawable.baseline_edsenha_24, "Ed_senha")
    object Ed_email : NavigationItem("ed_email", R.drawable.baseline_edemail_24, "Ed_email")
    object Ed_Nome : NavigationItem("ed_nome", R.drawable.baseline_ednome_24, "Ed_Nome")
    object Add_dispositivo : NavigationItem("add_dispo", R.drawable.baseline_adddisp_24, "Add_dispositivo")
    data class Add_Qr(val infoQR:String) : NavigationItem("add_qr/{infoQR}", R.drawable.baseline_addqr_24, "Add_Qr")
    data class Disp_Dt(val devideId:String) : NavigationItem("disp_dt/{deviceId}", R.drawable.baseline_disp_dt_24, "Disp_Dt")
}