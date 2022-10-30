package com.zekierciyas.fancyfilterapp.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterSelectionNavModel(val uri: Uri?) : Parcelable