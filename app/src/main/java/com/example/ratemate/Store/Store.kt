package com.example.ratemate.Store

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ratemate.R
import com.example.ratemate.data.StoreItem
import com.example.ratemate.data.User
import com.example.ratemate.home.getExampleUser
import java.util.UUID

@Composable
fun StoreScreen(navController: NavController) {
    val user = getExampleUser()
    val goods = getExampleGoodsList()
    user.PurchaseList = listOf(goods[0], goods[2])


    val showGoodsList by rememberSaveable { mutableStateOf(goods) }
    var userPurchaseList by rememberSaveable { mutableStateOf(user.PurchaseList) }
    var showPurchased by rememberSaveable { mutableStateOf(false) }
    var points by rememberSaveable { mutableIntStateOf(user.points) }
    val context = LocalContext.current

    //구매 버튼 클릭 시
    val clickBuy: (String) -> Unit = { itemId ->
        val item = showGoodsList.find { it.itemId == itemId }
        if (item == null) {
            Log.d("상점 화면", "상품을 찾을 수 없음")
            Toast.makeText(context, "상품을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()

        }
        else if (points < item.cost) {
            Log.d("상점 화면", "id: ${item.itemId} 구매 실패")
            Toast.makeText(context, "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
        } else {
            points -= item.cost
            userPurchaseList = userPurchaseList + item
            user.PurchaseList = userPurchaseList

        }
    }




    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        //유저정보
        val modifier1 = Modifier.weight(50f)
        StoreUserInfo(user = user, points = points ,modifier = modifier1)

        //체크박스
        Divider()
        val modifier2 = Modifier.height(50.dp)
        StoreCheckBox(showPurchased, modifier = modifier2, clickCheckBox = { showPurchased = !showPurchased })

        //상품 리스트
        val modifier3 = Modifier.weight(50f)
        StoreGoodsList(showGoodsList, userPurchaseList ,showPurchased, modifier = modifier3, clickBuy = clickBuy)

    }
}



@Composable
fun getExampleGoodsList() : List<StoreItem> {
    val goodsList = mutableListOf<StoreItem>()

    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item1", 100, "item1"))
    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item2", 200, "item2"))
    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item3", 300, "item3"))
    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item4", 400, "item4"))
    goodsList.add(StoreItem(UUID.randomUUID().toString(), "item5", 500, "item5"))

    return goodsList
}


@Composable
fun StoreUserInfo(user: User, points : Int , modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ){
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
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray, shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Column (
                modifier =
                Modifier
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(8.dp)
                    .clip(MaterialTheme.shapes.medium)
            ){
                Text(text = user.email, style = MaterialTheme.typography.h6)
                Text(text = "잔여 포인트: $points", style = MaterialTheme.typography.body1)
            }

        }
    }

}

@Composable
fun StoreCheckBox( showPurchased : Boolean ,modifier : Modifier, clickCheckBox : () -> Unit){
    Row (
        modifier = modifier
            .background(Color.Gray.copy(alpha = 0.1f))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Checkbox(
            checked = showPurchased,
            onCheckedChange = {clickCheckBox() },
            modifier = Modifier.padding(start = 16.dp, end = 8.dp)
        )
        Text(
            text = "구매한 상품 포함",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(end = 16.dp),
        )
    }
}

@Composable
fun StoreGoodsList(goodsList: List<StoreItem>, purchasedList: List<StoreItem>,
                   showPurchased: Boolean, modifier: Modifier, clickBuy: (String) -> Unit
){

    var showGoodsList by rememberSaveable { mutableStateOf(goodsList) }

    showGoodsList = if (showPurchased) {
        goodsList
    } else {
        goodsList.filter { !purchasedList.contains(it) }
    }

    LazyColumn(
        modifier = modifier
    ){
        items(showGoodsList){
            ShowGoods(goods = it, isPurchased = purchasedList.contains(it), clickBuy = clickBuy)
            Divider()
        }
    }
}

@Composable
fun ShowGoods(goods: StoreItem, isPurchased: Boolean, clickBuy: (String) -> Unit){
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
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ){
            Text(text = goods.itemName, style = MaterialTheme.typography.h6)
            Text(text = goods.description, style = MaterialTheme.typography.body1)
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
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("취소")
                }
            }


        )
    }
}