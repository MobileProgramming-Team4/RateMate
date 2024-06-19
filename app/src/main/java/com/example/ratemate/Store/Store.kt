package com.example.ratemate.Store

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratemate.R
import com.example.ratemate.common.CommonTopAppBar
import com.example.ratemate.data.PointTransaction
import com.example.ratemate.data.StoreItem
import com.example.ratemate.data.User
import com.example.ratemate.repository.PointTransactionRepository
import com.example.ratemate.repository.StoreItemRepository
import com.example.ratemate.repository.UserRepository
import com.example.ratemate.viewModel.PointTransactionViewModel
import com.example.ratemate.viewModel.PointTransactionViewModelFactory
import com.example.ratemate.viewModel.StoreItemViewModel
import com.example.ratemate.viewModel.StoreItemViewModelFactory
import com.example.ratemate.viewModel.UserViewModel
import com.example.ratemate.viewModel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import java.util.UUID

@Composable
fun StoreScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(UserRepository()))
    userViewModel.getUser(uid!!)
    val user by userViewModel.user.collectAsState(initial = null)

    Log.d("상점 화면", "유저 정보: $user")

    val storeItemViewModel: StoreItemViewModel = viewModel(
        factory = StoreItemViewModelFactory(
            StoreItemRepository()
        )
    )

    val goods by storeItemViewModel.storeItems.collectAsState(initial = emptyList())

    var showGoodsList by rememberSaveable { mutableStateOf(goods) }

    LaunchedEffect(key1 = goods) {
        showGoodsList = goods.sortedBy { it.itemName }
    }

    LaunchedEffect(key1 = Unit) {
        userViewModel.getUser(uid)
    }

    user?.let {

        var userPurchaseList by rememberSaveable {
            if (user != null) {
                mutableStateOf(user!!.PurchaseList)
            } else {
                mutableStateOf(emptyList())
            }
        }

        var showPurchased by rememberSaveable { mutableStateOf(false) }
        var points by rememberSaveable {
            if (user != null) {
                mutableIntStateOf(user!!.points)
            } else {
                mutableIntStateOf(0)
            }
        }
        val context = LocalContext.current


        var addTrans by rememberSaveable { mutableStateOf(false) }
        var addItem by rememberSaveable { mutableStateOf("") }
        var addItemName by rememberSaveable { mutableStateOf("") }
        //구매 버튼 클릭 시
        val clickBuy: (String) -> Unit = { itemId ->
            val item = showGoodsList.find { it.itemId == itemId }
            if (item == null) {
                Log.d("상점 화면", "상품을 찾을 수 없음")
                Toast.makeText(context, "상품을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()

            } else if (points < item.cost) {
                Log.d("상점 화면", "id: ${item.itemId} 구매 실패")
                Toast.makeText(context, "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "${item.itemName}을 구매하였습니다.", Toast.LENGTH_SHORT).show()
                points -= item.cost
                userPurchaseList = userPurchaseList + item
                user!!.PurchaseList = userPurchaseList
                addTrans = true
                addItem = item.itemId
                addItemName = item.itemName
            }
        }

        if (addTrans) {
            addTrans = false
            val transaction = PointTransaction(
                userId = uid,
                amount = -showGoodsList.find { it.itemId == addItem }!!.cost,
                transactionType = "${addItemName} 상품 구매",
                transactionDate = Date().toString(),
                itemId = addItem
            )

            var pointTransactionViewModel: PointTransactionViewModel = viewModel(
                factory = PointTransactionViewModelFactory(
                    PointTransactionRepository()
                )
            )

            pointTransactionViewModel.addPointTransaction(transaction)
            userViewModel.updateUser(uid, mapOf("PurchaseList" to userPurchaseList))
            userViewModel.updateUser(uid, mapOf("points" to points))
            addItem = ""
            addItemName = ""
        }

//        LaunchedEffect(key1 = userPurchaseList) {
//            userViewModel.updateUser(uid, mapOf("PurchaseList" to userPurchaseList))
//        }
//
//        LaunchedEffect(key1 = points) {
//            userViewModel.updateUser(uid, mapOf("points" to points))
//        }


        Scaffold(
            topBar = {
                CommonTopAppBar(
                    title = "Point Store",
                    onNavigateBack = { },
                    false
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                //유저정보
                val modifier1 = Modifier.weight(50f)
                StoreUserInfo(user = user!!, points = points, modifier = modifier1)

                //체크박스
                Divider()
                val modifier2 = Modifier.height(50.dp)
                StoreCheckBox(
                    showPurchased,
                    modifier = modifier2,
                    clickCheckBox = { showPurchased = !showPurchased })

                //상품 리스트
                val modifier3 = Modifier.weight(50f)
                StoreGoodsList(
                    showGoodsList,
                    userPurchaseList,
                    showPurchased,
                    modifier = modifier3,
                    clickBuy = clickBuy
                )

            }
        }

    }
}


@Composable
fun getExampleGoodsList(): List<StoreItem> {
    val goodsList = mutableListOf<StoreItem>()

    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item1", 100, "item1"))
    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item2", 200, "item2"))
    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item3", 300, "item3"))
    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item4", 400, "item4"))
    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item5", 500, "item5"))

    return goodsList
}


@Composable
fun StoreUserInfo(user: User, points: Int, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.storebackground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .height(300.dp)
                .alpha(0.7f)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            Image(
                painter = painterResource(id = user.profileImage.toInt()),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${user.email}님", style = MaterialTheme.typography.h6)
                Text(text = "잔여 포인트: $points", style = MaterialTheme.typography.body1)
            }

        }
    }

}

@Composable
fun StoreCheckBox(showPurchased: Boolean, modifier: Modifier, clickCheckBox: () -> Unit) {
    Row(
        modifier = modifier
            .background(Color.Gray.copy(alpha = 0.1f))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = showPurchased,
            onCheckedChange = { clickCheckBox() },
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(R.color.main_blue)
            )

        )
        Text(
            text = "구매한 상품 포함",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 16.dp),
        )
    }
}

@Composable
fun StoreGoodsList(
    goodsList: List<StoreItem>, purchasedList: List<StoreItem>,
    showPurchased: Boolean, modifier: Modifier, clickBuy: (String) -> Unit
) {

    var showGoodsList by rememberSaveable { mutableStateOf(goodsList) }

    showGoodsList = if (showPurchased) {
        goodsList
    } else {
        goodsList.filter { !purchasedList.contains(it) }
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(showGoodsList) {
            ShowGoods(goods = it, isPurchased = purchasedList.contains(it), clickBuy = clickBuy)
            Divider()
        }
    }
}

@Composable
fun ShowGoods(goods: StoreItem, isPurchased: Boolean, clickBuy: (String) -> Unit) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isPurchased) {
                    Toast
                        .makeText(context, "이미 구매한 상품입니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    showDialog = true
                }
            }
            .background(if (isPurchased) Color.Gray.copy(alpha = 0.8f) else Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(
                        id = context.resources.getIdentifier(
                            goods.itemName,
                            "drawable",
                            context.packageName
                        )
                    ),
                    contentScale = ContentScale.Crop,
                    contentDescription = goods.itemName,
                    modifier = Modifier.size(60.dp),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(text = goods.itemName, style = MaterialTheme.typography.h6)
                    Text(text = goods.description, style = MaterialTheme.typography.body1)
                }
            }
            Text(text = "${goods.cost} 포인트", style = MaterialTheme.typography.body2)
        }

    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "구매 확인") },
            text = { Text(text = "${goods.itemName}을 구매하시겠습니까?") },
            confirmButton = {
                Button(
                    onClick = {
                        clickBuy(goods.itemId)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.main_blue)
                    )
                ) {
                    Text("확인", color = colorResource(R.color.white))
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.gray_500)
                    )
                ) {
                    Text("취소", color = colorResource(R.color.white))
                }
            }


        )
    }
}