package pages.account

// OBJECT: store form data
object FormData {
    var name: String = ""
    var username: String = ""
    var email: String = ""
    var password: String = ""
}

// Form data for AccountSettings
class AccountSettingsFormData(
    var name: String,
    var username: String,
    var email: String,
    var educationLevel: String,
    var profilePictureId: String
) {
}

// Dropdown options for "Education Level"
val educationLevelOptions: List<String> = listOf("Elementary", "Secondary", "Post-Secondary", "Graduate", "Self-Taught")


