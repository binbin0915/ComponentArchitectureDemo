/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.model.home.model

import android.widget.TextView
import coil.load
import com.drake.brv.BindingAdapter
import com.drake.brv.item.ItemBind
import com.model.home.R
import com.model.home.databinding.HomeItemSimpleBinding

data class SimpleModel(var name: String, var url: String) : ItemBind {
    /**
     * 不推荐这种方式, 因为Model只应该存在数据和逻辑, 如果包含UI绑定会导致视图耦合不例如项目迭代 (例如BRVAH)
     */
    override fun onBind(holder: BindingAdapter.BindingViewHolder) {
        // 使用不同的方法来获取视图控件
        holder.findView<TextView>(R.id.tv_simple).text = name // 使用findById
        val viewBinding = HomeItemSimpleBinding.bind(holder.itemView) // 使用ViewBinding
        viewBinding.ivSimple.load(url) {

        }
        // val dataBinding = holder.getBinding<ItemMultiTypeOneBinding>() // 使用DataBinding
    }
}