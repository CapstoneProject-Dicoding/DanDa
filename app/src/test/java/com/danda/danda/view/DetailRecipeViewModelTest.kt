package com.danda.danda.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danda.danda.data.DataDummy
import com.danda.danda.data.MainCoroutinesRule
import com.danda.danda.data.getOrAwaitValue
import com.danda.danda.model.dataclass.Comment
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.model.repository.detail.DetailRepositoryImp
import com.danda.danda.ui.detailrecipe.DetailRecipeViewModel
import com.danda.danda.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import kotlin.math.exp

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class DetailRecipeViewModelTest {

    private lateinit var detailRepositoryImp: DetailRepositoryImp
    private lateinit var detailRecipeViewModel: DetailRecipeViewModel

    private val dataDummy = DataDummy.showDataDummyCommentResponse()
    private val nameRecipe = "name"

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @Before
    fun setup(){
        detailRepositoryImp = Mockito.mock(DetailRepositoryImp::class.java)
        detailRecipeViewModel = DetailRecipeViewModel(detailRepositoryImp)
    }

    @Test
    fun `Ketika mengambil list komentar berdasarkan nama resep`() = runTest {
        val expected = MutableLiveData<Result<List<Comment>>>()

        Mockito.`when`(detailRepositoryImp.commentListDetail(nameRecipe){
            expected.value = it
        }).thenReturn(Unit)

        detailRecipeViewModel.getCommentListDetail(nameRecipe)

        val actual = detailRecipeViewModel.listComment.getOrAwaitValue()

        Mockito.verify(detailRepositoryImp).commentListDetail(nameRecipe) {
            expected.value = it
        }

        Assert.assertNotNull(actual)

    }
}