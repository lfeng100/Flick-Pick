package ca.uwaterloo.flickpick.domain.repository

import android.util.Log
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Group
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.AddUserToGroup
import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

object GroupRepository {
    private val _groups = MutableStateFlow(emptyList<Group>())
    val groups = _groups.asStateFlow()

    private val groupCache: MutableMap<String, Group> = ConcurrentHashMap()

    init {
        fetchGroups()
    }

    fun addUserToGroup(userID: String, groupID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                DatabaseClient.apiService.addUserToGroup(AddUserToGroup(groupID, userID))
            } catch (e: Exception) {
                Log.e("GroupRepository", "Error adding user to group: ${userID}, $groupID ${e.message}")
            }
        }
    }

    private fun fetchGroups() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val groupList = DatabaseClient.apiService.getAllGroups().items
                _groups.value += groupList
                for (group in groupList) {
                    groupCache[group.groupID] = group
                }
            } catch (e: Exception) {
                Log.e("GroupRepository", "Error fetching groups: ${e.message}")
            }
        }
    }

    suspend fun getAllUsersInGroup(groupID: String): List<User> {
        return DatabaseClient.apiService.getGroupUsersById(groupID).items
    }
}
