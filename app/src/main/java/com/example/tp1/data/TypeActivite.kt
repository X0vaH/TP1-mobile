package com.example.tp1.data

import androidx.annotation.StringRes
import com.example.tp1.R

enum class TypeActivite (@StringRes val labelRes: Int) {
    MUSCULATION(R.string.activite_musculation),
    COURSE(R.string.activite_course),
    VELO(R.string.activite_velo),
    NATATION(R.string.activite_natation),
    YOGA(R.string.activite_yoga),
    AUTRE(R.string.activite_autre)
}