package utils

// Map a picture ID to a file
fun getProfilePic(profilePicId: Int): String {
    return when (profilePicId) {
        0 -> "anya.jpg"
        1 -> "asa_mitaka.jpg"
        2 -> "boston_pizza.jpg"
        3 -> "cayde_6.jpg"
        4 -> "commander_psyduck.jpg"
        5 -> "saxophone_man.jpg"
        6 -> "sunset.jpg"
        7 -> "uwaterloo_mascot.jpg"
        else -> "pfp_tmp.svg"
    }
}

// Validate an email
fun validateEmail(email: String): Boolean {
    val pattern = Regex("\\S+@\\S+\\.\\S+\$")
    return pattern.matches(email)
}

// Validate a password
// Note: a password must have at least 8 characters, including a number, uppercase, and lowercase character
fun validatePassword(password: String): Boolean {
    val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$")
    return pattern.matches(password)
}
