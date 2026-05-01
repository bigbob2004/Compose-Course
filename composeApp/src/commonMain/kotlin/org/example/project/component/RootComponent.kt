package org.example.project.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import androidx.datastore.core.DataStore
import kotlinx.serialization.Serializable
import org.example.project.preferences.Preferences
import org.example.project.preferences.ThemeConfig
import org.example.project.model.ShoppingItem

interface HomeComponent {
    fun addItem(name: String)
    fun toggleItem(id: String)
    fun removeItem(id: String)
    fun navigateToSecond(message: String)
}

interface SettingsComponent {
    fun updateTheme(config: ThemeConfig)
    fun goBack()
}

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    val preferences: Flow<Preferences>

    sealed class Child {
        class Home(val component: HomeComponent) : Child()
        class Settings(val component: SettingsComponent) : Child()
    }
}

class RootComponentImpl(
    componentContext: ComponentContext,
    private val dataStore: DataStore<Preferences>
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Home,
            handleBackButton = true,
            childFactory = ::createChild
        )

    override val preferences: Flow<Preferences> = dataStore.data

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Home -> RootComponent.Child.Home(
                HomeComponentImpl(
                    dataStore = dataStore,
                    componentContext = componentContext,
                    onNavigateToSecond = { navigation.push(Config.Settings) }
                )
            )
            is Config.Settings -> RootComponent.Child.Settings(
                SettingsComponentImpl(
                    componentContext = componentContext,
                    dataStore = dataStore,
                    onGoBack = { navigation.pop() }
                )
            )
        }

    @Serializable
    private sealed class Config {
        @Serializable
        data object Home : Config()
        @Serializable
        data object Settings : Config()
    }
}

class HomeComponentImpl(
    private val dataStore: DataStore<Preferences>,
    componentContext: ComponentContext,
    private val onNavigateToSecond: () -> Unit
) : HomeComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun addItem(name: String) {
        scope.launch {
            try {
                dataStore.updateData { it.copy(shoppingList = it.shoppingList + ShoppingItem(
                    id = Clock.System.now().toEpochMilliseconds().toString(),
                    name = name,
                    isChecked = false
                )) }
                println("Успешно добавлено: $name")
            } catch (e: Exception) {
                println("Ошибка добавления: ${e.message}")
            }
        }
    }

    override fun toggleItem(id: String) {
        scope.launch {
            dataStore.updateData { pref ->
                pref.copy(shoppingList = pref.shoppingList.map {
                    if (it.id == id) it.copy(isChecked = !it.isChecked) else it
                })
            }
        }
    }

    override fun removeItem(id: String) {
        scope.launch {
            dataStore.updateData { pref ->
                pref.copy(shoppingList = pref.shoppingList.filter { it.id != id })
            }
        }
    }

    override fun navigateToSecond(message: String) {
        onNavigateToSecond()
    }
}

class SettingsComponentImpl(
    componentContext: ComponentContext,
    private val dataStore: DataStore<Preferences>,
    private val onGoBack: () -> Unit
) : SettingsComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun updateTheme(config: ThemeConfig) {
        scope.launch {
            dataStore.updateData { it.copy(theme = config) }
        }
    }

    override fun goBack() {
        onGoBack()
    }
}