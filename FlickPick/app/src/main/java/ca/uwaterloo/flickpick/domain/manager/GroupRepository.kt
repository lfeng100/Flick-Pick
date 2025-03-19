import android.util.Log
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Group
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

object GroupRepository {
    private val _groups = MutableStateFlow(emptyList<Group>())
    val groups = _groups.asStateFlow()

    private val _isFetching = MutableStateFlow(false)
    val isFetching = _isFetching.asStateFlow()

    private val groupCache: MutableMap<String, Group> = ConcurrentHashMap()

    init {
        fetchGroups()
    }
    fun fetchGroups() {
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
}
