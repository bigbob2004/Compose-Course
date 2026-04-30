package org.example.project.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

interface RootComponent {
    val childStack: Value<ChildStack<Config, Child>>
    fun navigate(config: Config.MainScreen)

    sealed interface Child {
        class Home(val component: HomeComponent) : Child
        class About(val component: AboutComponent) : Child
        class Second(val component: SecondComponent) : Child
    }
}

@Serializable
sealed interface Config {
    @Serializable data object Home : Config, MainScreen
    @Serializable data object About : Config, MainScreen
    @Serializable data class Second(val param: String) : Config

    sealed interface MainScreen : Config
}

class RootComponentImpl(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<Config, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Config, context: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Home -> RootComponent.Child.Home(
                HomeComponentImpl({ navigation.pushNew(Config.Second(it)) }, context)
            )
            is Config.About -> RootComponent.Child.About(AboutComponentImpl(context))
            is Config.Second -> RootComponent.Child.Second(SecondComponentImpl(config.param, context))
        }

    override fun navigate(config: Config.MainScreen) {
        navigation.bringToFront(config)
    }
}