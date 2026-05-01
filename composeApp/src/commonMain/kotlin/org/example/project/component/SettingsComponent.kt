package org.example.project.component

import com.arkivanov.decompose.ComponentContext

interface SecondComponent {
    val param: String
}

class SecondComponentImpl(
    override val param: String,
    componentContext: ComponentContext
) : SecondComponent, ComponentContext by componentContext