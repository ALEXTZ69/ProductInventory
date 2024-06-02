package com.example.cart.ui.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cart.CartTopAppBar
import com.example.cart.R
import com.example.cart.data.Cart
import com.example.cart.ui.AppViewModelProvider
import com.example.cart.ui.navigation.NavigationDestination
import com.example.cart.ui.theme.CartTheme
import kotlinx.coroutines.launch

object DetailDetailsDestination : NavigationDestination {
    override val route = "detail_details"
    override val titleRes = R.string.detail_detail_title
    const val detailIdArg = "detailId"
    val routeWithArgs = "$route/{$detailIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailDetailsScreen(
    navigateToEditDetail: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CartTopAppBar(
                title = stringResource(DetailDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
             {
            }
        },
        modifier = modifier,
    ) { innerPadding ->
        DetailDetailsBody(
            detailDetailsUiState = uiState.value,
            onDelete = {
                coroutineScope.launch {
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun DetailDetailsBody(
    detailDetailsUiState: DetailDetailsUiState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        DetailDetails(
            detail = detailDetailsUiState.detailDetails.toDetail(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun DetailDetails(
    detail: Cart, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            DetailDetailsRow(
                labelResID = R.string.userid,
                detailDetail = detail.userId,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
            DetailDetailsRow(
                labelResID = R.string.detail,
                detailDetail = detail.name,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
            DetailDetailsRow(
                    labelResID = R.string.cartid,
            detailDetail = detail.cartId,
            modifier = Modifier.padding(
                horizontal = dimensionResource(
                    id = R.dimen
                        .padding_medium
                )
            )
            )
            DetailDetailsRow(
                labelResID = R.string.status,
                detailDetail = detail.status,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
            DetailDetailsRow(
                labelResID = R.string.total,
                detailDetail = detail.formatedTotal(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
        }

    }
}

@Composable
private fun DetailDetailsRow(
    @StringRes labelResID: Int, detailDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = detailDetail, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun DetailDetailsScreenPreview() {
    CartTheme {
        DetailDetailsBody(DetailDetailsUiState(
            outOfStock = true, detailDetails = DetailDetails(1, "Pen", "$1.00", "scholar util")
        ),
            onDelete = {})
    }
}
