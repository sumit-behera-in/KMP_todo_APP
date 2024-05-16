import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.screens.home.HomeScreen
import theme.darkScheme
import theme.lightScheme

@Composable
@Preview
fun App() {
    val colors by mutableStateOf(
        if (isSystemInDarkTheme()) darkScheme else lightScheme
    )
    MaterialTheme(colorScheme = colors) {
        Navigator(HomeScreen()){
            SlideTransition(it)
        }
    }
}