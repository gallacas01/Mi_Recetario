package com.miguelgallardocastillo.proyectoprimertrimestre.model

data class RemoteResult(
    val _links: Links,
    val count: Int,
    val from: Int,
    val hits: List<Hit>,
    val to: Int
)