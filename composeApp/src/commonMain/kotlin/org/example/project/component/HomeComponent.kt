package org.example.project.component

import com.arkivanov.decompose.ComponentContext

interface HomeComponent {
    fun navigateToSecond(text: String)
}

class HomeComponentImpl(
    private val onNavigateToSecond: (String) -> Unit,
    componentContext: ComponentContext
) : HomeComponent, ComponentContext by componentContext {
    override fun navigateToSecond(text: String) {
        onNavigateToSecond(text)
    }
}