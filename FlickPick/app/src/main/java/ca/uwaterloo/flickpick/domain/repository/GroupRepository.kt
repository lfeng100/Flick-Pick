package ca.uwaterloo.flickpick.domain.repository

import android.util.Log
import ca.uwaterloo.flickpick.data.database.model.Group
import ca.uwaterloo.flickpick.data.database.DatabaseClient
import ca.uwaterloo.flickpick.data.database.model.AddUserToGroup
import ca.uwaterloo.flickpick.data.database.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

object GroupRepository {
    private val _joinGroups = MutableStateFlow(emptyList<Group>())
    private val _yourGroups = MutableStateFlow(emptyList<Group>())
    val joinGroups = _joinGroups.asStateFlow()
    val yourGroups = _yourGroups.asStateFlow()

    private val joinGroupCache: MutableMap<String, Group> = ConcurrentHashMap()
    private val yourGroupCache: MutableMap<String, Group> = ConcurrentHashMap()

    fun addUserToGroup(userID: String, groupID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                DatabaseClient.apiService.addUserToGroup(AddUserToGroup(groupID, userID))
            } catch (e: Exception) {
                Log.e("GroupRepository", "Error adding user to group: ${userID}, $groupID ${e.message}")
            }
        }
    }

    fun fetchYourGroups(userID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val groupList = DatabaseClient.apiService.getAllGroups().items
                val yourGroupList = mutableListOf<Group>()

                groupList.forEach { group ->
                    val usersInGroup = DatabaseClient.apiService.getGroupUsersById(group.groupID).items

                    if (userID in usersInGroup.map { it.userID }) {
                        yourGroupList.add(group)
                        yourGroupCache[group.groupID] = group
                    }
                }
                _yourGroups.value = yourGroupList

            } catch (e: Exception) {
                Log.e("GroupRepository", "Error fetching joinable groups: ${e.message}")
            }
        }
    }

    fun fetchJoinGroups(userID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val groupList = DatabaseClient.apiService.getAllGroups().items
                val joinGroupList = mutableListOf<Group>()

                groupList.forEach { group ->
                    val usersInGroup = DatabaseClient.apiService.getGroupUsersById(group.groupID).items

                    if (userID !in usersInGroup.map { it.userID }) {
                        joinGroupList.add(group)
                        joinGroupCache[group.groupID] = group
                    }
                }
                _joinGroups.value = joinGroupList

            } catch (e: Exception) {
                Log.e("GroupRepository", "Error fetching joinable groups: ${e.message}")
            }
        }
    }

    suspend fun getAllUsersInGroup(groupID: String): List<User> {
        return DatabaseClient.apiService.getGroupUsersById(groupID).items
    }
}
