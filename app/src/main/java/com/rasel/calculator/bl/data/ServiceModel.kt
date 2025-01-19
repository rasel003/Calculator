package com.rasel.calculator.bl.data

import androidx.recyclerview.widget.DiffUtil

data class ServiceModel (
    val title: String,
    val webUrl: String
)
object LessonDiff : DiffUtil.ItemCallback<ServiceModel>() {
    override fun areItemsTheSame(oldItem: ServiceModel, newItem: ServiceModel) = oldItem.title == newItem.title
    override fun areContentsTheSame(oldItem: ServiceModel, newItem: ServiceModel) = oldItem == newItem
}