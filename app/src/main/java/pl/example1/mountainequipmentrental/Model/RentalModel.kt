package pl.example1.mountainequipmentrental.Model

data class RentalModel(val Name: String = "",
                       val Surname: String = "",
                       val telephone_number: String = "",
                       val dateFrom: String = "",
                       val dateTo: String = "",
                       var gearId: String = "",
                       val returned: Boolean = false,
                       val email: String = "")
