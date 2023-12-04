package pages.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import composables.errorDialog
import composables.formField
import composables.primaryButton
import composables.secondaryButton
import controllers.AccountController
import utils.DataModels.Account
import utils.DataModels.AccountFormData
import utils.getProfilePic
import utils.validateEmail
import utils.validatePassword

@Composable
@Preview
fun accountSettings(changePage: (String) -> Unit, accountId: String, profilePicId: Int) {
    val accountController = AccountController()
    val account = accountController.getAccount(accountId)

    // Track error state
    var showErrorDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // Display account data, and store changes
    val accountData = AccountFormData(
        account.name,
        account.username,
        account.email,
        "",
        ""
    )

    // Handle return to Landing page
    fun handleCancel() {
        changePage("Landing")
    }

    // Handle saving account changes
    fun handleSaveChanges() {

        // Check for valid name and username
        if (accountData.name == "" || accountData.username == "") {
            error = "Your name and username cannot be empty."
            showErrorDialog = true
            return
        }

        // Check for valid password change
        var newPassword = account.password
        val changedPassword = (accountData.password != "" || accountData.confirmPassword != "")
        if (changedPassword && (accountData.password != account.password)) {
            error = "The old password you entered is incorrect."
            showErrorDialog = true
            return
        } else if (changedPassword && !validatePassword(accountData.confirmPassword)) {
            error = "Your new password is not valid."
            showErrorDialog = true
            return
        } else if (changedPassword) {
            newPassword = accountData.confirmPassword
        }

        // Check for valid email
        if (!validateEmail(accountData.email)) {
            error = "The chosen email is not a valid email."
            showErrorDialog = true
            return
        }

        // Check for unique username and email
        val existingUsername = accountController.getAccountFromLogin(accountData.username)
        val existingEmail = accountController.getAccountFromLogin(accountData.email)
        if (
            (existingUsername != null && existingUsername._id != account._id) &&
            (existingEmail != null && existingEmail._id != account._id)
        ) {
            error = "An account with this username and this email exists."
            showErrorDialog = true
            return
        } else if (existingUsername != null && existingUsername._id != account._id) {
            error = "An account with this username exists."
            showErrorDialog = true
            return
        } else if (existingEmail != null && existingEmail._id != account._id) {
            error = "An account with this email exists."
            showErrorDialog = true
            return
        }

        // Save the account changes
        val payload = Account(
            account._id,
            accountData.name,
            accountData.username,
            accountData.email,
            newPassword
        )
        accountController.upsertAccount(payload)
        changePage("Landing")
    }

    // Handle updating a field value
    fun updateField(field: String, value: String) {
        when (field) {
            "Name" -> accountData.name = value
            "Username" -> accountData.username = value
            "Email" -> accountData.email = value
            "Old Password" -> accountData.password = value
            "New Password" -> accountData.confirmPassword = value
        }
    }

    // Render UI
    Box(Modifier.fillMaxSize().background(Color(0xFFCBC3E3))) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            // Avatar
            Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(getProfilePic(profilePicId)),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(150.dp).width(150.dp).clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Account fields
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Name", true, ::updateField, accountData.name)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Username", true, ::updateField, accountData.username)
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Email", true, ::updateField, accountData.email)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Old Password", true, ::updateField, accountData.password)
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("New Password", true, ::updateField, accountData.confirmPassword)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("Passwords must have at least 8 characters, including a number, uppercase, and lowercase character.")
            Spacer(modifier = Modifier.height(2.dp))
            Text("Note: only fill out the password fields if you wish to change your password.")
            Spacer(modifier = Modifier.height(24.dp))

            // Cancel and save buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.weight(1f).padding(10.dp), contentAlignment = Alignment.CenterEnd) {
                    secondaryButton("Cancel", ::handleCancel)
                }
                Box(modifier = Modifier.weight(1f).padding(10.dp), contentAlignment = Alignment.CenterStart) {
                    primaryButton("Save Changes", ::handleSaveChanges)
                }
            }
        }
    }

    // Render dialogs
    if (showErrorDialog) {
        errorDialog("Error Saving Changes", error) {
            showErrorDialog = false
            error = ""
        }
    }
}