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
import composables.button
import composables.dropdown
import controllers.AccountController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import utils.DataModels.Account

//import utils.DataModels.Account

@Composable
@Preview
fun accountSettings(changePage: (String) -> Unit, accountId: String) {
    val accountController: AccountController = AccountController()
    val account = accountController.getAccount(accountId) ?: return

    // Display account data, and store changes
    val accountData = AccountSettingsFormData(
        account.name,
        account.username,
        account.email,
        account.educationLevel,
        account.profilePicId
    )

    // Handle button presses
    fun handleCancel() {
        changePage("Landing")
    }
    fun handleSaveChanges() {
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

    // Render the UI
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
                    button("Cancel", false, ::handleCancel)
                }
                Box(modifier = Modifier.weight(1f).padding(10.dp), contentAlignment = Alignment.CenterStart) {
                    button("Save Changes", true, ::handleSaveChanges)
                }
            }
        }
    }

}