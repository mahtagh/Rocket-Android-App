import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neofinancial.neo.android.interview.models.Launch
import com.neofinancial.neo.android.interview.network.LaunchesDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LaunchListViewModel : ViewModel() {

    private val _launches = MutableStateFlow<List<Launch>>(emptyList())
    val launches: StateFlow<List<Launch>> = _launches

    init {
        viewModelScope.launch {
            try {
                _launches.value = LaunchesDomain.getLaunches()
            } catch (e: Exception) {

            }
        }
    }
}
