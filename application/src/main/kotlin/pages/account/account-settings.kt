package pages.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoClient
import composables.*
import controllers.AccountController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import utils.DataModels.Account

@Composable
@Preview
fun accountSettings(changePage: (String) -> Unit, accountId: String) {
    val accountController: AccountController = AccountController()
    val account = accountController.getAccount(accountId) ?: return

    // Track error state
    var showErrorDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // Display account data, and store changes
    val accountData = AccountSettingsFormData(
        account.name,
        account.username,
        account.email,
        account.educationLevel,
        account.profilePicId
    )

    // Handle return to Landing page
    fun handleCancel() {
        changePage("Landing")
    }

    // Handle saving account changes
    fun handleSaveChanges() {

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
            account.password,
            accountData.educationLevel,
            accountData.profilePictureId
        )
        accountController.upsertAccount(payload)
        // TODO: show confirmation modal, show success message
        changePage("Landing")
    }

    // Handle updating a field value
    fun updateField(field: String, value: String) {
        when (field) {
            "Name" -> accountData.name = value
            "Username" -> accountData.username = value
            "Email" -> accountData.email = value
            "Education Level" -> accountData.educationLevel = value
            "Profile Picture" -> accountData.profilePictureId = value
        }
    }

    // Render UI
    Box(Modifier.fillMaxSize().padding(15.dp)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            // Avatar
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource("hamburger.png"),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(150.dp).width(150.dp).clip(CircleShape)
                )
            }

            // Account fields
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Name", true, ::updateField, accountData.name)
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Username", true, ::updateField, accountData.username)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    formField("Email", true, ::updateField, accountData.email)
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    dropdown("Education Level", educationLevelOptions, ::updateField, accountData.educationLevel)
                }
            }

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