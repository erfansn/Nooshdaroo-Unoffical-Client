package ir.nooshdaroo

import com.google.common.truth.Correspondence
import ir.nooshdaroo.model.Category

val CategoryCorrespondence = Correspondence.from<Category, Category>(
    { actual, expected -> actual.url?.path == expected.url?.path && actual.title == expected.title },
    "contains"
)!!
