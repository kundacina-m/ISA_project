package com.example.demo.dto


data class UserDTO(

    var id: Int? = null,
    var username: String? = null,
    var email: String? = null,
    var password: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var city: String? = null,
    var number: String? = null,
    var avioCompany: AvioCompanyDTO? = null,
    var authority: MutableList<AuthorityDTO>? = mutableListOf(),
    var isPassChanged: Boolean = false

)
