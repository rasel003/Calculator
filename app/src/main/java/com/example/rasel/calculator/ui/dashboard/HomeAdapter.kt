package com.example.rasel.calculator.ui.dashboard
/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rasel.calculator.bl.data.LessonDiff
import com.example.rasel.calculator.bl.data.ServiceModel
import com.example.rasel.calculator.databinding.HomeItemBinding

class HomeAdapter(
    private val onClick: (ServiceModel) -> Unit
) : ListAdapter<ServiceModel, LessonViewHolder>(LessonDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = LessonViewHolder(binding)
        binding.root.setOnClickListener {
            onClick(getItem(holder.bindingAdapterPosition))
        }
        return holder
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class LessonViewHolder(
    private val binding: HomeItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(lesson: ServiceModel) {
        binding.run {
            this.lesson = lesson
            executePendingBindings()
        }
    }
}