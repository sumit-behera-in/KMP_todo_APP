import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import data.realm.RealmDb
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import presentation.screen.home.HomeScreen
import presentation.screen.home.HomeScreenViewModel
import presentation.theme.darkScheme
import presentation.theme.lightScheme

@Composable
@Preview
fun App() {
    initKoin()

    // Theme
    val colors by mutableStateOf(
        if (isSystemInDarkTheme()) darkScheme else lightScheme
    )

    // UI
    MaterialTheme(colorScheme = colors) {
        Navigator(HomeScreen()) {
            SlideTransition(it)
        }
    }
}

// Koin module

val realmModule = module {
    // singleton
    single { RealmDb() }
    // Viewmodel factory
    factory { HomeScreenViewModel(get()) }

}

fun initKoin() {
    startKoin {
        modules(realmModule)
    }

}