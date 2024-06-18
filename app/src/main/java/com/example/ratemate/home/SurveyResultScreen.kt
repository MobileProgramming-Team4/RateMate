package com.example.ratemate.home

import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbDownOffAlt
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratemate.R
import com.example.ratemate.common.CommonTopAppBar
import com.example.ratemate.data.Comment
import com.example.ratemate.data.Dislike
import com.example.ratemate.data.Like
import com.example.ratemate.data.PointTransaction
import com.example.ratemate.data.QnA
import com.example.ratemate.data.Response
import com.example.ratemate.data.SurveyV2
import com.example.ratemate.data.User
import com.example.ratemate.repository.PointTransactionRepository
import com.example.ratemate.repository.SurveyV2Repository
import com.example.ratemate.repository.UserRepository
import com.example.ratemate.viewModel.PointTransactionViewModel
import com.example.ratemate.viewModel.PointTransactionViewModelFactory
import com.example.ratemate.viewModel.SurveyV2ViewModel
import com.example.ratemate.viewModel.SurveyV2ViewModelFactory
import com.example.ratemate.viewModel.UserViewModel
import com.example.ratemate.viewModel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.Date
import java.util.UUID

@Composable
fun SurveyResultScreen(SurveyID: String?, navController: NavController){
    Log.d("설문결과 화면", "SurveyID : $SurveyID")

    //유저 정보 가져오기
    val auth = FirebaseAuth.getInstance()
    val userUid = auth.currentUser?.uid
    val userViewModel : UserViewModel = viewModel (factory = UserViewModelFactory(UserRepository()))
    userViewModel.getUser(userUid!!)
    val user by userViewModel.user.collectAsState(initial = null)


    //설문 정보 가져오기
    val surveyId = SurveyID //전달 받아야 하는 값
    val surveyV2ViewModel : SurveyV2ViewModel = viewModel (factory = SurveyV2ViewModelFactory(
        SurveyV2Repository()
    ))
    if (surveyId != null) {
        surveyV2ViewModel.getSurvey(surveyId)
    }

    val surveyResult by surveyV2ViewModel.survey.collectAsState(initial = null)

    //테스트용 -> 예시 설문 추가
//    var addSurvey by remember { mutableStateOf(false) }
//    LaunchedEffect(Unit) {
//        Log.d("설문 추가", "LaunchedEffect 실행")
//        addSurvey = true
//    }
//    if(addSurvey){
//        addSurvey = false
//        Log.d("설문 추가", "if 문 실행")
//        AddExampleSurveyV2()
//    }


    //유저 정보와 설문 정보가 모두 로드되면 화면 출력
    if (user != null && surveyResult != null) {
        Log.d("설문결과 화면", "user : $user")
        Log.d("설문결과 화면", "surveyResult : $surveyResult")
        ShowSurveyResultScreen(user!!, surveyResult!!, navController)
    }
    else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Loading...")
        }
    }
}

@Composable
fun ShowSurveyResultScreen(user: User, Result : SurveyV2, navController: NavController) {


    val surveyV2ViewModel : SurveyV2ViewModel = viewModel (factory = SurveyV2ViewModelFactory(
        SurveyV2Repository()
    ))

    surveyV2ViewModel.getSurvey(Result.surveyId)
    val surveyResult by surveyV2ViewModel.survey.collectAsState(initial = Result)


    surveyResult?.let{
        val content = surveyResult!!.qnA
        val userChoice = surveyResult!!.response.find { it.userId == user.userId }?.answer ?: listOf()

        var commentList by remember { mutableStateOf(surveyResult!!.comments.toMutableList()) }
        var sortComment by remember { mutableStateOf("인기순") }
        var likes by remember { mutableIntStateOf(surveyResult!!.likes.count) }
        var numberOfComment by remember { mutableIntStateOf(surveyResult!!.comments.size) }
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        val isMySurvey = user.email == surveyResult!!.creatorId
        var showDialog by remember { mutableStateOf(false) }


        val userViewModel : UserViewModel = viewModel (factory = UserViewModelFactory(UserRepository()))

        var newComment by remember { mutableStateOf("") }

        val commentPoint = 5


        //해당 유저가 좋아요를 눌렀는지 확인
        var isLiked by remember {
            mutableStateOf(surveyResult!!.likes.usersWhoLiked.find { it == user.userId } != null)
        }

        //섬문의 좋아요를 눌렀을때 실행될 함수
        val clickLikes = {
            if (isLiked) {
                isLiked = false
                likes -= 1
            } else {
                isLiked = true
                likes += 1
            }
        }


        var isclickedSort by remember { mutableStateOf(false) }
        //인기순 버튼 클릭시 실행될 함수
        val clickSortByLikes = {
            sortComment = "인기순"
            isclickedSort = true

        }

        //최신순 버튼 클릭시 실행될 함수
        val clickSortByDate = {
            sortComment = "최신순"
            isclickedSort = true
        }

        if (isclickedSort) {
            surveyV2ViewModel.getSurvey(Result.surveyId)
            val isSurveyLoaded by surveyV2ViewModel.isSurveyLoaded.collectAsState()
            if (isSurveyLoaded) {
                isclickedSort = false
                if (sortComment == "인기순") {
                    try {
                        val commentsCopy = surveyResult!!.comments.toList()
                        commentList =
                            commentsCopy.sortedByDescending { it.like.count }.toMutableList()
                    } catch (e: Exception) {
                        Toast.makeText(context, "정렬 에러", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    try {
                        val commentsCopy = surveyResult!!.comments.toList()
                        commentList =
                            commentsCopy.sortedByDescending { Date(it.createdDate) }.toMutableList()
                    } catch (e: Exception) {
                        Toast.makeText(context, "정렬 에러", Toast.LENGTH_SHORT).show()
                    }
                }
                numberOfComment = surveyResult!!.comments.size
                surveyV2ViewModel.updateSurvey(surveyResult!!.surveyId, mapOf("numOfComments" to numberOfComment))

            }
        }




        var addComment by remember { mutableStateOf(false) }
        //댓글 입력 버튼 클릭시 실행될 함수
        val onClickSend = @Composable { comment: String ->
            if (comment == "") {
                Toast.makeText(context, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (newComment != ""){
                Toast.makeText(context, "잠시 후 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
            }

            else {
                newComment = comment
                addComment = true
                Toast.makeText(context, "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show()
            }
        }

        //댓글 추가
        if (addComment) {
            if (newComment != "") {
                surveyV2ViewModel.getSurvey(Result.surveyId)
                val isSurveyLoaded by surveyV2ViewModel.isSurveyLoaded.collectAsState()
                if (isSurveyLoaded) {
                    addComment = false
                    if (newComment != "") {

                        Log.d("댓글", "댓글 추가 : $newComment")

                        val add = Comment(
                            commentId = UUID.randomUUID().toString(),
                            userId = user.email,
                            text = newComment,
                            createdDate = Date().toString(),
                            profileImage = user.profileImage.toString(),
                            like = Like(),
                            dislike = Dislike()
                        )

                        //댓글 추가시 유저 포인트 증가
                        val auth = FirebaseAuth.getInstance()
                        val userUid = auth.currentUser?.uid
                        userViewModel.updateUser(userUid!!, mapOf( "points" to user.points + commentPoint))

                        val pointTransactionViewModel : PointTransactionViewModel = viewModel(factory = PointTransactionViewModelFactory(
                            PointTransactionRepository()
                        )
                        )

                        pointTransactionViewModel.addPointTransaction(
                            PointTransaction(
                                transactionId = "",
                                userId = user.userId,
                                amount = commentPoint,
                                transactionType = "댓글 작성",
                                transactionDate = Date().toString()
                            )
                        )
                        //댓글 추가
                        val newCommentList = surveyResult!!.comments.toMutableList()
                        newCommentList.add(add)
                        surveyV2ViewModel.updateSurvey(surveyResult!!.surveyId, mapOf("comments" to newCommentList))
                        numberOfComment = surveyResult!!.comments.size
                        surveyV2ViewModel.updateSurvey(surveyResult!!.surveyId, mapOf("numOfComments" to numberOfComment))
                        commentList = surveyResult!!.comments.toMutableList()

                        if (sortComment == "인기순") {
                            clickSortByLikes()
                        } else {
                            clickSortByDate()
                        }

                        newComment = ""
                    }
                }
            }

        }

        val editSurvey = {
            /*Todo*/
            Toast.makeText(context, "설문 수정", Toast.LENGTH_SHORT).show()
        }

        val removeSurvey = {
            if (user.email != surveyResult!!.creatorId) {
                Toast.makeText(context, "에러 : 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            } else{
                showDialog = true

            }
        }

        //설문 삭제
        if(showDialog){
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("설문 삭제") },
                text = { Text("설문을 삭제하시겠습니까?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            surveyV2ViewModel.deleteSurvey(surveyResult!!.surveyId)
                            val userCreated = user.surveysCreated.toMutableList()
                            userCreated.remove(surveyResult!!.surveyId)
                            userViewModel.updateUser(user.userId, mapOf("surveysCreated" to userCreated))
                            Toast.makeText(context, "설문 삭제", Toast.LENGTH_SHORT).show()

                            navController.popBackStack()
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


        //좋아요, 싫어요 버튼 클릭시 실행될 함수
        var showLog = false
        val onClickLikeDislike = {
            showLog = true

        }
        if (showLog) {
            showLog = false
            Log.d("댓글", "리스트 : $commentList")
        }


        //좋아요 눌렀을 때 동기화
        var changedLike by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = likes, key2 = isLiked) {
            changedLike = true
            Log.d("좋아요", "좋아요 : $likes")
            Log.d("좋아요", "좋아요 누른 유저 : ${surveyResult!!.likes.usersWhoLiked}")

        }

        if (changedLike) {
            surveyV2ViewModel.getSurvey(Result.surveyId)
            val isSurveyLoaded by surveyV2ViewModel.isSurveyLoaded.collectAsState()
            if (isSurveyLoaded) {
                changedLike = false
                if (isLiked && surveyResult!!.likes.usersWhoLiked.find { it == user.userId } == null) {
                    var userWhoLiked = surveyResult!!.likes.usersWhoLiked.toMutableList()
                    userWhoLiked.add(user.userId)
                    surveyV2ViewModel.updateSurvey(surveyResult!!.surveyId, mapOf("likes" to Like(
                        surveyResult!!.likes.count+1, userWhoLiked)))
                    likes = surveyResult!!.likes.count +1


                } else if(!isLiked && surveyResult!!.likes.usersWhoLiked.find { it == user.userId } != null) {
                    var userWhoLiked = surveyResult!!.likes.usersWhoLiked.toMutableList()
                    userWhoLiked.remove(user.userId)
                    surveyV2ViewModel.updateSurvey(surveyResult!!.surveyId, mapOf("likes" to Like(
                        surveyResult!!.likes.count-1, userWhoLiked)))

                    likes = surveyResult!!.likes.count -1

                }

            }

        }


        //기본값 -> 인기순 정렬
        LaunchedEffect(Unit) {
            clickSortByLikes()
        }

        Scaffold(
            topBar = {
                CommonTopAppBar(title = "결과", onNavigateBack = { navController.popBackStack() })
            }
        ) { paddingValues ->
            //전체화면 Column
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .clickable(
                        onClick = { focusManager.clearFocus() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),

                ) {


                //제목, 작성자
                val modifier1 = Modifier.height(40.dp)
                ShowTitle(
                    title = surveyResult!!.title,
                    writer = surveyResult!!.creatorId,
                    isMySurvey = isMySurvey,
                    modifier = modifier1,
                    editSurvey = editSurvey,
                    removeSurvey = removeSurvey
                )

                //내용
                val modifier2 = Modifier.weight(7f)
                ShowMainContent(content = content, userChoice = userChoice, modifier = modifier2)

                //좋아요, 댓글 수, 댓글 정렬 방법 버튼
                val modifier3 = Modifier.height(40.dp)
                ShowCounts(
                    isLiked = isLiked, like = likes,
                    numberOfComment = numberOfComment, modifier = modifier3,
                    clickLikes = clickLikes, clickSortByLikes = clickSortByLikes,
                    clickSortByDate = clickSortByDate
                )

                //댓글 리스트
                Divider()
                val modifier5 = Modifier.weight(5f)
                ShowComments(
                    user, comments = commentList, surveyResult = surveyResult!!,
                    modifier = modifier5
                )

                //댓글 입력창
                val modifier6 = Modifier.height(70.dp)
                ShowCommentInput(modifier = modifier6, onClickSend = onClickSend)


            }
        }
    }

}

@Composable
fun AddExampleSurveyV2(){
    Log.d("설문 추가", "설문 추가 시작")
    val calendar1 = Calendar.getInstance()
    calendar1.set(2022, 10, 1)
    val commentEX1 = Comment(
        userId = "user1@gmail.com",
        text = "내용1",
        createdDate = calendar1.time.toString(),
        profileImage = R.drawable.image1.toString(),
        like = Like(),
        dislike = Dislike()
    )
    commentEX1.like.usersWhoLiked = mutableListOf("1", "2", "3", "4", "5")
    commentEX1.dislike.usersWhoDisliked = mutableListOf("1", "2", "3")
    commentEX1.like.count = 5
    commentEX1.dislike.count = 3

    val calendar2 = Calendar.getInstance()
    calendar2.set(2023, 10, 2)
    val commentEX2 = Comment(
        userId = "user2@gmail.com",
        text = "내용2",
        createdDate = calendar2.time.toString(),
        profileImage = R.drawable.image2.toString(),
        like = Like(),
        dislike = Dislike()
    )

    commentEX2.like.usersWhoLiked = mutableListOf("1", "2", "3", "4", "5")
    commentEX2.dislike.usersWhoDisliked = mutableListOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    commentEX2.like.count = 5
    commentEX2.dislike.count = 10

    val SurveyV2 = SurveyV2(
        creatorId = "example@gmail.com",
        title = "제목",
        content = "내용",
        likes = Like(
            count = 5,
            usersWhoLiked = mutableListOf("1", "2", "3", "4", "5")
        ),
        numOfComments = 2,
        createdDate = Date().toString(),
        modifiedDate = Date().toString(),
        status = "active",
        qnA = mutableListOf(),
        response = mutableListOf(),
        comments = mutableListOf( commentEX1, commentEX2)

    )

    val qnA1 = QnA(
        order = 0,
        question = "질문1",
        answerList = mutableListOf("답변1", "답변2", "답변3"),
        answerCountList = mutableListOf(10, 20, 30),
        questionType = "single"
    )

    val qnA2 = QnA(
        order = 1,
        question = "질문2",
        answerList = mutableListOf("답변2_1", "답변2_2", "답변2_3", "답변2_4"),
        answerCountList = mutableListOf(30, 10, 40, 50),
        questionType = "single"
    )

    val qnA3 = QnA(
        order = 2,
        question = "질문3",
        answerList = mutableListOf("답변3_1", "답변3_2", "답변3_3", "답변3_4", "답변3_5"),
        answerCountList = mutableListOf(100, 20, 30, 0, 50),
        questionType = "multiple"
    )

    val qnA4 = QnA(
        order = 3,
        question = "질문4",
        answerList = mutableListOf("답변4_1", "답변4_2"),
        answerCountList = mutableListOf(30, 20),
        questionType = "single"
    )

    SurveyV2.qnA.add(qnA1)
    SurveyV2.qnA.add(qnA2)
    SurveyV2.qnA.add(qnA3)
    SurveyV2.qnA.add(qnA4)


    val Response1 = Response(
        userId = "1",
        answer = mutableListOf(listOf(0), listOf(1), listOf(1, 3), listOf(1))

        )

    val Response2 = Response(
        userId = "2",
        answer = mutableListOf(listOf(1), listOf(0), listOf(2, 3), listOf(0))

        )

    val Response3 = Response(
        userId = "8ByCzSY8UqRTqfViU3luhJdPZKB2",
        answer = mutableListOf(listOf(2), listOf(1), listOf(2, 3), listOf(1))

        )

    SurveyV2.response.add(Response1)
    SurveyV2.response.add(Response2)
    SurveyV2.response.add(Response3)


    val surveyV2ViewModel : SurveyV2ViewModel = viewModel (factory = SurveyV2ViewModelFactory(
        SurveyV2Repository()
    ))
    surveyV2ViewModel.addSurvey(SurveyV2)

}

@Composable
fun ShowTitle(title: String, writer: String, isMySurvey : Boolean
              ,modifier: Modifier, editSurvey : () -> Unit,
              removeSurvey : () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    var iconSize by remember { mutableStateOf(Size.Zero) }
    var offsetSize by remember { mutableStateOf(Size.Zero) }

    Row(modifier = modifier
        .fillMaxWidth()
        .onGloballyPositioned { layoutCoordinates ->
            rowSize = layoutCoordinates.size.toSize()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {

        //제목
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        //작성자
        Text(
            text = if (isMySurvey) "내 설문" else writer,
            fontSize = 15.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)
        )

        if(isMySurvey){

            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "edit",
                modifier = Modifier
                    .size(height = 40.dp, width = 20.dp)
                    .clickable { expanded = true }
                    .onGloballyPositioned { layoutCoordinates ->
                        iconSize = layoutCoordinates.size.toSize()
                    }
            )

            offsetSize = Size(rowSize.width - iconSize.width, 0F)

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                offset = DpOffset(offsetSize.width.dp, 0.dp)
            ){
//                DropdownMenuItem(text = { Text("수정") }, onClick = { editSurvey(); expanded = false })
                DropdownMenuItem(text = { Text("삭제") }, onClick = { removeSurvey(); expanded = false})
            }


        }

    }
}

@Composable
fun ShowMainContent(content : List<QnA>, userChoice : List<List<Int>>, modifier : Modifier){

    val contentSize = content.size
    var currentContent by rememberSaveable { mutableIntStateOf(0) }

    Box(modifier = modifier){
        //회색 배경
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.5f))
            .padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)
        )

        //내용
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ){

            //질문 + 이전, 다음 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //질문
                Text(
                    text = content[currentContent].question,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(8f)
                )

                //이전 버튼
                Button(
                    onClick = {
                        if (currentContent > 0) {
                            currentContent -= 1
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentContent == 0) Color.Gray else Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp))
                }

                Spacer(modifier = Modifier.width(8.dp))
                //다음 버튼
                Button(
                    onClick = {
                        if (currentContent < contentSize - 1) {
                            currentContent += 1
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentContent == contentSize - 1) Color.Gray else Color.Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "next",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }

            var userChoiceList = userChoice
            if (userChoiceList.size < contentSize) {
                userChoiceList = userChoiceList.toMutableList().apply {
                    addAll(List(contentSize - userChoiceList.size) { listOf() })
                }
            }


            Spacer(modifier = Modifier.height(20.dp))
            //설문 결과
            ShowResult(result = content[currentContent], userChoice = userChoiceList[currentContent], modifier = Modifier.weight(8f))

        }
    }
}

@Composable
fun ShowResult(result : QnA, userChoice : List<Int>, modifier: Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ){

        //설문 결과 차트
        SelectionPercentageChart(answers = result.answerList,choices = result.answerCountList, userChoices = userChoice )

    }
}


@Composable
fun SelectionPercentageChart(answers: List<String>, choices: List<Int>, userChoices: List<Int>) {
    val total = choices.sum()
    val percentages = choices.map { it.toFloat() / total * 100 }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 차트
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .weight(3f)) {
            val barWidth = size.width / (choices.size * 2)
            percentages.forEachIndexed { index, percentage ->
                val barHeight = size.height * (percentage / 100)
                drawRect(
                    color = if (index in userChoices) Color.Green else Color.Gray,
                    topLeft = Offset(index * 2 * barWidth + barWidth / 2, size.height - barHeight),
                    size = Size(barWidth, barHeight)
                )
                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.drawText(
                        "${percentage.toInt()}%",
                        index * 2 * barWidth + barWidth,
                        size.height - barHeight - 10,
                        Paint().apply {
                            color = android.graphics.Color.BLACK
                            textAlign = Paint.Align.CENTER
                            textSize = 40f
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(5.dp))
        // 답변 리스트
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            answers.forEach { answer ->
                Text(
                    text = answer,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }


    }
}

@Composable
fun ShowCounts(
    isLiked: Boolean,
    like: Int,
    numberOfComment: Int,
    modifier: Modifier,
    clickLikes: () -> Unit,
    clickSortByLikes: () -> Unit,
    clickSortByDate: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    val selectedText by remember { mutableStateOf("댓글 정렬") }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    var boxSize by remember { mutableStateOf(Size.Zero) }
    var buttonSize by remember { mutableStateOf(Size.Zero) }
    var offsetSize by remember { mutableStateOf(Size.Zero) }

    Row(
        modifier = modifier
            .onGloballyPositioned { layoutCoordinates ->
                rowSize = layoutCoordinates.size.toSize()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        //좋아요 버튼
        Icon(
            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Likes",
            modifier = Modifier.clickable {
                clickLikes()
            }
        )

        //좋아요 수
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "$like likes")


        //댓글 수
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Comment,
            contentDescription = "Comments"
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "$numberOfComment comments")



        Spacer(modifier = Modifier.weight(1f))
        //인기순, 최신순 버튼
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { layoutCoordinates ->
                    boxSize = layoutCoordinates.size.toSize()
                }
        ){
            Button(
                onClick = {expanded = true},
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .clip(RoundedCornerShape(6.dp))
                    .onGloballyPositioned { layoutCoordinates ->
                        buttonSize = layoutCoordinates.size.toSize()
                    }

                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = selectedText,
                    modifier = Modifier.safeContentPadding()
                )
            }
            offsetSize = Size(rowSize.width - buttonSize.width, 0F)


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                offset = DpOffset(offsetSize.width.dp, 0.dp)

            ){

                DropdownMenuItem(text = { Text(text = "인기순") },
                    onClick = { clickSortByLikes(); expanded = false})

                DropdownMenuItem(text = { Text(text = "최신순") },
                    onClick = { clickSortByDate(); expanded = false})
            }


        }

    }
}


@Composable
fun ShowComments(user : User, comments: List<Comment>, surveyResult: SurveyV2, modifier: Modifier){
    var sendInfo by remember { mutableStateOf(true) }
    val changedCommentList by rememberSaveable { mutableStateOf(mutableListOf<changedComment>()) }
    Log.d("댓글2", "처음 리스트 : $changedCommentList")

    LazyColumn(
        modifier = modifier
    ) {
        items(comments) { comment ->

            var like by rememberSaveable { mutableIntStateOf(comment.like.count) }
            var dislike by rememberSaveable { mutableIntStateOf(comment.dislike.count) }

            var isLiked by rememberSaveable { mutableStateOf(
                comment.like.usersWhoLiked.find { it == user.userId } != null) }

            var isDisliked by rememberSaveable { mutableStateOf(
                comment.dislike.usersWhoDisliked.find { it == user.userId } != null) }

            LaunchedEffect(comments) {
                like = comment.like.count
                dislike = comment.dislike.count
                isLiked = comment.like.usersWhoLiked.find { it == user.userId } != null
                isDisliked = comment.dislike.usersWhoDisliked.find { it == user.userId } != null
            }

            LaunchedEffect(key1 = isLiked, key2 = isDisliked) {
                comment.like.count = like
                comment.dislike.count = dislike
                val changedComment = changedComment(comment.commentId, isLiked, isDisliked)

                if (changedCommentList.find { it.commentId == changedComment.commentId } == null) {
                    changedCommentList.add(changedComment)
                    Log.d("댓글2", "리스트 추가 : $changedCommentList")
                }
                else if (changedCommentList.find { it.commentId == changedComment.commentId } != changedComment) {
                    changedCommentList.remove(changedCommentList.find { it.commentId == changedComment.commentId })
                    changedCommentList.add(changedComment)
                    Log.d("댓글2", "리스트 최신화 : $changedCommentList")
                }

                Log.d("댓글2", "바뀐 코멘트 : $changedComment")
                Log.d("댓글2", "보내는 리스트 : $changedCommentList")

                sendInfo = true

            }

//            LaunchedEffect(key1 = Unit) {
//                sendInfo = true
//            }
//
//            LaunchedEffect(key1 = comments) {
//                sendInfo = true
//            }

            LaunchedEffect(key1 = changedCommentList) {
                Log.d("댓글2", "리스트 변경 감지 : $changedCommentList")
            }

            if (sendInfo) {
                val surveyV2ViewModel : SurveyV2ViewModel = viewModel (factory = SurveyV2ViewModelFactory(
                    SurveyV2Repository()
                ))
                surveyV2ViewModel.getSurvey(surveyResult.surveyId)
                val isSurveyLoaded by surveyV2ViewModel.isSurveyLoaded.collectAsState()
                val loadedSurvey by surveyV2ViewModel.survey.collectAsState(initial = surveyResult)
                try {
                    Log.d("댓글2", "받은 리스트 1: $changedCommentList")
                    if (isSurveyLoaded) {
                        Log.d("댓글2", "댓글 업데이트 시작")
                        sendInfo = false
                        val loadedComments = loadedSurvey!!.comments.toMutableList()
                        for (loadedComment in loadedComments) {
                            Log.d("댓글2", "loadedComment : ${loadedComment.commentId}")

                            val iterator = changedCommentList.iterator()
                            while (iterator.hasNext()) {
                                val changedComment = iterator.next()
                                Log.d("댓글2", "코멘트 : ${changedComment.commentId}")
                                if (loadedComment.commentId == changedComment.commentId) {
                                    loadedComment.like.usersWhoLiked = loadedComment.like.usersWhoLiked.toMutableList().apply {
                                        if (loadedComment.like.usersWhoLiked.find { it == user.userId } != null) {
                                            Log.d("댓글2", "좋아요 누른 유저 찾음")
                                            if (!changedComment.isLiked) {
                                                Log.d("댓글2", "좋아요 취소")
                                                remove(user.userId)
                                            }
                                        } else {
                                            if (changedComment.isLiked) {
                                                Log.d("댓글2", "좋아요 추가")
                                                add(user.userId)
                                            }
                                        }
                                    }
                                    loadedComment.dislike.usersWhoDisliked = loadedComment.dislike.usersWhoDisliked.toMutableList().apply {
                                        if (loadedComment.dislike.usersWhoDisliked.find { it == user.userId } != null) {
                                            if (!changedComment.isDisliked) {
                                                remove(user.userId)
                                            }
                                        } else {
                                            if (changedComment.isDisliked) {
                                                add(user.userId)
                                            }
                                        }
                                    }
                                    iterator.remove() // Iterator를 사용하여 안전하게 제거
                                }
                            }

                            Log.d("댓글2", "좋아요 : ${loadedComment.like.usersWhoLiked}")
                            Log.d("댓글2", "싫어요 : ${loadedComment.dislike.usersWhoDisliked}")

                            loadedComment.like.count = loadedComment.like.usersWhoLiked.size
                            loadedComment.dislike.count = loadedComment.dislike.usersWhoDisliked.size
                        }

                        Log.d("댓글2", "업데이트 된 리스트 : $loadedComments")
                        surveyV2ViewModel.updateSurvey(surveyResult.surveyId, mapOf("comments" to loadedComments))
                    }

                } catch (e: Exception) {
                    Log.d("댓글2", "댓글 업데이트 에러 : $e")
                }


            }

            //좋아요, 싫어요 버튼 클릭시 실행될 함수
            val clickLike = {
                if (!isLiked && !isDisliked) {
                    isLiked = true
                    like += 1

                } else if (isLiked) {
                    isLiked = false
                    like -= 1
                } else {
                    isDisliked = false
                    dislike -= 1
                    isLiked = true
                    like += 1
                }
            }

            val clickDislike = {
                if (!isLiked && !isDisliked) {
                    isDisliked = true
                    dislike += 1
                } else if (isDisliked) {
                    isDisliked = false
                    dislike -= 1
                } else {
                    isLiked = false
                    like -= 1
                    isDisliked = true
                    dislike += 1
                }
            }

            //댓글
            ShowComment(
                comment = comment,
                like = like,
                dislike = dislike,
                isLiked = isLiked,
                isDisliked = isDisliked,
                clickLike = clickLike,
                clickDislike = clickDislike
            )

        }

    }

}

data class changedComment(
    val commentId: String,
    val isLiked: Boolean,
    val isDisliked: Boolean,
)

@Composable
fun ShowComment(
    comment: Comment,
    like: Int = comment.like.count,
    dislike: Int = comment.dislike.count,
    isLiked: Boolean,
    isDisliked: Boolean,
    clickLike: () -> Unit,
    clickDislike: () -> Unit
) {

    Column {
        // 댓글 내용
        Row(
            verticalAlignment = Alignment.Top
        ) {
            // 프로필 이미지
            Image(
                painter = painterResource(id = comment.profileImage.toInt()),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(30.dp))
            )


            // 댓글 내용
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = comment.userId, fontWeight = FontWeight.Bold)
                Text(text = comment.text)
                Text(text = comment.createdDate, color = Color.Gray, fontSize = 15.sp)
            }

        }


        //댓글 -> 좋아요, 싫어요
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 좋아요
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Filled.ThumbUpOffAlt,
                contentDescription = "Likes",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        clickLike()
                    }
            )

            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "$like likes")


            // 싫어요
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = if (isDisliked) Icons.Filled.ThumbDown else Icons.Default.ThumbDownOffAlt,
                contentDescription = "Dislikes",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        clickDislike()
                    })

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$dislike dislikes"
            )

        }
    }
}

@Composable
fun ShowCommentInput(modifier: Modifier, onClickSend: @Composable (String) -> Unit) {

    var userComment by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var sendComment by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier.padding(start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {

        //댓글 입력창
        TextField(
            value = userComment,
            onValueChange = { userComment = it },
            label = { Text("Comment") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (userComment != "") {
                        sendComment = true
                    }

                    else {
                        focusManager.clearFocus()

                    }
                }
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp)
        )

        //전송 버튼
        Button(
            onClick = {
                sendComment = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        if (sendComment) {
            onClickSend(userComment)
            userComment = ""
            sendComment = false
        }

    }
}